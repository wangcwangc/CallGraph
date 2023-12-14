package callGraph;

import callGraph.edge.MethodCall;
import org.junit.Assert;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

public class CallGraphTest {
    @org.junit.Before
    public void init() {
        CallGraph.getInstance().clearMethodCallManager();
    }

    @org.junit.Test
    public void javaFileCallGraphTest() throws IOException {
        String directoryPath = "testJavaFile";
        // 使用类加载器获取文件夹下的所有文件
        URL directoryUrl = getClass().getClassLoader().getResource(directoryPath);
        Assert.assertNotNull(directoryUrl);
        // 获取调用图
        CallGraph callGraph = CallGraph.getInstance();
        ArrayList<String> jarPaths = new ArrayList<>();
        jarPaths.add(directoryUrl.getPath());
        callGraph.addPaths(jarPaths);
        callGraph.create();
        Set<MethodCall> methodCalls = callGraph.getMethodCalls();
        Assert.assertEquals(4, methodCalls.size());
    }

    @org.junit.Test
    public void jarFileCallGraphTest() throws IOException {
        String directoryPath = "testJarFile";
        // 使用类加载器获取文件夹下的所有文件
        URL directoryUrl = getClass().getClassLoader().getResource(directoryPath);
        Assert.assertNotNull(directoryUrl);
        String jarFile = directoryUrl.getPath() + "/callGraph-0.0.2-SNAPSHOT.jar";
        // 获取调用图
        CallGraph callGraph = CallGraph.getInstance();
        ArrayList<String> jarPaths = new ArrayList<>();
        jarPaths.add(jarFile);
        callGraph.addPaths(jarPaths);
        callGraph.create();
        Set<MethodCall> methodCalls = callGraph.getMethodCalls();
        Assert.assertEquals(293, methodCalls.size());
    }
}
