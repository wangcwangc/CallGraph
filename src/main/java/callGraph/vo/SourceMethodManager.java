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

public class SourceMethodManager {

    public static Set<String> getAllMethodsFromPaths(List<String> paths) throws IOException {
        Set<String> methodsSig = new HashSet<>();
        List<InputStream> classesStream = getAllClassesStream(paths);
        for (InputStream classInputStream : classesStream) {
            ClassReader classReader = new ClassReader(classInputStream);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);
            for (MethodNode methodNode : classNode.methods) {
                methodsSig.add((classNode.name + ": " + methodNode.name + methodNode.desc).replaceAll("/", "."));
            }
        }
        return methodsSig;
    }

    private static List<InputStream> getAllClassesStream(List<String> jarPaths) throws IOException {
        ArrayList<InputStream> classesStream = new ArrayList<InputStream>();
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
}
