package callGraph.vo;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AbstractInsnNode;
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
    static ClassLoader classLoader = new ClassLoader() {
    };

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
        ArrayList<InputStream> classesStream = new ArrayList<InputStream>();
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


    public static Set<DCGClassVO> getAllClassVOFromPaths(List<String> paths) throws IOException {
        Set<DCGClassVO> classSet = new HashSet<>();
        List<InputStream> classesStream = getAllClassesStream(paths);
        for (InputStream classInputStream : classesStream) {
            ClassReader classReader = new ClassReader(classInputStream);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);
            List<MethodVO> methodVOList = new ArrayList<>();
            for (MethodNode methodNode : classNode.methods) {
                MethodVO methodVO = new MethodVO(classNode.name, methodNode.name, methodNode.desc);
                methodVOList.add(methodVO);
            }
            classSet.add(new DCGClassVO(classNode.access, classNode.superName, classNode.name, methodVOList));
        }
        return classSet;
    }
}