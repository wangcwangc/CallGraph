package callGraph;

import callGraph.edge.MethodCall;
import callGraph.vo.MethodVO;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ListIterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CallGraph {
    //将要处理的所有jar的路径
    List<String> jarPaths;
    //所有类文件的InputStream
    List<InputStream> classesStream;
    //方法调用边
    List<MethodCall> methodCalls;

    public CallGraph(List<String> jarPaths) {
        this.jarPaths = jarPaths;
        try {
            getAllClassesStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getAllClassesStream() throws IOException {
        classesStream = new ArrayList<InputStream>();
        for (String jarPath : jarPaths) {
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

    public List<MethodCall> getMethodCalls() {
        return methodCalls;
    }

    public void create() throws IOException {
        for (InputStream classInputStream : classesStream) {
            ClassReader classReader = new ClassReader(classInputStream);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);
            for (MethodNode methodNode : classNode.methods) {
                MethodVO callerMethodVO = new MethodVO(classNode.name.replaceAll("/", "."), methodNode.name,
                        methodNode.desc);
                ListIterator<AbstractInsnNode> instructions = methodNode.instructions.iterator();
                int lineNumber = -1;
                while (instructions.hasNext()) {
                    AbstractInsnNode node = instructions.next();
                    if (node instanceof MethodInsnNode) {
                        MethodVO calledMethodVO = new MethodVO(((MethodInsnNode) node).owner
                                .replaceAll("/", "."), ((MethodInsnNode) node).name,
                                ((MethodInsnNode) node).desc);
                        MethodCall methodCall = new MethodCall(callerMethodVO, calledMethodVO, lineNumber);
//                        System.out.println(methodCall);
                    }
                    if (node instanceof LineNumberNode) {
                        lineNumber = ((LineNumberNode) node).line;
                    }
                }
            }
        }
    }
}
