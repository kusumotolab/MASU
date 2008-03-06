package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


public final class MonominalOperationInfo extends EntityUsageInfo {

    public MonominalOperationInfo(final EntityUsageInfo operand, final PrimitiveTypeInfo type,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        this.operand = operand;
        this.type = type;
    }

    @Override
    public TypeInfo getType() {
        return this.type;
    }

    public EntityUsageInfo getOperand() {
        return this.operand;
    }

    private final EntityUsageInfo operand;

    private final PrimitiveTypeInfo type;
}
