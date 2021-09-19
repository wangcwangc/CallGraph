package callGraph;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class CallGraph {
    //将要处理的所有jar的路径
    List<String> jarPaths;
    //所有类文件的InputStream
    List<InputStream> classStream;

    public CallGraph(List<String> jarPaths) {
        this.jarPaths = jarPaths;
        try {
            getAllClassesStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void getAllClassesStream() throws IOException {
        classStream = new ArrayList<InputStream>();
        for (String jarPath : jarPaths) {
            JarFile jarFile = new JarFile(jarPath);
            Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
            while (jarEntryEnumeration.hasMoreElements()) {
                JarEntry jarEntry = jarEntryEnumeration.nextElement();
                if (jarEntry.getName().endsWith(".class")) {
                    classStream.add(jarFile.getInputStream(jarEntry));
                }
            }
        }
    }
}
