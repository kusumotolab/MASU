package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;


public class ExpressionStatementBuilder extends
        SingleStatementBuilder<UnresolvedExpressionInfo<? extends ExpressionInfo>> {

    public ExpressionStatementBuilder(ExpressionElementManager expressionManager,
            BuildDataManager buildDataManager) {
        super(expressionManager, buildDataManager);
    }

    @Override
    protected UnresolvedExpressionInfo<? extends ExpressionInfo> buildStatement(final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        final UnresolvedExpressionInfo<? extends ExpressionInfo> singleStatement = this.expressionManager
                .getPeekExpressionElement().getUsage();
        return singleStatement;
    }

    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.isExpressionStatement();
    }

}
