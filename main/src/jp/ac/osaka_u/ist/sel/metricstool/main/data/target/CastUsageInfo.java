package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public final class CastUsageInfo extends EntityUsageInfo {

    public CastUsageInfo(final TypeInfo castType, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == castType) {
            throw new IllegalArgumentException();
        }

        this.castType = castType;
    }

    @Override
    public TypeInfo getType() {
        return this.castType;
    }

    private final TypeInfo castType;
}
