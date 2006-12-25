package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VoidTypeInfo;

public class PrimitiveTypeToken extends AstTokenAdapter{

    public static final PrimitiveTypeToken BOOLEAN = new PrimitiveTypeToken(PrimitiveTypeInfo.BOOLEAN_STRING);
    public static final PrimitiveTypeToken BYTE = new PrimitiveTypeToken(PrimitiveTypeInfo.BYTE_STRING);
    public static final PrimitiveTypeToken CHAR = new PrimitiveTypeToken(PrimitiveTypeInfo.CHAR_STRING);
    public static final PrimitiveTypeToken SHORT = new PrimitiveTypeToken(PrimitiveTypeInfo.SHORT_STRING);
    public static final PrimitiveTypeToken INT = new PrimitiveTypeToken(PrimitiveTypeInfo.INT_STRING);
    public static final PrimitiveTypeToken LONG = new PrimitiveTypeToken(PrimitiveTypeInfo.LONG_STRING);
    public static final PrimitiveTypeToken FLOAT = new PrimitiveTypeToken(PrimitiveTypeInfo.FLOAT_STRING);
    public static final PrimitiveTypeToken DOUBLE = new PrimitiveTypeToken(PrimitiveTypeInfo.DOUBLE_STRING);
    public static final PrimitiveTypeToken VOID = new PrimitiveTypeToken(VoidTypeInfo.VOID_STRING);
    
    public PrimitiveTypeToken(String text) {
        super(text);
    }
    
    public boolean isPrimitiveType(){
        return true;
    }
}
