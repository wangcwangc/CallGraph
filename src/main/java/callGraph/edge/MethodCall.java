package callGraph.edge;

import callGraph.vo.DCGMethodVO;

public class MethodCall {

    private DCGMethodVO callerMethod;
    private DCGMethodVO calledMethod;
    private int lineNumber;

    public MethodCall(DCGMethodVO callerMethod, DCGMethodVO calledMethod, int lineNumber) {
        this.callerMethod = callerMethod;
        this.calledMethod = calledMethod;
        this.lineNumber = lineNumber;
    }

    public DCGMethodVO getCallerMethod() {
        return callerMethod;
    }

    public void setCallerMethod(DCGMethodVO callerMethod) {
        this.callerMethod = callerMethod;
    }

    public DCGMethodVO getCalledMethod() {
        return calledMethod;
    }

    public void setCalledMethod(DCGMethodVO calledMethod) {
        this.calledMethod = calledMethod;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getCallerMethodSig() {
        return callerMethod.getSig();
    }

    public String getCalledMethodSig() {
        return calledMethod.getSig();
    }

    @Override
    public String toString() {
        return "method call [" + callerMethod.getSig() + " -> " + calledMethod.getSig() + "] line: " + lineNumber;
    }
}
