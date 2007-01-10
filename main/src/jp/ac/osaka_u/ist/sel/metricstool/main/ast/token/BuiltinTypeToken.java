package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VoidTypeInfo;


/**
 * 組み込み型を表すトークンクラス.
 * 
 * @author kou-tngt
 *
 */
public class BuiltinTypeToken extends AstTokenAdapter {

    /**
     * bool型を表す定数インスタンス.
     */
    public static final BuiltinTypeToken BOOLEAN = new BuiltinTypeToken(
            PrimitiveTypeInfo.BOOLEAN_STRING);

    /**
     * byte型を表す定数インスタンス.
     */
    public static final BuiltinTypeToken BYTE = new BuiltinTypeToken(PrimitiveTypeInfo.BYTE_STRING);

    /**
     * char型を表す定数インスタンス.
     */
    public static final BuiltinTypeToken CHAR = new BuiltinTypeToken(PrimitiveTypeInfo.CHAR_STRING);

    /**
     * short型を表す定数インスタンス.
     */
    public static final BuiltinTypeToken SHORT = new BuiltinTypeToken(
            PrimitiveTypeInfo.SHORT_STRING);

    /**
     * int型を表す定数インスタンス.
     */
    public static final BuiltinTypeToken INT = new BuiltinTypeToken(PrimitiveTypeInfo.INT_STRING);

    /**
     * long型を表す定数インスタンス.
     */
    public static final BuiltinTypeToken LONG = new BuiltinTypeToken(PrimitiveTypeInfo.LONG_STRING);

    /**
     * float型を表す定数インスタンス.
     */
    public static final BuiltinTypeToken FLOAT = new BuiltinTypeToken(
            PrimitiveTypeInfo.FLOAT_STRING);

    /**
     * double型を表す定数インスタンス.
     */
    public static final BuiltinTypeToken DOUBLE = new BuiltinTypeToken(
            PrimitiveTypeInfo.DOUBLE_STRING);

    /**
     * void型を表す定数インスタンス.
     */
    public static final BuiltinTypeToken VOID = new BuiltinTypeToken(VoidTypeInfo.VOID_STRING);

    /**
     * 指定された文字列で表される基本型を表すトークンを作成するコンストラクタ.
     * 
     * @param text　この組み込み型を表す文字列
     */
    protected BuiltinTypeToken(String text) {
        super(text);
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstTokenAdapter#isPrimitiveType()
     */
    public boolean isPrimitiveType() {
        return !isVoidType();
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstTokenAdapter#isVoidType()
     */
    public boolean isVoidType() {
        return this.equals(VOID);
    }

}
