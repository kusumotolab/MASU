package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;

public interface AstToken {
    public boolean isAssignmentOperator();
    
    public boolean isArrayDeclarator();
    
    public boolean isClassDefinition();
    
    public boolean isClassBlock();
    
//    public boolean isConstructorCall();
    
    public boolean isConstructorDefinition();
    
    public boolean isExtendDefinition();
    
    public boolean isExpression();
    
    public boolean isFieldDefinition();
    
    public boolean isInstantiation();

    public boolean isMethodDefinition();
    
    public boolean isMethodCall();
    
    public boolean isModifiersDefinition();
    
    public boolean isNameDescription();
    
    public boolean isNameSpaceDefinition();
    
    public boolean isNameUsingDefinition();
    
    public boolean isNameSeparator();
    
    public boolean isBlock();
    
    public boolean isOperator();
    
    public boolean isMethodParameterDefinition();
    
    public boolean isLocalParameterDefinition();
    
    public boolean isPrimitiveType();
    
    public boolean isModifier();
    
    public boolean isAccessModifier();
    
    public boolean isIdentifier();
    
    public boolean isTypeDefinition();
    
    public boolean isLocalVariableDefinition();
    
    public boolean isVoidType();
}
