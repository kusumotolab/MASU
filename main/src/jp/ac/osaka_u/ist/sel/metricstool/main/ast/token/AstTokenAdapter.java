package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;





public class AstTokenAdapter implements AstToken{
    
    public AstTokenAdapter(String text){
        this.text = text;
    }
    
    public boolean isAccessModifier() {
        return false;
    }
    
    public boolean isArrayDeclarator() {
        return false;
    }

    public boolean isAssignmentOperator() {
        return false;
    }
    
    public boolean isBlock() {
        return false;
    }

    public boolean isClassBlock() {
        return false;
    }
    
    public boolean isClassDefinition() {
        return false;
    }
    
    public boolean isConstructorDefinition() {
        return false;
    }
    
    public boolean isExpression() {
        return false;
    }
    
    public boolean isInheritanceDescription(){
        return false;
    }
    
    public boolean isFieldDefinition(){
        return false;
    }
    
    public boolean isIdentifier() {
        return false;
    }

    public boolean isInstantiation() {
        return false;
    }

    public boolean isLocalParameterDefinition() {
        return false;
    }
    
    public boolean isLocalVariableDefinition(){
        return false;
    }

    public boolean isMethodCall() {
        return false;
    }
    
    public boolean isMethodDefinition() {
        return false;
    }

    public boolean isMethodParameterDefinition() {
        return false;
    }

    public boolean isModifier() {
        return isAccessModifier();
    }
    
    public boolean isModifiersDefinition() {
        return false;
    }
    
    public boolean isNameDescription() {
        return false;
    }

    public boolean isNameSeparator() {
        return false;
    }

    public boolean isNameSpaceDefinition() {
        return false;
    }
    
    public boolean isNameUsingDefinition() {
        return false;
    }
    
    public boolean isOperator(){
        return false;
    }
    
    public boolean isPrimitiveType() {
        return false;
    }
    
    public boolean isTypeDescription() {
        return false;
    }
    
    public boolean isVoidType() {
        return false;
    }
    
    public String toString(){
        return text;
    }
    
    private final String text;

}
