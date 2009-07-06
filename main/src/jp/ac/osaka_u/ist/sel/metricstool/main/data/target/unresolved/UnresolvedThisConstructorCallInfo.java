package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

public class UnresolvedThisConstructorCallInfo extends UnresolvedClassConstructorCallInfo {

    public UnresolvedThisConstructorCallInfo(final UnresolvedClassTypeInfo classType) {
        super(classType);
    }

    public UnresolvedThisConstructorCallInfo(final UnresolvedClassTypeInfo classType,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(classType, fromLine, fromColumn, toLine, toColumn);
    }
}
