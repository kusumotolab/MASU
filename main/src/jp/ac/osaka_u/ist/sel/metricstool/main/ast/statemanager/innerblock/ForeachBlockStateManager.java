package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

/**
 * Foreach文のステートマネージャ
 * @author a-saitoh
 *
 */
public class ForeachBlockStateManager extends InnerBlockStateManager {

    public static enum FOREACH_BLOCK_STATE_CHANGE implements StateChangeEventType{
        ENTER_FOR_EACH_CLAUSE, EXIT_FOR_EACH_CLAUSE, ENTER_LOCAL_PARAMETER, EXIT_LOCAL_PARAMETER
    }
   

    @Override
    protected boolean fireStateChangeEnterEvent(final AstVisitEvent event){
        
        // 既にイベントが発行済みの場合，何もせず終了
        if (super.fireStateChangeEnterEvent(event)) {
            return true;
        }
        boolean isFired = false;
    
        if(STATE.DECLARE == this.getState()){
            final AstToken token = event.getToken();
            if(event.getToken().isLocalParameterDefinition()){
                this.fireStateChangeEvent(FOREACH_BLOCK_STATE_CHANGE.ENTER_LOCAL_PARAMETER, event);
                isFired = true;
            }
            else if(this.isForEachClause(token)){
                this.fireStateChangeEvent(FOREACH_BLOCK_STATE_CHANGE.ENTER_FOR_EACH_CLAUSE, event);
                isFired = true;
            }

        }
        
        return isFired;
    }
    
    @Override
    protected boolean fireStateChangeExitEvent(AstVisitEvent event){
        // 既にイベントが発行済みの場合，何もせず終了
        if (super.fireStateChangeExitEvent(event)) {
            return true;
        }
        boolean isFired = false;
        
        if(STATE.DECLARE == this.getState()){
            final AstToken token = event.getToken();
            if(event.getToken().isLocalParameterDefinition()){
                this.fireStateChangeEvent(FOREACH_BLOCK_STATE_CHANGE.EXIT_LOCAL_PARAMETER, event);
                isFired = true;
            }
            else if(this.isForEachClause(token)){
                this.fireStateChangeEvent(FOREACH_BLOCK_STATE_CHANGE.EXIT_FOR_EACH_CLAUSE, event);
                isFired = true;
            }
 
        }
        return isFired;
    }
    
    @Override
    protected boolean isStateChangeTriggerEvent(final AstVisitEvent event){
        final AstToken token = event.getToken();
        return super.isStateChangeTriggerEvent(event) || this.isForEachClause(token) || token.isLocalParameterDefinition();
    }
    
    protected boolean isForEachClause(final AstToken token) {
        return token.isForEachClause();
    }
    
    @Override
    protected boolean isDefinitionEvent(AstVisitEvent event) {
        return event.getToken().isForeach();
    }

}
