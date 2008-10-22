package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedReturnStatementInfo;


/**
 * リターン文の情報を構築するクラス
 * 
 * @author t-miyake
 *
 */
public class ReturnStatementBuilder extends SingleStatementBuilder<UnresolvedReturnStatementInfo> {

    /**
     * 構築済みの式情報マネージャー，構築済みデータマネージャーを与えて初期化．
     * 
     * @param expressionManager 構築済み式情報マネージャー
     * @param buildDataManager 構築済みデータマネージャー
     */
    public ReturnStatementBuilder(ExpressionElementManager expressionManager,
            BuildDataManager buildDataManager) {
        super(expressionManager, buildDataManager);
    }

    @Override
    protected UnresolvedReturnStatementInfo buildStatement(final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        final UnresolvedReturnStatementInfo returnStatement = new UnresolvedReturnStatementInfo();
        returnStatement.setFromLine(fromLine);
        returnStatement.setFromColumn(fromColumn);
        returnStatement.setToLine(toLine);
        returnStatement.setToColumn(toColumn);

        return returnStatement;
    }

    @Override
    public void exited(AstVisitEvent e) {
        super.exited(e);

        if (this.isTriggerToken(e.getToken())) {
            if (null != this.getLastBuildData()) {
                final UnresolvedExpressionInfo<? extends ExpressionInfo> returnedExpression = this
                        .getLastBuiltExpression();
                this.getLastBuildData().setReturnedExpression(returnedExpression);
            }
        }
    }

    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.isReturn();
    }

}
