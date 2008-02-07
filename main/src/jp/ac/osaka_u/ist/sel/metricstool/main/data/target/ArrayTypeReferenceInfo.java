package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public final class ArrayTypeReferenceInfo extends EntityUsageInfo {

    /**
     * オブジェクトを初期化 
     */
    public ArrayTypeReferenceInfo(final ArrayTypeInfo arrayType, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == arrayType) {
            throw new IllegalArgumentException();
        }

        this.arrayType = arrayType;
    }

    @Override
    public TypeInfo getType() {
        return this.arrayType;
    }

    private final ArrayTypeInfo arrayType;
}
