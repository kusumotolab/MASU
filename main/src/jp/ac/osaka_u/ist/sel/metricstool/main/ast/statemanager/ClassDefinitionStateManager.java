package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;


public class ClassDefinitionStateManager extends DeclaredBlockStateManager {
    public static enum CLASS_STATE_CHANGE implements StateChangeEventType {
        ENTER_CLASS_DEF, EXIT_CLASS_DEF,
        ENTER_CLASS_BLOCK, EXIT_CLASS_BLOCK;
    }
    
    @Override
    protected StateChangeEventType getBlockEnterEventType() {
        return CLASS_STATE_CHANGE.ENTER_CLASS_BLOCK;
    }

    @Override
    protected StateChangeEventType getBlockExitEventType() {
        return CLASS_STATE_CHANGE.EXIT_CLASS_BLOCK;
    }

    @Override
    protected StateChangeEventType getDefinitionEnterEventType() {
        return CLASS_STATE_CHANGE.ENTER_CLASS_DEF;
    }

    @Override
    protected StateChangeEventType getDefinitionExitEventType() {
        return CLASS_STATE_CHANGE.EXIT_CLASS_DEF;
    }

    @Override
    protected boolean isDefinitionToken(final AstToken token) {
        return token.isClassDefinition();
    }
    
    @Override
    protected boolean isBlockToken(final AstToken token){
        return token.isClassBlock();
    }
}
