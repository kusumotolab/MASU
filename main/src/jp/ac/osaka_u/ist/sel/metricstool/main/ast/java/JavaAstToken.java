package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstTokenAdapter;

public class JavaAstToken extends AstTokenAdapter{
    
    public static final JavaAstToken IMPORT = new JavaAstToken("import");
    
    public static final JavaAstToken STAR = new JavaAstToken("*");
    
    public static final JavaAstToken ENUM_CONSTANT = new JavaAstToken("ENUM_CONST");
    
    public static final JavaAstToken SUPER = new JavaAstToken("SUPER");
    
    public static final JavaAstToken ARRAY_INIT = new JavaAstToken("ARRAY_INIT");
    
    public static final JavaAstToken CONSTRUCTOR_CALL = new JavaAstToken("CONSTRUCTOR_CALL");
    
    public static final JavaAstToken SUPER_CONSTRUCTOR_CALL = new JavaAstToken("SUPER_CONSTRUCTOR_CALL");
    
    public static final JavaAstToken CLASS = new JavaAstToken("CLASS");
    
    public JavaAstToken(String text) {
        super(text);
    }

}
