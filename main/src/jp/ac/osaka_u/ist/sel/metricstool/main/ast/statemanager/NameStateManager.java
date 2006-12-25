package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;

public class NameStateManager extends EnterExitStateManager{
    public static enum NAME_STATE implements StateChangeEventType{
        ENTER_NAME,
        EXIT_NAME
    }
    
    @Override
    public StateChangeEventType getEnterEventType() {
        return NAME_STATE.ENTER_NAME;
    }

    @Override
    public StateChangeEventType getExitEventType() {
        return NAME_STATE.EXIT_NAME;
    }

    @Override
    protected boolean isStateChangeTriggerToken(AstToken token) {
        return token.isNameDescription();
    }

}
