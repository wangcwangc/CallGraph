package callGraph.classLoader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class DCGClassLoader extends URLClassLoader {

    public static DCGClassLoader getDCGClassLoader(List<String> paths) {
        URL[] urls = new URL[paths.size()];
        int i = 0;
        for (String path : paths) {
            try {
                urls[i] = new File(path).toURI().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return new DCGClassLoader(urls);
    }

    public DCGClassLoader(URL[] urls) {
        super(urls);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return super.loadClass(name);
    }
}
