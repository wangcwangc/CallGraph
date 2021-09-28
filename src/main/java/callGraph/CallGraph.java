package callGraph;

import callGraph.edge.MethodCall;
import callGraph.edge.MethodCallManager;
import callGraph.vo.DCGClassVO;
import callGraph.vo.DCGMethodVO;
import callGraph.vo.SourceClassManager;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CallGraph {

    private static CallGraph cg;

    private CallGraph() {

    }

    public static CallGraph getInstance() {
        if (cg == null) {
            cg = new CallGraph();
        }
        return cg;
    }

    //将要处理的所有jar的路径
    List<String> jarPaths;
    //所有类文件的InputStream
    List<InputStream> classesStream;
    //方法调用边
    List<MethodCall> methodCalls;

    public void addPaths(List<String> jarPaths) {
        //清空
//        MethodCallManager.getInstance().clearMethodCalls();
        this.jarPaths = jarPaths;
        try {
            getAllClassesStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getAllClassesStream() throws IOException {
        classesStream = new ArrayList<>();
        for (String jarPath : jarPaths) {
            if (jarPath.endsWith("/classes") || new File(jarPath).isDirectory()) {
                findClassFiles(new File(jarPath));
            } else {
                JarFile jarFile = new JarFile(jarPath);
                Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
                while (jarEntryEnumeration.hasMoreElements()) {
                    JarEntry jarEntry = jarEntryEnumeration.nextElement();
                    if (jarEntry.getName().endsWith(".class")) {
                        classesStream.add(jarFile.getInputStream(jarEntry));
                    }
                }
            }
        }
    }

    private void findClassFiles(File file) {
        try {
            File[] files = file.listFiles();
            for (File subFile : files != null ? files : new File[0]) {
                if (subFile.isDirectory()) findClassFiles(subFile);
                else {
                    if (subFile.getName().endsWith(".class")) {
                        classesStream.add(new FileInputStream(subFile));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<MethodCall> getMethodCalls() {
        return methodCalls;
    }

    public void create() throws IOException {
        SourceClassManager.getAllClassVOFromPaths(jarPaths);
        for (InputStream classInputStream : classesStream) {
            ClassReader classReader = new ClassReader(classInputStream);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);
            for (MethodNode methodNode : classNode.methods) {
                DCGMethodVO callerDCGMethodVO = new DCGMethodVO(classNode.name, methodNode.name, methodNode.desc);
                ListIterator<AbstractInsnNode> instructions = methodNode.instructions.iterator();
                int lineNumber = -1;
                while (instructions.hasNext()) {
                    AbstractInsnNode node = instructions.next();
                    if (node instanceof MethodInsnNode) {
                        DCGMethodVO calledDCGMethodVO = new DCGMethodVO(((MethodInsnNode) node).owner,
                                ((MethodInsnNode) node).name, ((MethodInsnNode) node).desc, node.getOpcode());
                        MethodCall methodCall = new MethodCall(callerDCGMethodVO, calledDCGMethodVO, lineNumber);
                        MethodCallManager.getInstance().addMethodCall(methodCall);
                        addDynamicMethodCall(methodCall);
                    }
                    if (node instanceof LineNumberNode) {
                        lineNumber = ((LineNumberNode) node).line;
                    }
                }
            }
        }
    }

    private void addDynamicMethodCall(MethodCall methodCall) {
        if (Opcodes.ACC_PRIVATE == methodCall.getCallerMethod().getOpcode()) return;
        Map<String, DCGClassVO> dcgClassVOMap = SourceClassManager.DCGClassPool;
        String calledDCGMethodVOClassName = methodCall.getCalledMethod().getClassName();
        if (dcgClassVOMap.containsKey(calledDCGMethodVOClassName)) {
            HashSet<DCGClassVO> set = dcgClassVOMap.get(calledDCGMethodVOClassName).getSubDCGClassVO();
            add(methodCall, set);
        }
    }

    private void add(MethodCall methodCall, Set<DCGClassVO> dcgClassVOS) {
        for (DCGClassVO subDCGClassVO : dcgClassVOS) {
            if (subDCGClassVO.getSubDCGClassVO().size() > 0) {
                add(methodCall, subDCGClassVO.getSubDCGClassVO());
            }
            DCGMethodVO calledMethod = methodCall.getCalledMethod();
            DCGMethodVO dynamicDCGMethodVO = new DCGMethodVO(subDCGClassVO.getClassName(),
                    calledMethod.getMethodName(), calledMethod.getDesc());
            MethodCall dynamicMethodCall = new MethodCall(methodCall.getCallerMethod(), dynamicDCGMethodVO,
                    methodCall.getLineNumber());
            MethodCallManager.getInstance().addMethodCall(dynamicMethodCall);
        }
    }
}
