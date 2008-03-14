package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public final class UnresolvedVariableDeclarationStatementInfo extends UnresolvedSingleStatementInfo<VariableDeclarationStatementInfo> {

    public UnresolvedVariableDeclarationStatementInfo(
            final UnresolvedLocalVariableInfo declaredVariable,
            final UnresolvedExpressionInfo<? extends ExpressionInfo> initializationExpression) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == declaredVariable) {
            throw new IllegalArgumentException("declaredVariable is null");
        }

        this.declaredLocalVariable = declaredVariable;
        this.initializationExpression = initializationExpression;
    }

    @Override
    public VariableDeclarationStatementInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

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

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        final LocalVariableInfo localVariable = this.declaredLocalVariable.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        if (null != this.initializationExpression) {
            final ExpressionInfo initializationExpression = this.initializationExpression.resolve(
                    usingClass, usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

            this.resolvedInfo = new VariableDeclarationStatementInfo(localVariable,
                    initializationExpression, fromLine, fromColumn, toLine, toColumn);
        } else {
            this.resolvedInfo = new VariableDeclarationStatementInfo(localVariable, null, fromLine,
                    fromColumn, toLine, toColumn);
        }

        return this.resolvedInfo;
    }

    public final UnresolvedLocalVariableInfo getDeclaredLocalVariable() {
        return this.declaredLocalVariable;
    }

    /**
     * 宣言されている変数の初期化式を返す
     * 
     * @return 宣言されている変数の初期化式．初期化されてい場合はnull
     */
    public final UnresolvedExpressionInfo<? extends ExpressionInfo> getInitializationExpression() {
        return this.initializationExpression;
    }

    /**
     * 宣言されている変数が初期化されているかどうかを返す
     * 
     * @return 宣言されている変数が初期化されていればtrue
     */
    public boolean isInitialized() {
        return null != this.initializationExpression;
    }

    /**
     * 宣言されている変数を表すフィールド
     */
    private final UnresolvedLocalVariableInfo declaredLocalVariable;

    /**
     * 宣言されている変数の初期化式を表すフィールド
     */
    private final UnresolvedExpressionInfo<? extends ExpressionInfo> initializationExpression;

}
