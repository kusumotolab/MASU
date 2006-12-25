package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

/**
 * @author kou-tngt
 *
 */
public abstract class VariableDefinitionStateManager extends StackedAstVisitStateManager<VariableDefinitionStateManager.STATE>{

    public static enum VARIABLE_STATE implements StateChangeEventType {
        ENTER_VARIABLE_DEF,
        ENTER_VARIABLE_INITIALIZER,
        EXIT_VARIABLE_INITIALIZER,
        EXIT_VARIABLE_DEF;
    }
    
    public void entered(AstVisitEvent event){
        super.entered(event);
        
        AstToken token = event.getToken();
        if (isDefinitionToken(token)){
            this.state = STATE.DEF;
            fireStateChangeEvent(VARIABLE_STATE.ENTER_VARIABLE_DEF,event);
        } else if (token.isAssignmentOperator() && STATE.DEF == this.state){
            this.state = STATE.INITIALIZE;
            fireStateChangeEvent(VARIABLE_STATE.ENTER_VARIABLE_INITIALIZER,event);
        }
    }
    
    public void exited(AstVisitEvent event){
        super.exited(event);
        
        AstToken token = event.getToken();
        if (isDefinitionToken(token)){
            fireStateChangeEvent(VARIABLE_STATE.EXIT_VARIABLE_DEF,event);
        } else if (token.isAssignmentOperator() && STATE.DEF == this.state){
            fireStateChangeEvent(VARIABLE_STATE.EXIT_VARIABLE_INITIALIZER,event);
        }
    }
    
    public boolean isInDefinition(){
        return STATE.DEF == this.state;
    }
    
    public boolean isInInitializer(){
        return STATE.INITIALIZE == this.state;
    }
    
    @Override
    protected STATE getState() {
        return this.state;
    }
    
    protected abstract boolean isDefinitionToken(AstToken token);
    
    
    @Override
    protected boolean isStateChangeTriggerToken(AstToken token) {
        return token.isAssignmentOperator() || isDefinitionToken(token);
    }

    @Override
    protected void setState(STATE state) {
        this.state = state;
    }
    
    private STATE state = STATE.NOT;
    
    protected static enum STATE{NOT,DEF,INITIALIZE}

    
}
