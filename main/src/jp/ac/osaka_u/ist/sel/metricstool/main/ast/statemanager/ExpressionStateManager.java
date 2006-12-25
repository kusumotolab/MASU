package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

public class ExpressionStateManager extends StackedAstVisitStateManager<ExpressionStateManager.STATE>{

    public static enum EXPR_STATE implements StateChangeEventType{
        ENTER_EXPR,
        EXIT_EXPR
    }
    
    public void entered(AstVisitEvent event){
        super.entered(event);
        
        AstToken token = event.getToken();
        if (token.isExpression()){
            this.state = STATE.IN;
            fireStateChangeEvent(EXPR_STATE.ENTER_EXPR, event);
        } else if (token.isBlock()){
            this.state = STATE.NOT;
        }
    }
    
    public void exited(AstVisitEvent event){
        super.exited(event);
        
        AstToken token = event.getToken();
        if (token.isExpression()){
            fireStateChangeEvent(EXPR_STATE.EXIT_EXPR, event);
        }
    }
    
    public boolean inExpression(){
        return STATE.IN == this.state;
    }
    
    @Override
    protected boolean isStateChangeTriggerToken(AstToken token) {
        return token.isExpression() || token.isBlock();
    }
    
    @Override
    protected STATE getState() {
        return this.state;
    }

    @Override
    protected void setState(STATE state) {
        this.state = state;
    }
    
    protected static enum STATE {
        NOT,
        IN,
    }
    
    private STATE state = STATE.NOT;
}
