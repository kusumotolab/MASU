package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

public abstract class EnterExitStateManager extends StackedAstVisitStateManager {

    public void entered(AstVisitEvent e){
        super.entered(e);
        
        if (isStateChangeTriggerToken(e.getToken())){
            enterDepthCount++;
            fireStateChangeEvent(getEnterEventType(),e);
        }
    }
    
    public void exited(AstVisitEvent e){
        super.exited(e);
        
        if (isStateChangeTriggerToken(e.getToken())){
            enterDepthCount--;
            fireStateChangeEvent(getExitEventType(),e);
        }
    }
    
    public boolean isEntered(){
        return 0 < this.enterDepthCount;
    }
    
    public abstract StateChangeEventType getEnterEventType();
    
    public abstract StateChangeEventType getExitEventType();

    @Override
    protected Object getState() {
        return null;
    }
    
    protected int getEnterDepthCount(){
        return this.enterDepthCount;
    }
    
    @Override
    protected abstract boolean isStateChangeTriggerToken(AstToken token);

    @Override
    protected void setState(Object state) {
        //‰½‚à‚µ‚È‚¢
    }
    
    private int enterDepthCount;
}
