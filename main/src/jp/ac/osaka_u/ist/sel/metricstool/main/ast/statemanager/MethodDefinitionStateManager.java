package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;


public class MethodDefinitionStateManager extends DeclaredBlockStateManager{
    public static enum METHOD_STATE_CHANGE implements StateChangeEventType{
        ENTER_METHOD_DEF,
        EXIT_METHOD_DEF,
        
        ENTER_METHOD_BLOCK,
        EXIT_METHOD_BLOCK,
        ;
    }
    
    protected StateChangeEventType getBlockEnterEventType() {
        return METHOD_STATE_CHANGE.ENTER_METHOD_BLOCK;
    }

    protected StateChangeEventType getBlockExitEventType() {
        return METHOD_STATE_CHANGE.EXIT_METHOD_BLOCK;
    }

    protected StateChangeEventType getDefinitionEnterEventType() {
        return METHOD_STATE_CHANGE.ENTER_METHOD_DEF;
    }

    protected StateChangeEventType getDefinitionExitEventType() {
        return METHOD_STATE_CHANGE.EXIT_METHOD_DEF;
    }

    protected boolean isDefinitionToken(AstToken token) {
        return token.isMethodDefinition();
    }

}
