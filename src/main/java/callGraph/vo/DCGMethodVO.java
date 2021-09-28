package callGraph.vo;

public class DCGMethodVO {
    private int access;
    private String className;
    private String methodName;
    private String desc;
    private int opcode;

    public DCGMethodVO(int access, String className, String methodName, String desc) {
        this.access = access;
        this.className = className.replaceAll("/", ".");
        this.methodName = methodName.replaceAll("/", ".");
        this.desc = desc.replaceAll("/", ".");
    }

    public DCGMethodVO(String className, String methodName, String desc) {
        this.className = className.replaceAll("/", ".");
        this.methodName = methodName.replaceAll("/", ".");
        this.desc = desc.replaceAll("/", ".");
        this.opcode = opcode;
    }

    public DCGMethodVO(String className, String methodName, String desc, int opcode) {
        this.className = className.replaceAll("/", ".");
        this.methodName = methodName.replaceAll("/", ".");
        this.desc = desc.replaceAll("/", ".");
        this.opcode = opcode;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
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

    public int getOpcode() {
        return opcode;
    }

    public void setOpcode(int opcode) {
        this.opcode = opcode;
    }

    @Override
    public String toString() {
        return getSig();
    }
}
