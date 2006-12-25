package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;




public class IdentifierToken extends AstTokenAdapter{

    public IdentifierToken(String text) {
        super(text);
    }
    
    public boolean isIdentifier(){
        return true;
    }
}
