package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VoidTypeInfo;

public class BuiltinTypeToken extends AstTokenAdapter{

    public static final BuiltinTypeToken BOOLEAN = new BuiltinTypeToken(PrimitiveTypeInfo.BOOLEAN_STRING);
    public static final BuiltinTypeToken BYTE = new BuiltinTypeToken(PrimitiveTypeInfo.BYTE_STRING);
    public static final BuiltinTypeToken CHAR = new BuiltinTypeToken(PrimitiveTypeInfo.CHAR_STRING);
    public static final BuiltinTypeToken SHORT = new BuiltinTypeToken(PrimitiveTypeInfo.SHORT_STRING);
    public static final BuiltinTypeToken INT = new BuiltinTypeToken(PrimitiveTypeInfo.INT_STRING);
    public static final BuiltinTypeToken LONG = new BuiltinTypeToken(PrimitiveTypeInfo.LONG_STRING);
    public static final BuiltinTypeToken FLOAT = new BuiltinTypeToken(PrimitiveTypeInfo.FLOAT_STRING);
    public static final BuiltinTypeToken DOUBLE = new BuiltinTypeToken(PrimitiveTypeInfo.DOUBLE_STRING);
    
    public static final BuiltinTypeToken VOID = new BuiltinTypeToken(VoidTypeInfo.VOID_STRING,true);
    
    public BuiltinTypeToken(String text) {
        this(text,false);
    }
    
    public BuiltinTypeToken(String text, boolean voidType){
        super(text);
        this.voidType = voidType;
    }
    
    public boolean isPrimitiveType(){
        return !isVoidType();
    }
    
    public boolean isVoidType(){
        return voidType;
    }
    
    private final boolean voidType;
}
