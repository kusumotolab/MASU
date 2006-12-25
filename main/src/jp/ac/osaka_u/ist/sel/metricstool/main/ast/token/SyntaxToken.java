package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;




public class SyntaxToken extends AstTokenAdapter{

    public static final SyntaxToken NAME_SEPARATOR = new SyntaxToken("NAME_SEPARATOR"){
        public boolean isNameSeparator(){
            return true;
        }
    };
    
    public static final SyntaxToken BLOCK_START = new SyntaxToken("BLOCK_START"){
        public boolean isBlock(){
            return true;
        }
    };
    
    public static final SyntaxToken CLASSBLOCK_START = new SyntaxToken("CLASSBLOCK_START"){
        public boolean isBlock(){
            return true;
        }
        public boolean isClassBlock(){
            return true;
        }
    };
    
    public static final SyntaxToken METHOD_CALL = new SyntaxToken("METHOD_CALL"){
        public boolean isMethodCall(){
            return true;
        }
    };
    
    public static final SyntaxToken NEW = new SyntaxToken("NEW"){
        public boolean isInstantiation() {
            return true;
        }
    };
    
    public static final SyntaxToken ARRAY = new SyntaxToken("ARRAY"){
        public boolean isArrayDeclarator(){
            return true;
        }
    };
    
    public SyntaxToken(String text) {
        super(text);
    }
}
