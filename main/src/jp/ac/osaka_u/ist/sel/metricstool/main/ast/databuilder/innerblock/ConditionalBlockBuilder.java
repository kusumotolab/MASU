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

    /*private void startConditionalClause(final AstVisitEvent triggerEvent) {
        if (!this.buildingBlockStack.isEmpty()
                && this.buildingBlockStack.peek() == this.buildManager.getCurrentBlock()) {
            final T currentBlock = (T) this.buildingBlockStack.peek();
            final UnresolvedConditionalClauseInfo conditionalClause = currentBlock
                    .getConditionalExpression();

            this.buildingClauseStack.push(conditionalClause);

            conditionalClause.setFromLine(triggerEvent.getStartLine());
            conditionalClause.setFromColumn(triggerEvent.getStartColumn());
            conditionalClause.setToLine(triggerEvent.getEndLine());
            conditionalClause.setToColumn(triggerEvent.getEndColumn());

            this.buildManager.startConditionalClause(conditionalClause);
        }
    }*/

    private void registerConditionalExpression() {
        final T currentBuildingBlock = this.buildingBlockStack.peek();
        
        if (!this.buildingBlockStack.isEmpty()
                && currentBuildingBlock == this.buildManager.getCurrentBlock()) {

            final UnresolvedExpressionInfo<? extends ExpressionInfo> conditionalExpression = this.expressionManager
                    .getLastPoppedExpressionElement().getUsage();
            
            assert null != conditionalExpression || currentBuildingBlock instanceof UnresolvedForBlockInfo : "Illegal state; conditional expression is not found.";

            this.buildingBlockStack.peek().setConditionalExpression(conditionalExpression);

        }
    }

    protected final ExpressionElementManager expressionManager;
}
