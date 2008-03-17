package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public final class CastUsageInfo extends EntityUsageInfo {

    public CastUsageInfo(final TypeInfo castType, final EntityUsageInfo castedUsage, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == castType || null == castedUsage) {
            throw new IllegalArgumentException();
        }
        
        this.castType = castType;
        this.castedUsage = castedUsage;
    }

    @Override
    public TypeInfo getType() {
        return this.castType;
    }
    
    public EntityUsageInfo getCastedUsage() {
        return this.castedUsage;
    }
    
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return this.getCastedUsage().getVariableUsages();
    }

    private final TypeInfo castType;
    
    private final EntityUsageInfo castedUsage;
}
