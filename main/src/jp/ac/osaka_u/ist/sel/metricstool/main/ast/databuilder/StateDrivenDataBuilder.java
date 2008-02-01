package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.LinkedHashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.AstVisitStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeListener;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitListener;


public abstract class StateDrivenDataBuilder<T> extends DataBuilderAdapter<T> implements
        StateChangeListener<AstVisitEvent> {

    @Override
    public void entered(final AstVisitEvent e) {
        if (isActive()) {
            for (final AstVisitListener listener : this.stateManagers) {
                listener.entered(e);
            }
        }
    }

    @Override
    public void exited(final AstVisitEvent e) {
        if (isActive()) {
            for (final AstVisitListener listener : this.stateManagers) {
                listener.exited(e);
            }
        }
    }

    public abstract void stateChangend(StateChangeEvent<AstVisitEvent> event);

    /**
     * 
     * @param stateManager
     */
    protected final void addStateManager(AstVisitStateManager stateManager) {
        //注：このメソッドのfinal修飾子は絶対に外してはならない．

        this.stateManagers.add(stateManager);
        stateManager.addStateChangeListener(this);
    }

    private final Set<AstVisitStateManager> stateManagers = new LinkedHashSet<AstVisitStateManager>();
}
