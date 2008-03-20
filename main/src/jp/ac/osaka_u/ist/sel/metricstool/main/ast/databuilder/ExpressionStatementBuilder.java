package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionStatementInfo;


public class ExpressionStatementBuilder extends
        SingleStatementBuilder<UnresolvedExpressionStatementInfo> {

    public ExpressionStatementBuilder(ExpressionElementManager expressionManager,
            BuildDataManager buildDataManager) {
        super(expressionManager, buildDataManager);
    }

    @Override
    protected UnresolvedExpressionStatementInfo buildStatement(final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        final UnresolvedExpressionInfo<? extends ExpressionInfo> returnedStatement = this
        .getLastBuiltExpression();

        final UnresolvedExpressionStatementInfo expressionStatement = new UnresolvedExpressionStatementInfo(
                returnedStatement);
        expressionStatement.setFromLine(fromLine);
        expressionStatement.setFromColumn(fromColumn);
        expressionStatement.setToLine(toLine);
        expressionStatement.setToColumn(toColumn);
        
        return expressionStatement;
    }

    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.isExpressionStatement();
    }

}
