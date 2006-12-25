package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;



public interface AstTokenTranslator<T> {

    public AstToken translate(T node);

}
