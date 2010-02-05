package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public class JavaUnresolvedExternalFieldInfo {

    public JavaUnresolvedExternalFieldInfo() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.name = null;
        this.type = null;
    }

    public void setName(final String name) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == name) {
            throw new IllegalArgumentException();
        }

        this.name = name;
    }

    public void setType(final String type) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == type) {
            throw new IllegalArgumentException();
        }

        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    private String name;

    private String type;
}
