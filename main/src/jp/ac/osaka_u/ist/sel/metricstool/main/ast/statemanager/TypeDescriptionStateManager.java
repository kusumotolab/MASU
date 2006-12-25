package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;

public class TypeDescriptionStateManager extends EnterExitStateManager{
    public static enum TYPE_STATE implements StateChangeEventType{
        ENTER_TYPE,
        EXIT_TYPE
    }
    
    @Override
    public StateChangeEventType getEnterEventType() {
        return TYPE_STATE.ENTER_TYPE;
    }

    @Override
    public StateChangeEventType getExitEventType() {
        return TYPE_STATE.EXIT_TYPE;
    }

    @Override
    protected boolean isStateChangeTriggerToken(AstToken token) {
        return token.isTypeDefinition();
    }

}
