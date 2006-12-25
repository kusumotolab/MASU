package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;


public interface AstVisitStrategy<T> {
    public void guide(AstVisitor<T> visitor, T node, AstToken info);
}
