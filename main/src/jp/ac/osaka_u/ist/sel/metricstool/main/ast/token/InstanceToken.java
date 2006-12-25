package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;



public class InstanceToken extends AstTokenAdapter{
    
    public static final InstanceToken THIS = new InstanceToken("this");
    public static final InstanceToken NULL = new InstanceToken("null");

    private InstanceToken(String text) {
        super(text);
    }
}
