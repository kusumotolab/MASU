package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import java.util.EventListener;


public interface StateChangeListener<T> extends EventListener {
    public void stateChangend(StateChangeEvent<T> event);
}
