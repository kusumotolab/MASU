package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;



public class ModifierToken extends AstTokenAdapter{
    public static final ModifierToken STATIC = new ModifierToken("static");
    
    public ModifierToken(String text) {
        super(text);
    }
    
    public boolean isModifier(){
        return true;
    }
}
