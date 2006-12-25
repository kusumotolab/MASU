package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor;

import jp.ac.osaka_u.ist.sel.metricstool.main.parse.PositionManager;

public interface AstVisitor<T> {
    public void addVisitListener(AstVisitListener listener);
    public void enter();
    public void exit();
    public void reset();
    public boolean isVisited(T node);
    public void visit(T node);
    public void removeVisitListener(AstVisitListener listener);
    public void setLineColumnManager(PositionManager lineColumn);
}
