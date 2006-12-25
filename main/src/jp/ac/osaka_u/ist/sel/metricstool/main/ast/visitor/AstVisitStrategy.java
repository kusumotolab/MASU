package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;


/**
 * {@link AstVisitor} がどのようにASTのノードを訪問するかを制御するインタフェース.
 * 
 * @author kou-tngt
 *
 * @param <T> ビジターが訪問するASTの各ノードの型
 */
public interface AstVisitStrategy<T> {

    /**
     * ビジターが次に訪問すべきノードを選択し，その選択に応じて {@link AstVisitor#visit(T)}，
     * {@link AstVisitor#enter()}， {@link AstVisitor#exit()}の3つのメソッドを適切に呼び出す.
     * 
     * @param visitor 訪問先を指示する対象のビジター
     * @param node ビジターが現在到達しているノード
     * @param token ビジターが現在到達しているノードの種類を表すトークン
     */
    public void guide(AstVisitor<T> visitor, T node, AstToken token);
}
