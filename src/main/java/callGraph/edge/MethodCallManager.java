package callGraph.edge;

import java.util.HashSet;
import java.util.Set;

public class MethodCallManager {
    private static MethodCallManager instance;

    public static MethodCallManager getInstance() {
        if (instance == null) {
            instance = new MethodCallManager();
        }
        return instance;
    }

    private MethodCallManager() {
        methodCalls = new HashSet<MethodCall>();
    }

    Set<MethodCall> methodCalls;

    public void addMethodCall(MethodCall methodCall) {
        methodCalls.add(methodCall);
    }

    public Set<MethodCall> getMethodCalls() {
        return methodCalls;
    }

    public void clearMethodCalls() {
        methodCalls.clear();
    }
}
