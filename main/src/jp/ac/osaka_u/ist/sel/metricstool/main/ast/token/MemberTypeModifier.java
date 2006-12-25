package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


public class MemberTypeModifier extends ModifierToken{

    public MemberTypeModifier(String text) {
        this(text,false);
    }
    
    public MemberTypeModifier(String text,boolean staticMember){
        super(text);
        this.staticMember = staticMember;
    }
    
    public boolean isStaticMember(){
        return staticMember;
    }
    
    private final boolean staticMember;
}
