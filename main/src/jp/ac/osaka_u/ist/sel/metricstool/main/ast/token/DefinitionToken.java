package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;



public class DefinitionToken extends AstTokenAdapter{

    public static final DefinitionToken CLASS_DEFINITION = new DefinitionToken("CLASS_DEFINITION"){
        public boolean isClassDefinition(){
            return true;
        }
    };
    
    public static final DefinitionToken METHOD_DEFINITION = new DefinitionToken("METHOD_DEFINITION"){
        public boolean isMethodDefinition(){
            return true;
        }
    };
    
    public static final DefinitionToken CONSTRUCTOR_DEFINITION = new DefinitionToken("CONSTRUCTOR_DEFINITION"){
        public boolean isMethodDefinition(){
            return true;
        }
        
        public boolean isConstructorDefinition(){
            return true;
        }
    };
    
    public static final DefinitionToken METHOD_PARAMETER_DEFINITION = new DefinitionToken("METHOD_PARAMETER_DEFINITION"){
        public boolean isMethodParameterDefinition(){
            return true;
        }
    };
    
    public static final DefinitionToken LOCAL_PARAMETER_DEFINITION = new DefinitionToken("LOCAL_PARAMETER_DEFINITION"){
        public boolean isLocalParameterDefinition(){
            return true;
        }
    };
    
    public static final DefinitionToken FIELD_DEFINITION = new DefinitionToken("FIELD_DEFINITION"){
        public boolean isFieldDefinition(){
            return true;
        }
    };
    
    public static final DefinitionToken NAMESPACE_DEFINITION = new DefinitionToken("NAMESPACE_DEFINITION"){
        public boolean isNameSpaceDefinition(){
            return true;
        }
    };
    
    public static final DefinitionToken LOCALVARIABLE_DEFINITION = new DefinitionToken("LOCALVARIABLE_DEFINITION"){
        public boolean isLocalVariableDefinition(){
            return true;
        }
    };
    
    public DefinitionToken(String text) {
        super(text);
    }
}
