package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


public final class MethodCallInfo extends EntityUsageInfo {

    public MethodCallInfo(final MethodInfo callee) {

        super();

        if (null == callee) {
            throw new NullPointerException();
        }

        this.callee = callee;
    }

    @Override
    public TypeInfo getType() {
        return this.callee.getReturnType();
    }

    public MethodInfo getCallee() {
        return this.callee;
    }

    private final MethodInfo callee;
}
