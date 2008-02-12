package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * 変数使用を表す抽象クラス
 * 
 * @author higo
 *
 */
public abstract class VariableUsageInfo<T extends VariableInfo> extends EntityUsageInfo {

    VariableUsageInfo(final T usedVariable, final boolean reference, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        this.usedVariable = usedVariable;
        this.reference = reference;
    }

    public final T getUsedVariable() {
        return this.usedVariable;
    }

    public final boolean isReference() {
        return this.reference;
    }

    /**
     * このフィールド使用が代入であるかどうかを返す
     * 
     * @return 代入である場合は true，参照である場合は false
     */
    public final boolean isAssignment() {
        return !this.reference;
    }

    private final T usedVariable;

    private final boolean reference;
}
