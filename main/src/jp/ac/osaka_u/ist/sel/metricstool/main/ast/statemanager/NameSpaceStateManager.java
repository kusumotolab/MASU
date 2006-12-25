package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;

public class NameSpaceStateManager extends DeclaredBlockStateManager{
    public static enum NAMESPACE_STATE_CHANGE implements StateChangeEventType{
        ENTER_NAMESPACE_DEF,
        EXIT_NAMESPACE_DEF,
        
        ENTER_NAMESPACE_BLOCK,
        EXIT_NAMESPACE_BLOCK,
        ;
    }
    
    protected StateChangeEventType getBlockEnterEventType() {
        return NAMESPACE_STATE_CHANGE.ENTER_NAMESPACE_BLOCK;
    }

    protected StateChangeEventType getBlockExitEventType() {
       return NAMESPACE_STATE_CHANGE.EXIT_NAMESPACE_BLOCK;
    }

    protected StateChangeEventType getDefinitionEnterEventType() {
        return NAMESPACE_STATE_CHANGE.ENTER_NAMESPACE_DEF;
    }

    protected StateChangeEventType getDefinitionExitEventType() {
        return NAMESPACE_STATE_CHANGE.EXIT_NAMESPACE_DEF;
    }

    protected boolean isDefinitionToken(AstToken token) {
        return token.isNameSpaceDefinition();
    }
}
