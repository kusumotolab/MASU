package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.antlr;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstTokenAdapter;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.VisitControlToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitStrategy;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitor;
import antlr.collections.AST;

/**
 * ビジターが次にどのノードに進むべきかを誘導する.
 * 構文木上の何かの要素の内部に入る時はそのことをビルダーにも通知する.
 * @author kou-tngt
 *
 */
public class AntlrAstVisitStrategy implements AstVisitStrategy<AST>{
    public AntlrAstVisitStrategy(boolean intoClass, boolean intoMethod){
        this.intoClass = intoClass;
        this.intoMethod = intoMethod;
    }
    
    public void guide(AstVisitor<AST> visitor, AST prev, AstToken token) {
        if (needToVisitChild(token)){
            //この子ノードに入る必要があるらしい
            
            AST child = prev.getFirstChild();
                
            //トークンの中に入ることを通知
            visitor.enter();
            
            //ビジターを誘導する.
            visitor.visit(child);
            
            //このトークンの中から出たことを通知
            visitor.exit();
            
//        } else {
//            System.out.println("skip: " + prev.getText());
        }
        
        //さらに隣のノードがあれば先に誘導する.
        AST sibiling = prev.getNextSibling();
        if (null != sibiling){
            visitor.visit(sibiling);
        }
    }
    
    protected boolean needToVisitChild(AstToken token){
        if (token.isClassDefinition()){
            return intoClass;
        } else if (token.isMethodDefinition()){
            return intoMethod;
        } else if (token.equals(VisitControlToken.SKIP)){
            return false;
        } else if (token.equals(VisitControlToken.UNKNOWN)){
            return false;
        }
        return true;
    }
    
    private final boolean intoClass;
    private final boolean intoMethod;
}
