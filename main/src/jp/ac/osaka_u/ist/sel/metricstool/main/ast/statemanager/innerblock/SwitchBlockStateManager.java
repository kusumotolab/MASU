package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


public class SwitchBlockStateManager extends InnerBlockStateManager {

    public static enum SWITCH_BLOCK_STATE_CHANGE implements StateChangeEventType {
        ENTER_ENTRY_DEF, EXIT_ENTRY_DEF,
    }

    @Override
    public void entered(final AstVisitEvent event) {
        final AstToken token = event.getToken();

        if (this.isStateChangeTriggerEvent(event)) {
            super.entered(event);

            if (this.isEntryDefinitionToken(token) && STATE.BLOCK == this.state) {
                this.fireStateChangeEvent(SWITCH_BLOCK_STATE_CHANGE.ENTER_ENTRY_DEF, event);
            }
        }
    }

    @Override
    public void exited(final AstVisitEvent event) {
        final AstToken token = event.getToken();

        if (this.isStateChangeTriggerEvent(event)) {
            super.exited(event);

            if (this.isBlockToken(token) && STATE.BLOCK == this.state) {
                this.fireStateChangeEvent(SWITCH_BLOCK_STATE_CHANGE.EXIT_ENTRY_DEF, event);
            }
        }
    }
    
    @Override
    protected boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        return super.isStateChangeTriggerEvent(event) || isEntryDefinitionToken(event.getToken());
    }

    private boolean isEntryDefinitionToken(final AstToken token) {
        return token.isCase() || token.isDefault();
    }

    @Override
    protected boolean isDefinitionEvent(final AstVisitEvent event) {
        return event.getToken().isSwitch();
    }

}
