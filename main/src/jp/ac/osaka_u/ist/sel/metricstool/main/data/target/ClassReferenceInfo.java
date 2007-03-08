package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


public final class ClassReferenceInfo extends EntityUsageInfo {

    public ClassReferenceInfo(final ClassInfo referredClass) {

        super();

        if (null == referredClass) {
            throw new NullPointerException();
        }

        this.referredClass = referredClass;
    }

    @Override
    public TypeInfo getType() {
        return this.getReferredClass();
    }

    public ClassInfo getReferredClass() {
        return this.referredClass;
    }

    private final ClassInfo referredClass;
}
