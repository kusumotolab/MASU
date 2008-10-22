package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.AssertStatementStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.AssertStatementStateManager.ASSERT_STATEMENT_STATE_CHANGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedAssertStatementInfo;


public class AssertStatementBuilder extends StateDrivenDataBuilder<UnresolvedAssertStatementInfo> {

    public AssertStatementBuilder(final ExpressionElementManager expressioManager,
            final BuildDataManager buildDataManager) {
        this.expressionManager = expressioManager;
        this.buildDaraManager = buildDataManager;

        this.stateManager = new AssertStatementStateManager();
    }

    protected UnresolvedAssertStatementInfo buildStatement(int fromLine, int fromColumn,
            int toLine, int toColumn) {

        final UnresolvedAssertStatementInfo assertStatement = new UnresolvedAssertStatementInfo();
        assertStatement.setFromLine(fromLine);
        assertStatement.setFromColumn(fromColumn);
        assertStatement.setToLine(toLine);
        assertStatement.setToColumn(toColumn);

        return assertStatement;
    }

    @Override
    public void stateChangend(StateChangeEvent<AstVisitEvent> event) {
        final StateChangeEventType type = event.getType();
        final AstVisitEvent trigger = event.getTrigger();

        if (type.equals(this.stateManager.getStatementEnterEventType())) {
            final UnresolvedAssertStatementInfo assertStatement = this.buildStatement(trigger
                    .getStartLine(), trigger.getStartColumn(), trigger.getEndLine(), trigger
                    .getEndColumn());
            this.registBuiltData(assertStatement);
        } else if (type.equals(this.stateManager.getStatementExitEventType())) {
            this.buildDaraManager.getCurrentLocalSpace().addStatement(this.getLastBuildData());
        } else if (type.equals(ASSERT_STATEMENT_STATE_CHANGE.ENTER_ASSERT_EXPRESSION)) {

        } else if (type.equals(ASSERT_STATEMENT_STATE_CHANGE.EXIT_ASSERT_EXPRESSION)) {
            if (null != this.expressionManager
                    && null != this.expressionManager.getPeekExpressionElement()) {
                this.getLastBuildData().setAsserttedExpression(
                        this.expressionManager.getPeekExpressionElement().getUsage());
            }
        } else if (type.equals(ASSERT_STATEMENT_STATE_CHANGE.ENTER_MESSAGE_EXPRESSION)) {

        } else if (type.equals(ASSERT_STATEMENT_STATE_CHANGE.EXIT_MESSAGE_EXPRESSION)) {
            if (null != this.expressionManager
                    && null != this.expressionManager.getPeekExpressionElement()) {
                this.getLastBuildData().setMessageExpression(
                        this.expressionManager.getPeekExpressionElement().getUsage());
            }
        }
    }

    private final ExpressionElementManager expressionManager;

    private final BuildDataManager buildDaraManager;

    private final AssertStatementStateManager stateManager;
}
