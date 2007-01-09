package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;



public class DescriptionToken extends AstTokenAdapter{

    public static final DescriptionToken TYPE = new DescriptionToken("TYPE"){
        public boolean isTypeDescription() {
            return true;
        }
    };
    
    public static final DescriptionToken INHERITANCE = new DescriptionToken("INHERITANCE"){
        public boolean isInheritanceDescription(){
            return true;
        }
    };
    
    public static final DescriptionToken USING = new DescriptionToken("USING"){
        public boolean isNameUsingDefinition(){
            return true;
        }
    };
    
    public static final DescriptionToken MODIFIER = new DescriptionToken("MODIFIER"){
        public boolean isModifiersDefinition(){
            return true;
        }
    };
    
    public static final DescriptionToken EXPRESSION = new DescriptionToken("EXPRESSION"){
        public boolean isExpression(){
            return true;
        }
    };
    
    public static final DescriptionToken NAME = new DescriptionToken("NAME"){
        public boolean isNameDescription(){
            return true;
        }
    };
    
    public DescriptionToken(String text) {
        super(text);
    }

    

}
