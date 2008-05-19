package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedThrowStatementInfo;


/**
 * Throw文の情報を構築するクラス
 * 
 * @author t-miyake
 *
 */
public class ThrowStatementBuilder extends SingleStatementBuilder<UnresolvedThrowStatementInfo> {

    /**
     * 構築済み式情報マネージャー，構築済みデータマネージャーを与えて初期化
     * 
     * @param expressionManager 構築済み式情報マネージャー
     * @param buildDataManager 構築済みデータマネージャー
     */
    public ThrowStatementBuilder(ExpressionElementManager expressionManager,
            BuildDataManager buildDataManager) {
        super(expressionManager, buildDataManager);
    }

    @Override
    protected UnresolvedThrowStatementInfo buildStatement(final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        final UnresolvedExpressionInfo<? extends ExpressionInfo> thrownStatement = this
                .getLastBuiltExpression();

        assert null != thrownStatement : "Illegal state: the thrown statement was not found.";
        final UnresolvedThrowStatementInfo throwStatement = new UnresolvedThrowStatementInfo(
                thrownStatement);
        throwStatement.setFromLine(fromLine);
        throwStatement.setFromColumn(fromColumn);
        throwStatement.setToLine(toLine);
        throwStatement.setToColumn(toColumn);

        return throwStatement;
    }

    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.isThrow();
    }

}
