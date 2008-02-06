package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ModifiersDefinitionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

public class SynchronizedBlockStateManager extends InnerBlockStateManager {

    @Override
    public void entered(final AstVisitEvent event) {
        this.modifierStateManager.entered(event);
        super.entered(event);
    }

    @Override
    public void exited(final AstVisitEvent event) {
        this.modifierStateManager.exited(event);
        super.exited(event);
    }
    
    @Override
    protected boolean isDefinitionToken(AstToken token) {
        return token.isSynchronized() && !this.modifierStateManager.isEntered();
    }
    
    private final ModifiersDefinitionStateManager modifierStateManager = new ModifiersDefinitionStateManager();

}
