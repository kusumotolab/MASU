package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import java.util.EventObject;


public class StateChangeEvent<T> extends EventObject {
    
    public interface StateChangeEventType{}
    
    public StateChangeEvent(StateManager source, StateChangeEventType stateChangeType, T trigger) {
        super(source);
        this.source = source;
        this.stateChangeType = stateChangeType;
        this.trigger = trigger;
    }
    
    public StateManager getSource(){
        return source;
    }
    
    public StateChangeEventType getType(){
        return stateChangeType;
    }
    
    public T getTrigger(){
        return this.trigger;
    }
    
    private final StateManager source;
    
    private final StateChangeEventType stateChangeType;
    
    private final T trigger;
    
}
