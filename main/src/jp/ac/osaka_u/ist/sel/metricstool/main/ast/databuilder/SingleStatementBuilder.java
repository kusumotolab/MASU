package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedStatementInfo;


public abstract class SingleStatementBuilder<T extends UnresolvedStatementInfo<? extends StatementInfo>>
        extends DataBuilderAdapter<T> {

    public SingleStatementBuilder(final ExpressionElementManager expressionManager,
            final BuildDataManager buildDataManager) {

        if (null == buildDataManager || null == expressionManager) {
            throw new IllegalArgumentException();
        }

        this.buildDataManager = buildDataManager;
        this.expressionManager = expressionManager;
    }

    @Override
    public void exited(AstVisitEvent e) {
        if (this.isTriggerToken(e.getToken())) {
            final UnresolvedLocalSpaceInfo<? extends LocalSpaceInfo> currentLocal = this.buildDataManager
                    .getCurrentLocalSpace();
            
            if (null != currentLocal) {
                final T singleStatement = this.buildStatement(e.getStartLine(), e.getStartColumn(),
                        e.getEndLine(), e.getEndColumn());

                assert singleStatement != null : "Illegal state: a single statement was not built";

                currentLocal.addStatement(singleStatement);
            }
        }
    }

    protected UnresolvedExpressionInfo<? extends ExpressionInfo> getLastBuiltExpression() {
        return this.expressionManager.getPeekExpressionElement().getUsage();
    }
    
    protected abstract T buildStatement(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn);

    protected abstract boolean isTriggerToken(final AstToken token);

    protected final ExpressionElementManager expressionManager;

    protected final BuildDataManager buildDataManager;
}
