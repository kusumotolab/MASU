package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.InnerBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.InnerBlockStateManager.INNER_BLOCK_STATE_CHANGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedForBlockInfo;


public abstract class ConditionalBlockBuilder<TResolved extends ConditionalBlockInfo, T extends UnresolvedConditionalBlockInfo<TResolved>>
        extends InnerBlockBuilder<TResolved, T> {

    protected ConditionalBlockBuilder(final BuildDataManager targetDataManager,
            final InnerBlockStateManager blockStateManager,
            final ExpressionElementManager expressionManager) {
        super(targetDataManager, blockStateManager);

        if (null == expressionManager) {
            throw new IllegalArgumentException("expressionManager is null.");
        }

        this.expressionManager = expressionManager;
    }

    @Override
    public void stateChangend(final StateChangeEvent<AstVisitEvent> event) {
        super.stateChangend(event);
        final StateChangeEventType type = event.getType();

        if (type.equals(INNER_BLOCK_STATE_CHANGE.ENTER_CLAUSE)) {
            //startConditionalClause(event.getTrigger());
        } else if (type.equals(INNER_BLOCK_STATE_CHANGE.EXIT_CLAUSE)) {
            registerConditionalExpression();
        }
    }

    private void registerConditionalExpression() {
        final T buildingBlock = this.getBuildingBlock();

        if (!this.buildingBlockStack.isEmpty()
                && buildingBlock == this.buildManager.getCurrentBlock()) {

            final UnresolvedExpressionInfo<? extends ExpressionInfo> conditionalExpression = this.expressionManager
                    .getLastPoppedExpressionElement().getUsage();

            assert null != conditionalExpression || buildingBlock instanceof UnresolvedForBlockInfo : "Illegal state; conditional expression is not found.";

            this.buildingBlockStack.peek().setConditionalExpression(conditionalExpression);

            //assert buildingBlock.getStatements().size() <= 1 : "Illegal state: the number of conditional statements is more than one.";

            /*if (buildingBlock.getStatements().size() >= 1) {
                final UnresolvedStatementInfo<? extends StatementInfo> statement = buildingBlock
                        .getStatements().iterator().next();
                assert statement instanceof UnresolvedVariableDeclarationStatementInfo : "Illegal state: the conditioanl statement is not a variable declaration.";
                if (statement instanceof UnresolvedVariableDeclarationStatementInfo) {
                    buildingBlock.setConditionalExpression((UnresolvedVariableDeclarationStatementInfo) statement);
                }
            }*/

        }
    }

    protected final ExpressionElementManager expressionManager;

}
