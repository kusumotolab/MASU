package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

public class UnresolvedSuperConstructorCallInfo extends UnresolvedClassConstructorCallInfo {

    public UnresolvedSuperConstructorCallInfo(final UnresolvedClassTypeInfo classType) {
        super(classType);
    }

    public UnresolvedSuperConstructorCallInfo(final UnresolvedClassTypeInfo classType,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(classType, fromLine, fromColumn, toLine, toColumn);
    }
}
