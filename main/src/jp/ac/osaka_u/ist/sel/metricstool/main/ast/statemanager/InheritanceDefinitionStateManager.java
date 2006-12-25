package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;


public class InheritanceDefinitionStateManager extends EnterExitStateManager {

    public static enum INHERITANCE_STATE implements StateChangeEventType {
        ENTER_INHERITANCE_DEF, EXIT_INHERITANCE_DEF
    }

    @Override
    public StateChangeEventType getEnterEventType() {
        return INHERITANCE_STATE.ENTER_INHERITANCE_DEF;
    }

    @Override
    public StateChangeEventType getExitEventType() {
        return INHERITANCE_STATE.EXIT_INHERITANCE_DEF;
    }

    @Override
    protected boolean isStateChangeTriggerToken(final AstToken token) {
        return token.isExtendDefinition();
    }

}
