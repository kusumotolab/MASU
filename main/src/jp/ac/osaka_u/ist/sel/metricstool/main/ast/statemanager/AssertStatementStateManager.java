package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


public class AssertStatementStateManager extends SingleStatementStateManager {

    public interface AssertStatementState {
    }

    protected static enum STATE implements AssertStatementState {

    }

    
    
    public static enum ASSERT_STATEMENT_STATE_CHANGE implements StateChangeEventType {

        ENTER, EXIT,

        ENTER_ASSERT_EXPRESSION, EXIT_ASSERT_EXPRESSION,

        ENTER_MESSAGE_EXPRESSION, EXIT_MESSAGE_EXPRESSION
    }

    @Override
    public StateChangeEventType getStatementEnterEventType() {
        return ASSERT_STATEMENT_STATE_CHANGE.ENTER;
    }

    @Override
    public StateChangeEventType getStatementExitEventType() {
        return ASSERT_STATEMENT_STATE_CHANGE.EXIT;
    }

    @Override
    protected boolean isStateChangeTriggerEvent(AstVisitEvent event) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    protected boolean isHeaderToken(AstToken token) {
        // TODO Auto-generated method stub
        return false;
    }

}
