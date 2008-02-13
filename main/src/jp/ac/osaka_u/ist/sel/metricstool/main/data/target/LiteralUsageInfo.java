package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


public final class LiteralUsageInfo extends EntityUsageInfo {

    public LiteralUsageInfo(final String literal, final PrimitiveTypeInfo type, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        this.literal = literal;
        this.type = type;

    }

    @Override
    public TypeInfo getType() {
        return this.type;
    }

    public String getLiteral() {
        return this.literal;
    }

    private final String literal;

    private final PrimitiveTypeInfo type;
}