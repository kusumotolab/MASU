package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


/**
 * 状態を管理するものを表すインタフェース.
 * 
 * @author kou-tngt
 *
 */
public interface StateManager<T> {
    /**
     * 状態変化を通知するリスナを追加する
     * @param listener
     */
    public void addStateChangeListener(StateChangeListener<T> listener);

    /**状態変化を通知するリスナを削除する
     * @param listener
     */
    public void removeStateChangeListener(StateChangeListener<T> listener);
}
