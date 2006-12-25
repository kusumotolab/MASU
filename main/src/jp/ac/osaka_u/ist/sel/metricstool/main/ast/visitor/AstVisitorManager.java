package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor;


import jp.ac.osaka_u.ist.sel.metricstool.main.parse.PositionManager;
import antlr.collections.AST;


public interface AstVisitorManager {

    public void visitStart(AST node);

    public void setLineColumnManager(PositionManager lineColumn);
}