package callGraph.vo;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class SourceClassManager {

    public static Map<String, DCGClassVO> DCGClassPool = new HashMap<>();

    public static Set<String> getAllClassesFromPaths(List<String> paths) throws IOException {
        Set<String> classSet = new HashSet<>();
        List<InputStream> classesStream = getAllClassesStream(paths);
        for (InputStream classInputStream : classesStream) {
            ClassReader classReader = new ClassReader(classInputStream);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);
            classSet.add(classNode.name.replaceAll("/", "."));
        }
        return classSet;
    }

    private static List<InputStream> getAllClassesStream(List<String> jarPaths) throws IOException {
        ArrayList<InputStream> classesStream = new ArrayList<>();
        for (String jarPath : jarPaths) {
            if (jarPath.endsWith("/classes") || new File(jarPath).isDirectory()) {
                classesStream.addAll(findClassFiles(new File(jarPath)));
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
        return classesStream;
    }

    private static List<InputStream> findClassFiles(File file) {
        ArrayList<InputStream> classesStream = new ArrayList<>();
        try {
            File[] files = file.listFiles();
            for (File subFile : files != null ? files : new File[0]) {
                if (subFile.isDirectory()) classesStream.addAll(findClassFiles(subFile));
                else {
                    if (subFile.getName().endsWith(".class")) {
                        classesStream.add(new FileInputStream(subFile));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classesStream;
    }


    public static Map<String, DCGClassVO> getAllClassVOFromPaths(List<String> paths) throws IOException {
        List<InputStream> classesStream = getAllClassesStream(paths);
        for (InputStream classInputStream : classesStream) {
            ClassReader classReader = new ClassReader(classInputStream);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);
            String className = classNode.name.replaceAll("/", ".");
            DCGClassVO dcgClassVO = DCGClassPool.computeIfAbsent(className, k -> new DCGClassVO());
            dcgClassVO.setAccess(classNode.access);
            dcgClassVO.setClassName(classNode.name);
            dcgClassVO.setSuperName(classNode.superName);
            List<DCGMethodVO> DCGMethodVOList = new ArrayList<>();
            for (MethodNode methodNode : classNode.methods) {
                DCGMethodVO DCGMethodVO = new DCGMethodVO(methodNode.access, classNode.name, methodNode.name, methodNode.desc);
                DCGMethodVOList.add(DCGMethodVO);
            }
            dcgClassVO.setMethods(DCGMethodVOList);
            addSuperDCGClassVO(dcgClassVO, classNode.superName.replaceAll("/", "."));
        }
        return DCGClassPool;
    }

    private static void addSuperDCGClassVO(DCGClassVO dcgClassVO, String superClassName) {
        DCGClassVO superDCGClassVO = DCGClassPool.computeIfAbsent(superClassName, k -> new DCGClassVO());
        superDCGClassVO.setClassName(superClassName);
        superDCGClassVO.addSubDCGClassVO(dcgClassVO);
        dcgClassVO.setSuperDCGClassVO(superDCGClassVO);
    }
}