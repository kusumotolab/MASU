package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor;


import jp.ac.osaka_u.ist.sel.metricstool.main.parse.PositionManager;


/**
 * 任意の構造のASTを訪問するビジターのインタフェース.
 * <p>
 * このインタフェースを実装するクラスは，ASTノードを順番に訪問し，
 * 各ノードに到達した時，そのノードの内部に入る時，そのノードの内部から出る時に，
 * 登録された {@link AstVisitListener} に対して適切なイベントを発行する.
 * 
 * @author kou-tngt
 *
 * @param <T>　訪問するASTノードの型
 */
public interface AstVisitor<T> {

    /**
     * このビジターが発行する各 {@link AstVisitEvent} の通知を受けるリスナを登録する.
     * 
     * @param listener 登録するリスナ
     * @throws NullPointerException listenerがnullの場合
     */
    public void addVisitListener(AstVisitListener listener);

    /**
     * 直前に {@link #visit(T)} メソッドによって到達したノードの中に入る.
     */
    public void enter();

    /**
     * 現在のノードの中から外に出る.
     */
    public void exit();

    /**
     * 引数で与えられたノードに既に到達済みかどうかを返す.
     * 
     * @param node　到達済みかどうかを判定したいノード
     * @return　到達済みであればtrue, そうでなかればfalse.
     */
    public boolean isVisited(T node);

    /**
     * このビジターが発行する各 {@link AstVisitEvent} の通知を受けるリスナを削除する.
     * 
     * @param listener　削除するリスナ
     * @throws NullPointerException listenerがnullの場合
     */
    public void removeVisitListener(AstVisitListener listener);

    /**
     * このビジターの状態を初期状態に戻す.
     * イベントリスナは削除されない.
     */
    public void reset();

    /**
     * ASTノードのソースコード上での位置情報を管理する {@link PositionManager} をセットする.
     * 
     * @param position　ASTノードのソースコード上での位置情報を管理する {@link PositionManager}
     */
    public void setPositionManager(PositionManager position);

    /**
     * 引数で与えられたノードを訪問する.
     * 
     * @param node 訪問するノード.
     */
    public void visit(T node);
}
