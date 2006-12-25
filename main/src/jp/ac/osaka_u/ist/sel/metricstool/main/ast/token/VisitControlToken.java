package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;



public class VisitControlToken extends AstTokenAdapter{

    public static final VisitControlToken SKIP = new VisitControlToken("SKIP");
    public static final VisitControlToken ENTER = new VisitControlToken("ENTER");
    public static final VisitControlToken UNKNOWN = new VisitControlToken("UNKNOWN");
    
    private VisitControlToken(String text) {
        super(text);
    }
    
}
