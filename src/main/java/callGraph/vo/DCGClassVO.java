package callGraph.vo;

import java.util.List;

public class DCGClassVO {
    private int access;
    private String superName;
    private String className;
    private List<DCGMethodVO> methods;

    public DCGClassVO(int access, String superName, String className, List<DCGMethodVO> methods) {
        this.access = access;
        this.superName = superName.replaceAll("/", ".");
        this.className = className.replaceAll("/", ".");
        this.methods = methods;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public String getSuperName() {
        return superName;
    }

    public void setSuperName(String superName) {
        this.superName = superName;
    }

    public List<DCGMethodVO> getMethods() {
        return methods;
    }

    public void setMethods(List<DCGMethodVO> methods) {
        this.methods = methods;
    }
}
