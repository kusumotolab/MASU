package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.DeclaredBlockStateManager.DeclaredBlockState;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

public class CaseGroupStateManager extends InnerBlockStateManager {

    public static enum CASE_GROUP_STATE_CHANGE implements StateChangeEventType {
        ENTER_ENTRY_DEF, EXIT_ENTRY_DEF,
    }
    
    public static enum CASE_ENTRY_STATE implements DeclaredBlockState {
        CASE_DEF, DEFAULT_DEF
    }
    
    @Override
    public void entered(final AstVisitEvent event) {
        final AstToken token = event.getToken();

        if (this.isStateChangeTriggerEvent(event)) {
            super.entered(event);

            if (this.isEntryDefinitionToken(token) && STATE.DECLARE == this.state) {
                if(token.isCase()) {
                    this.state = CASE_ENTRY_STATE.CASE_DEF;
                } else if(token.isDefault()) {
                    this.state = CASE_ENTRY_STATE.DEFAULT_DEF;
                }
                
                this.fireStateChangeEvent(CASE_GROUP_STATE_CHANGE.ENTER_ENTRY_DEF, event);
            }
        }
    }
    
    @Override
    public void exited(final AstVisitEvent event) {
        final AstToken token = event.getToken();

        if (this.isStateChangeTriggerEvent(event)) {
            super.exited(event);

            if (this.isEntryDefinitionToken(token) && STATE.DECLARE == this.state) {
                this.fireStateChangeEvent(CASE_GROUP_STATE_CHANGE.EXIT_ENTRY_DEF, event);
            }
        }
    }
    
    protected boolean isEntryDefinitionToken(final AstToken token) {
        return token.isEntryDefinition();
    }
    
    @Override
    protected final boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        return super.isStateChangeTriggerEvent(event) || isEntryDefinitionToken(event.getToken());
    }
    
    @Override
    protected boolean isDefinitionEvent(final AstVisitEvent event) {
        return event.getToken().isCaseGroupDefinition();
    }

    @Override
    protected boolean isBlockToken(AstToken token) {
        return token.isSList();
    }
    
    
}
