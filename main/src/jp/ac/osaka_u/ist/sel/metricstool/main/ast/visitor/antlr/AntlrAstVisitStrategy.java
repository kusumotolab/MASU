package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.antlr;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.VisitControlToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitStrategy;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitor;
import antlr.collections.AST;


/**
 * antlrのAST用のビジターが次にどのノードに進むべきかを誘導する {@link AstVisitStrategy}のデフォルト実装.
 * 
 * @author kou-tngt
 *
 */
public class AntlrAstVisitStrategy implements AstVisitStrategy<AST> {

    /**
     * クラスやメソッド定義を表すノードの内部を訪問させるかどうかを指定するコンストラクタ.
     * @param intoClass クラス定義の内部を訪問させる場合はtrue
     * @param intoMethod　メソッド定義の内部を訪問させる場合はtrue
     */
    public AntlrAstVisitStrategy(final boolean intoClass, final boolean intoMethod) {
        this.intoClass = intoClass;
        this.intoMethod = intoMethod;
    }

    /**
     * ビジターが次に訪問すべきノードを選択し，その選択に応じて {@link AstVisitor#visit(T)}，
     * {@link AstVisitor#enter()}， {@link AstVisitor#exit()}の3つのメソッドを適切に呼び出す.
     * 
     * @param visitor 訪問先を指示する対象のビジター
     * @param node ビジターが現在到達しているノード
     * @param token ビジターが現在到達しているノードの種類を表すトークン
     */
    public void guide(final AstVisitor<AST> visitor, final AST node, final AstToken token) {
        if (this.needToVisitChild(token)) {
            //このノードの内部を訪問する必要があるらしい

            final AST child = node.getFirstChild();

            //トークンの中に入ることを通知（子ノードがを持っていなくてもenteredイベントを発生させる必要がある）
            visitor.enter();

            //ビジターを誘導する.
            visitor.visit(child);

            //このトークンの中から出たことを通知（子ノードがを持っていなくてもexitedイベントを発生させる必要がある）
            visitor.exit();

        }

        //さらに隣のノードがあれば先に誘導する.
        final AST sibiling = node.getNextSibling();
        if (null != sibiling) {
            visitor.visit(sibiling);
        }
    }

    /**
     * 引数のtokenが表す種類のノードについて，その子ノードを訪問する必要があるかどうかを判定する.
     * このメソッドをオーバーライドすることによって，{@link #guide(AstVisitor, AST, AstToken)}メソッドが
     * ビジターを誘導する際に，ノードの内部に誘導するかどうかを制御することができる.
     * 
     * @param token 子ノードを訪問する必要があるかどうかを判定するノードのトークン
     * @return 訪問する必要がある場合はtrue
     */
    protected boolean needToVisitChild(final AstToken token) {
        if (token.isClassDefinition()) {
            return this.intoClass;
        } else if (token.isMethodDefinition()) {
            return this.intoMethod;
        } else if (token.equals(VisitControlToken.SKIP)) {
            return false;
        }
        return true;
    }

    /**
     * クラス内部へ誘導するかどうかを表すフィールド
     */
    private final boolean intoClass;

    /**
     * メソッド内部へ誘導するかどうかを表すフィールド
     */
    private final boolean intoMethod;
}
