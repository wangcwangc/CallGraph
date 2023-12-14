package callGraph;

import callGraph.edge.MethodCall;
import org.junit.Assert;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

public class CallGraphTest {
    @org.junit.Test
    public void callGraphTest() throws IOException {
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
}
