package callGraph.edge;

import callGraph.vo.MethodVO;

public class MethodCall {

    private MethodVO callerMethod;
    private MethodVO calledMethod;
    private int lineNumber;

    public MethodCall(MethodVO callerMethod, MethodVO calledMethod, int lineNumber) {
        this.callerMethod = callerMethod;
        this.calledMethod = calledMethod;
        this.lineNumber = lineNumber;
    }

    public MethodVO getCallerMethod() {
        return callerMethod;
    }

    public void setCallerMethod(MethodVO callerMethod) {
        this.callerMethod = callerMethod;
    }

    public MethodVO getCalledMethod() {
        return calledMethod;
    }

    public void setCalledMethod(MethodVO calledMethod) {
        this.calledMethod = calledMethod;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    @Override
    public String toString() {
        return "method call [" + callerMethod.getSig() + " -> " + calledMethod.getSig() + "] line: " + lineNumber;
    }
}
