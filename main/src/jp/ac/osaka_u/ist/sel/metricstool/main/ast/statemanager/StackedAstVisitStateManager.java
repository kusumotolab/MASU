package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * AstVisitStateManager の基本となる抽象クラス.
 * <p>
 * 状態変化のトリガとなるトークンに入る時にスタックに現在の状態を記録しておき，
 * トリガとなるトークンから出た時にスタックから過去の状態を取り出して状態を復元する仕組みを提供する.
 * 型パラメータTに任意の型を入れることで,状態復元時に渡される情報の型を指定することができる.
 * 
 * <p>
 * このクラスを継承するクラスは {@link #isStateChangeTriggerToken(AstToken)},{@link #getState()},
 * {@link #setState(T)}の3つの抽象メソッドを実装する必要がある.
 * <p>
 * 
 * @author kou-tngt
 *
 * @param <T> 状態として記録する情報の型.
 */
public abstract class StackedAstVisitStateManager<T> implements AstVisitStateManager {
    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateManager#addStateChangeListener(jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeListener)
     */
    public void addStateChangeListener(final StateChangeListener<AstVisitEvent> listener) {
        this.listeners.add(listener);
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.AstVisitListener#entered(jp.ac.osaka_u.ist.sel.metricstool.main.ast.AstVisitEvent)
     */
    public void entered(final AstVisitEvent event) {
        final AstToken token = event.getToken();
        if (this.isStateChangeTriggerToken(token)) {
            this.pushState();
        }
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.AstVisitListener#exited(jp.ac.osaka_u.ist.sel.metricstool.main.ast.AstVisitEvent)
     */
    public void exited(final AstVisitEvent event) {
        final AstToken token = event.getToken();
        if (this.isStateChangeTriggerToken(token)) {
            this.popState();
        }
    }

    /**
     * @return
     */
    public Set<StateChangeListener<AstVisitEvent>> getListeners() {
        return Collections.unmodifiableSet(this.listeners);
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateManager#removeStateChangeListener(jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeListener)
     */
    public void removeStateChangeListener(final StateChangeListener<AstVisitEvent> listener) {
        this.listeners.remove(listener);
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.AstVisitListener#visited(jp.ac.osaka_u.ist.sel.metricstool.main.ast.AstVisitEvent)
     */
    public final void visited(final AstVisitEvent event) {
        //何もしない
    }
    
    /**
     * @param type
     */
    protected final  void fireStateChangeEvent(StateChangeEventType type, AstVisitEvent triggerEvent){
        StateChangeEvent<AstVisitEvent> event = new StateChangeEvent<AstVisitEvent>(this,type,triggerEvent);
        for(StateChangeListener<AstVisitEvent> listener : getListeners()){
            listener.stateChangend(event);
        }
    }

    /**
     * 状態をスタックから取り出して復元する.
     */
    private void popState() {
        this.setState(this.stateStack.pop());
    }

    /**
     * 現在の状態をスタックに入れる.
     */
    private void pushState() {
        this.stateStack.push(this.getState());
    }

    protected abstract T getState();

    protected abstract void setState(T state);

    protected abstract boolean isStateChangeTriggerToken(AstToken token);
    
    private final Set<StateChangeListener<AstVisitEvent>> listeners = new LinkedHashSet<StateChangeListener<AstVisitEvent>>();

    private final Stack<T> stateStack = new Stack<T>();
}
