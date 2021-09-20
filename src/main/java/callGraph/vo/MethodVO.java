package callGraph.vo;

public class MethodVO {
    private String className;
    private String methodName;
    private String desc;

    public MethodVO(String className, String methodName, String desc) {
        this.className = className.replaceAll("/", ".");
        this.methodName = methodName.replaceAll("/", ".");
        this.desc = desc.replaceAll("/", ".");
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSig() {
        return className + ": " + methodName + desc;
    }

    @Override
    public String toString() {
        return getSig();
    }
}
