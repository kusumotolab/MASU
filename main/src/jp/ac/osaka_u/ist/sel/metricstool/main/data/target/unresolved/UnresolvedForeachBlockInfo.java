package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public class UnresolvedForeachBlockInfo extends UnresolvedBlockInfo<ForeachBlockInfo> {

    /**
     * 外側のブロック情報を与えて，foreach ブロック情報を初期化
     * 
     * @param outerSpace 外側のブロック
     */
    public UnresolvedForeachBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);
    }

    /**
     * この未解決 for ブロックを解決する
     * 
     * @param usingClass 所属クラス
     * @param usingMethod 所属メソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     */
    @Override
    public ForeachBlockInfo resolve(final TargetClassInfo usingClass,
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

        // この foreach文の位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // 繰り返し用の変数を取得
        final UnresolvedVariableDeclarationStatementInfo unresolvedVariableDeclaration = this
                .getIteratorVariableDeclaration();
        final VariableDeclarationStatementInfo variableDeclaration = unresolvedVariableDeclaration
                .resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                        methodInfoManager);

        // 繰り返し用の式を取得
        final UnresolvedExpressionInfo<?> unresolvedIteratorExpression = this
                .getIteratorExpression();
        final ExpressionInfo iteratorExpression = unresolvedIteratorExpression.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        // 外側の空間を取得
        final UnresolvedLocalSpaceInfo<?> unresolvedLocalSpace = this.getOuterSpace();
        final LocalSpaceInfo outerSpace = unresolvedLocalSpace.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        this.resolvedInfo = new ForeachBlockInfo(usingClass, outerSpace, fromLine, fromColumn,
                toLine, toColumn, variableDeclaration, iteratorExpression);
        return this.resolvedInfo;
    }

    /**
     * 変数定義を設定する
     * 
     * @param iteraotorVariableDeclaration 変数定義
     */
    public void setIteratorVariableDeclaration(
            final UnresolvedVariableDeclarationStatementInfo iteraotorVariableDeclaration) {
        this.iteratorVariableDeclaration = iteraotorVariableDeclaration;
    }

    /**
     * 繰り返し用の式を設定する
     * 
     * @param iteratorExpression 繰り返し用の式
     */
    public void setIteratorExpression(final UnresolvedExpressionInfo<?> iteratorExpression) {
        this.iteratorExpression = iteratorExpression;
    }

    /**
     * 変数定義を返す
     * 
     * @return 変数定義
     */
    public UnresolvedVariableDeclarationStatementInfo getIteratorVariableDeclaration() {
        return this.iteratorVariableDeclaration;
    }

    /**
     * 繰り返し用の式を返す
     * 
     * @return 繰り返し用の式
     */
    public UnresolvedExpressionInfo<?> getIteratorExpression() {
        return this.iteratorExpression;
    }

    private UnresolvedVariableDeclarationStatementInfo iteratorVariableDeclaration;

    private UnresolvedExpressionInfo<?> iteratorExpression;
}
