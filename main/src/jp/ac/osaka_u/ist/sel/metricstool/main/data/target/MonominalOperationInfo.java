package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


public final class MonominalOperationInfo extends EntityUsageInfo {

    public MonominalOperationInfo(final EntityUsageInfo term, final PrimitiveTypeInfo type,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        this.term = term;
        this.type = type;
    }

    @Override
    public TypeInfo getType() {
        return this.type;
    }

    public EntityUsageInfo getTerm() {
        return this.term;
    }

    private final EntityUsageInfo term;

    private final PrimitiveTypeInfo type;
}
