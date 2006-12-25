package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;

public class ModifiersDefinitionStateManager extends EnterExitStateManager{

    public static enum MODIFIERS_STATE implements StateChangeEventType{
        ENTER_MODIFIERS_DEF,EXIT_MODIFIERS_DEF
    }
    
    @Override
    public  StateChangeEventType getEnterEventType() {
        return MODIFIERS_STATE.ENTER_MODIFIERS_DEF;
    }

    @Override
    public  StateChangeEventType getExitEventType() {
        return MODIFIERS_STATE.EXIT_MODIFIERS_DEF;
    }

    @Override
    protected boolean isStateChangeTriggerToken(AstToken token) {
        return token.isModifiersDefinition();
    }

}
