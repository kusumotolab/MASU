package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor;

import jp.ac.osaka_u.ist.sel.metricstool.main.parse.PositionManager;

/**
 * 任意の構造のASTを訪問するビジターのインタフェース.
 * 
 * このインタフェースを実装するクラスは，ASTノードへの到達した時，そのノードの内部に入る時，そのノードの内部から出る時に，
 * 登録された {@link AstVisitListener} に対して適切なイベントを発行する.
 * 
 * @author kou-tngt
 *
 * @param <T>　訪問するASTノードの型
 */
public interface AstVisitor<T> {
    public void addVisitListener(AstVisitListener listener);
    public void enter();
    public void exit();
    public boolean isVisited(T node);
    public void removeVisitListener(AstVisitListener listener);
    public void reset();
    public void setPositionManager(PositionManager lineColumn);
    public void visit(T node);
}
