package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * 代入文を表すクラス
 * 
 * @author higo
 *
 */
public class AssignmentExpressionInfo implements ExpressionInfo {

    /**
     * 必要な情報を与えて，オブジェクトを初期化
     * 
     * @param leftVariable 左辺の変数
     * @param rightExpression 右辺の式
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public AssignmentExpressionInfo(final VariableUsageInfo<?> leftVariable,
            final ExpressionInfo rightExpression, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        this.leftVariable = leftVariable;
        this.rightExpression = rightExpression;

        this.fromLine = fromLine;
        this.fromColumn = fromColumn;
        this.toLine = toLine;
        this.toColumn = toColumn;
    }

    /**
     * 位置情報に基づいて比較する
     */
    @Override
    public int compareTo(ExpressionInfo o) {

        if (null == o) {
            throw new NullPointerException();
        }

        if (this.getFromLine() < o.getFromLine()) {
            return 1;
        } else if (this.getFromLine() > o.getFromLine()) {
            return -1;
        } else if (this.getFromColumn() < o.getFromColumn()) {
            return 1;
        } else if (this.getFromColumn() > o.getFromColumn()) {
            return -1;
        } else if (this.getToLine() < o.getToLine()) {
            return 1;
        } else if (this.getToLine() > o.getToLine()) {
            return -1;
        } else if (this.getToColumn() < o.getToColumn()) {
            return 1;
        } else if (this.getToColumn() > o.getToColumn()) {
            return -1;
        }

        return 0;
    }

    /**
     * 左辺の変数を返す
     * 
     * @return 左辺の変数
     */
    public VariableUsageInfo<?> getLeftVariable() {
        return this.leftVariable;
    }

    /**
     * 右辺の式を返す
     * 
     * @return 右辺の式
     */
    public ExpressionInfo getRightExpression() {
        return this.rightExpression;
    }

    /**
     * 開始行を返す
     * 
     * @return 開始行
     */
    public final int getFromLine() {
        return this.fromLine;
    }

    /**
     * 開始列を返す
     * 
     * @return 開始列
     */
    public final int getFromColumn() {
        return this.fromColumn;
    }

    /**
     * 終了行を返す
     * 
     * @return 終了行
     */
    public final int getToLine() {
        return this.toLine;
    }

    /**
     * 終了列を返す
     * 
     * @return 終了列
     */
    public final int getToColumn() {
        return this.toColumn;
    }

    /**
     * この式（代入文）における変数利用の一覧を返す
     * 
     * @return 変数利用のSet
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        final SortedSet<VariableUsageInfo<?>> variableUsages = new TreeSet<VariableUsageInfo<?>>();
        variableUsages.addAll(this.getLeftVariable().getVariableUsages());
        variableUsages.addAll(this.getRightExpression().getVariableUsages());
        return variableUsages;
    }

    /**
     * 開始行を保存するための変数
     */
    private final int fromLine;

    /**
     * 開始列を保存するための変数
     */
    private final int fromColumn;

    /**
     * 終了行を保存するための変数
     */
    private final int toLine;

    /**
     * 開始列を保存するための変数
     */
    private final int toColumn;

    private final VariableUsageInfo<?> leftVariable;

    private final ExpressionInfo rightExpression;
}
