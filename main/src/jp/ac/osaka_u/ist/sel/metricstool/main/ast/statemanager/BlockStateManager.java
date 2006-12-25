package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;


public class BlockStateManager extends EnterExitStateManager {

    public static enum BLOCK_STATE_CHANGE implements StateChangeEventType{
        ENTER,EXIT
    };
    
    @Override
    public  StateChangeEventType getEnterEventType() {
        return BLOCK_STATE_CHANGE.ENTER;
    }

    @Override
    public  StateChangeEventType getExitEventType() {
        return BLOCK_STATE_CHANGE.EXIT;
    }

    @Override
    protected boolean isStateChangeTriggerToken(AstToken token) {
        return token.isBlock();
    }

}
