package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


/**
 * 未解決変数使用を保存するためのクラス
 * 
 * @author t-miyake, higo
 *
 */
public abstract class UnresolvedVariableUsageInfo<T extends VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>
        extends UnresolvedEntityUsageInfo<T> {

    public UnresolvedVariableUsageInfo(final String usedVariableName, final boolean reference,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        if (null == usedVariableName) {
            throw new IllegalArgumentException("usedVarialbeName is null");
        }

        this.usedVariableName = usedVariableName;
        this.reference = reference;

        this.setFromLine(fromLine);
        this.setFromColumn(fromColumn);
        this.setToLine(toLine);
        this.setToColumn(toColumn);
    }

    /**
     * この変数使用が参照であるかどうかを返す
     * 
     * @return 参照である場合は true，代入である場合は false
     */
    public final boolean isReference() {
        return this.reference;
    }

    /**
     * この変数使用が代入であるかどうかを返す
     * 
     * @return 代入である場合は true，参照である場合は false
     */
    public final boolean isAssignment() {
        return !this.reference;
    }

    /**
     * 使用されている変数の名前を返す
     * @return 使用されている変数の名前
     */
    public String getUsedVariableName() {
        return this.usedVariableName;
    }

    /**
     * 使用されている変数の名前を保存する変数
     */
    protected final String usedVariableName;

    protected boolean reference;

}
