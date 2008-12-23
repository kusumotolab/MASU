package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * 変数使用を表す抽象クラス
 * 
 * @author higo
 * @param <T> 使用されている変数
 */
public abstract class VariableUsageInfo<T extends VariableInfo<? extends UnitInfo>> extends
        EntityUsageInfo {

    /**
     * 
     * @param ownerExecutableElement オーナーエレメント
     * @param usedVariable 使用されている変数
     * @param reference 参照かどうか
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    VariableUsageInfo(final ExecutableElementInfo ownerExecutableElement, final T usedVariable,
            final boolean reference, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super(ownerExecutableElement, fromLine, fromColumn, toLine, toColumn);

        this.usedVariable = usedVariable;
        this.reference = reference;

    }

    /**
     * 使用されている変数を返す
     * 
     * @return 使用されている変数
     */
    public final T getUsedVariable() {
        return this.usedVariable;
    }

    /**
     * 参照か代入かを返す
     * 
     * @return 参照である場合は true，代入である場合は false
     */
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

    @Override
    public SortedSet<VariableUsageInfo<?>> getVariableUsages() {
        final SortedSet<VariableUsageInfo<?>> variableUsage = new TreeSet<VariableUsageInfo<?>>();
        variableUsage.add(this);
        return Collections.unmodifiableSortedSet(variableUsage);
    }

    /**
     * この変数使用のテキスト表現（型）を返す
     * 
     * @return この変数使用のテキスト表現（型）
     */
    @Override
    public final String getText() {
        final T variable = this.getUsedVariable();
        return variable.getName();
    }

    private final T usedVariable;

    private final boolean reference;

    /**
     * 空の変数利用のSetを表す
     */
    public static final SortedSet<VariableUsageInfo<?>> EmptySet = Collections
            .unmodifiableSortedSet(new TreeSet<VariableUsageInfo<?>>());
}
