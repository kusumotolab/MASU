package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import java.util.Set;


public final class TypeParameterUsageInfo extends EntityUsageInfo {

    public TypeParameterUsageInfo(final EntityUsageInfo entityUsage, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        if (null == entityUsage) {
            throw new NullPointerException();
        }

        this.entityUsage = entityUsage;
    }

    @Override
    public TypeInfo getType() {
        return this.entityUsage.getType();
    }

    public EntityUsageInfo getEntityUsage() {
        return this.entityUsage;
    }

    /**
     * 型パラメータの使用に変数使用が含まれることはないので空のセットを返す
     * 
     * @return 空のセット
     */
    @Override
    public final Set<VariableUsageInfo<?>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }
    
    private final EntityUsageInfo entityUsage;
}
