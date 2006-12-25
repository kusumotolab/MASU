package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


public class AccessModifierToken extends ModifierToken{
    
    public AccessModifierToken(String text, boolean publicVisibility, boolean nameSpaceVisibility,boolean inheritanceVisibility) {
        super(text);
        this.publicVisibility = publicVisibility;
        this.nameSpaceVisibility = nameSpaceVisibility;
        this.inheritanceVisibility = inheritanceVisibility;
    }
    
    public boolean isAccessModifier(){
        return true;
    }
    
    public boolean isPublicVisibility(){
        return publicVisibility;
    }
    
    public boolean isNameSpaceVisibility(){
        return nameSpaceVisibility;
    }
    
    public boolean isInheritanceVisibility(){
        return inheritanceVisibility;
    }
    
    private final boolean publicVisibility;
    private final boolean nameSpaceVisibility;
    private final boolean inheritanceVisibility;
}
