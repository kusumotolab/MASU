package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * プリミティブ型を表すためのクラス．プリミティブ型はプログラミング言語によって提唱されている型であるため， ユーザが新たらしい型を作ることができないよう，コンストラクタは private
 * にしている．
 * 
 * @author y-higo
 * 
 */
public final class PrimitiveTypeInfo implements TypeInfo {

    /**
     * boolean を表す定数
     */
    public static final String BOOLEAN_STRING = new String("boolean");

    /**
     * byte を表す定数
     */
    public static final String BYTE_STRING = new String("byte");

    /**
     * char を表す定数
     */
    public static final String CHAR_STRING = new String("char");

    /**
     * short を表す定数
     */
    public static final String SHORT_STRING = new String("short");

    /**
     * int を表す定数
     */
    public static final String INT_STRING = new String("int");

    /**
     * long を表す定数
     */
    public static final String LONG_STRING = new String("long");

    /**
     * float を表す定数
     */
    public static final String FLOAT_STRING = new String("float");

    /**
     * double を表す定数
     */
    public static final String DOUBLE_STRING = new String("double");

    /**
     * 不明な型を表す定数
     */
    public static final String UNKNOWN_STRING = new String("unknown");

    /**
     * boolean 型を表すための定数．
     */
    public static final PrimitiveTypeInfo BOOLEAN = new PrimitiveTypeInfo(BOOLEAN_STRING);

    /**
     * byte 型を表すための定数．
     */
    public static final PrimitiveTypeInfo BYTE = new PrimitiveTypeInfo(BYTE_STRING);

    /**
     * char 型を表すための定数．
     */
    public static final PrimitiveTypeInfo CHAR = new PrimitiveTypeInfo(CHAR_STRING);

    /**
     * short 型を表すための定数．
     */
    public static final PrimitiveTypeInfo SHORT = new PrimitiveTypeInfo(SHORT_STRING);

    /**
     * int 型を表すための定数．
     */
    public static final PrimitiveTypeInfo INT = new PrimitiveTypeInfo(INT_STRING);

    /**
     * long 型を表すための定数．
     */
    public static final PrimitiveTypeInfo LONG = new PrimitiveTypeInfo(LONG_STRING);

    /**
     * float 型を表すための定数．
     */
    public static final PrimitiveTypeInfo FLOAT = new PrimitiveTypeInfo(FLOAT_STRING);

    /**
     * double 型を表すための定数．
     */
    public static final PrimitiveTypeInfo DOUBLE = new PrimitiveTypeInfo(DOUBLE_STRING);

    /**
     * 不明な型を表すための定数．
     */
    public static final PrimitiveTypeInfo UNKNOWN = new PrimitiveTypeInfo(UNKNOWN_STRING);

    /**
     * {@link PrimitiveTypeInfo}のファクトリメソッド．
     * 
     * @param typeName 作成する型の名前
     * @return 指定された方を表す {@link PrimitiveTypeInfo} のインスタンス．
     */
    public static PrimitiveTypeInfo getType(final String typeName) {

        if (null == typeName) {
            throw new NullPointerException();
        }
        
        if (typeName.equals(BOOLEAN_STRING)) {
            return BOOLEAN;
        } else if (typeName.equals(BYTE_STRING)) {
            return BYTE;
        } else if (typeName.equals(CHAR_STRING)) {
            return CHAR;
        } else if (typeName.equals(SHORT_STRING)) {
            return SHORT;
        } else if (typeName.equals(INT_STRING)) {
            return INT;
        } else if (typeName.equals(LONG_STRING)) {
            return LONG;
        } else if (typeName.equals(FLOAT_STRING)) {
            return FLOAT;
        } else if (typeName.equals(DOUBLE_STRING)) {
            return DOUBLE;
        } else {
            assert (false) : "Illegal state: primitive type " + typeName + " is unknown.";
        }

        return UNKNOWN;
    }

    /**
     * この型名を返す．
     */
    public String getName() {
        return this.name;
    }

    /**
     * オブジェクトに型名を与えて初期化する． 型名は固定であるため，外部からはオブジェクトを生成できないようにしている．
     * 
     * @param name 型名
     */
    private PrimitiveTypeInfo(final String name) {
        
        if (null == name) {
            throw new NullPointerException();
        }
        
        this.name = name;
    }

    /**
     * 型名を表す変数．
     */
    private final String name;
}
