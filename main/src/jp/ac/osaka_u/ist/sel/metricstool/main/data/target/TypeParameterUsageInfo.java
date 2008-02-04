package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


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

    private final EntityUsageInfo entityUsage;
}
