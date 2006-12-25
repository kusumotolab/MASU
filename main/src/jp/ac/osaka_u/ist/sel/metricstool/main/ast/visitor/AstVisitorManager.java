package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor;


import jp.ac.osaka_u.ist.sel.metricstool.main.parse.PositionManager;


public interface AstVisitorManager<T> {

    public void visitStart(T node);

    public void setPositionManager(PositionManager lineColumn);
}