package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;



/**
 * 型Tで表現されるASTのノードを， {@link AstToken}に翻訳する.
 * 
 * @author kou-tngt
 *
 * @param <T> 翻訳されるASTノードの型
 */
public interface AstTokenTranslator<T> {

    /**
     * 引数nodeが表すASTノードを{@link AstToken}に翻訳する.
     * 
     * @param node 翻訳するASTノード
     * @return 翻訳結果のトークン
     */
    public AstToken translate(T node);

}
