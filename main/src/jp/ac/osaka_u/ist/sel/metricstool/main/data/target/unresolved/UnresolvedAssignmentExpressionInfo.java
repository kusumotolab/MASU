package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.AssignmentExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決代入文を表すクラス
 * 
 * @author higo
 */
public final class UnresolvedAssignmentExpressionInfo implements
        UnresolvedExpressionInfo<AssignmentExpressionInfo> {

    /**
     * オブジェクトを初期化
     * 
     */
    public UnresolvedAssignmentExpressionInfo() {
        this.leftVariable = null;
        this.rightExpression = null;
        this.resolved = null;
    }

    @Override
    public boolean alreadyResolved() {
        return null != this.resolved;
    }

    @Override
    public AssignmentExpressionInfo getResolved() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolved;
    }

    @Override
    public AssignmentExpressionInfo resolve(TargetClassInfo usingClass,
            CallableUnitInfo usingMethod, ClassInfoManager classInfoManager,
            FieldInfoManager fieldInfoManager, MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // この for文の位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // 左辺を解決
        final UnresolvedVariableUsageInfo<?> unresolvedLeftVariable = this.getLeftVariable();
        final VariableUsageInfo<?> leftVariable = unresolvedLeftVariable.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        // 右辺を解決
        final UnresolvedExpressionInfo<?> unresolvedRightExpression = this.getRightExpression();
        final ExpressionInfo rightExpression = unresolvedRightExpression.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        this.resolved = new AssignmentExpressionInfo(leftVariable, rightExpression, fromLine,
                fromColumn, toLine, toColumn);
        return this.resolved;
    }

    /**
     * 左辺の変数をセットする
     * 
     * @param leftVariable 左辺の変数
     */
    public void setLeftVariable(final UnresolvedVariableUsageInfo<?> leftVariable) {
        this.leftVariable = leftVariable;
    }

    /**
     * 右辺の式をセットする
     * 
     * @param rightExpression 右辺の式
     */
    public void setRightVariable(final UnresolvedExpressionInfo<?> rightExpression) {
        this.rightExpression = rightExpression;
    }

    /**
     * 左辺の変数を返す
     * 
     * @return 左辺の変数
     */
    public UnresolvedVariableUsageInfo<?> getLeftVariable() {
        return this.leftVariable;
    }

    /**
     * 右辺の式を返す
     * 
     * @return 右辺の式
     */
    public UnresolvedExpressionInfo<?> getRightExpression() {
        return this.rightExpression;
    }

    /**
     * 開始行をセットする
     * 
     * @param fromLine 開始行
     */
    public final void setFromLine(final int fromLine) {

        if (fromLine < 0) {
            throw new IllegalArgumentException();
        }

        this.fromLine = fromLine;
    }

    /**
     * 開始列をセットする
     * 
     * @param fromColumn 開始列
     */
    public final void setFromColumn(final int fromColumn) {

        if (fromColumn < 0) {
            throw new IllegalArgumentException();
        }

        this.fromColumn = fromColumn;
    }

    /**
     * 終了行をセットする
     * 
     * @param toLine 終了行
     */
    public final void setToLine(final int toLine) {

        if (toLine < 0) {
            throw new IllegalArgumentException();
        }

        this.toLine = toLine;
    }

    /**
     * 終了列をセットする
     * 
     * @param toColumn 終了列
     */
    public final void setToColumn(final int toColumn) {

        if (toColumn < 0) {
            throw new IllegalArgumentException();
        }

        this.toColumn = toColumn;
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
     * このユニットの行数を返す
     * 
     * @return ユニットの行数
     */
    public final int getLOC() {
        return this.getToLine() - this.getFromLine() + 1;
    }

    /**
     * 開始行を保存するための変数
     */
    private int fromLine;

    /**
     * 開始列を保存するための変数
     */
    private int fromColumn;

    /**
     * 終了行を保存するための変数
     */
    private int toLine;

    /**
     * 開始列を保存するための変数
     */
    private int toColumn;

    private UnresolvedVariableUsageInfo<?> leftVariable;

    private UnresolvedExpressionInfo<?> rightExpression;

    private AssignmentExpressionInfo resolved;
}
