package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


public final class UnknownEntityUsageInfo extends EntityUsageInfo {

    @Override
    public TypeInfo getType() {
        return UnknownTypeInfo.getInstance();
    }

    public UnknownEntityUsageInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);
    }
}
