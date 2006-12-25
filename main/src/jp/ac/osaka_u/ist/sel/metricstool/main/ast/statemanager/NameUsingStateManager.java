package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;

public class NameUsingStateManager extends EnterExitStateManager{

    public static enum USE_NAME_STATE implements StateChangeEventType{
        ENTER_USING_DEFINITION,
        
        
    }
    @Override
    public  StateChangeEventType getEnterEventType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public  StateChangeEventType getExitEventType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected boolean isStateChangeTriggerToken(AstToken token) {
        // TODO Auto-generated method stub
        return false;
    }

}
