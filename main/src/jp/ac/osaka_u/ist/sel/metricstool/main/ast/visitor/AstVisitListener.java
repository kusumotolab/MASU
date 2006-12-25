package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor;


import java.util.EventListener;


/**
 * 抽象構文木のビジターが通知するイベントを受け取るインタフェース.
 * 
 * 任意のノードaについて,{@link #visited(AstVisitEvent)},{@link #entered(AstVisitEvent)},{@link #exited(AstVisitEvent)}
 * の順番に通知される.
 * 
 * @author kou-tngt
 *
 */
public interface AstVisitListener extends EventListener {
    /**
     * ある頂点に到達したイベントを受け取る.
     * @param e 到達イベント
     */
    public void visited(AstVisitEvent e);

    /**
     * ある頂点の中に入るイベントを受け取る.
     * @param e
     */
    public void entered(AstVisitEvent e);

    /**
     * ある頂点の外に出たイベントを受け取る
     * @param e
     */
    public void exited(AstVisitEvent e);
}
