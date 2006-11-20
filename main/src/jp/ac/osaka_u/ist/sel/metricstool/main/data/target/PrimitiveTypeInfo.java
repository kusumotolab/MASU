package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * プリミティブ型を表すためのクラス．プリミティブ型はプログラミング言語によって提唱されている型であるため， ユーザが新たらしい型を作ることができないよう，コンストラクタは private
 * にしている．
 * 
 * @author y-higo
 * 
 */
public class PrimitiveTypeInfo implements TypeInfo {

    /**
     * boolean 型を表すための定数．
     */
    public static final PrimitiveTypeInfo BOOLEAN = new PrimitiveTypeInfo("boolean");

    /**
     * byte 型を表すための定数．
     */
    public static final PrimitiveTypeInfo BYTE = new PrimitiveTypeInfo("byte");

    /**
     * short 型を表すための定数．
     */
    public static final PrimitiveTypeInfo SHORT = new PrimitiveTypeInfo("short");

    /**
     * long 型を表すための定数．
     */
    public static final PrimitiveTypeInfo INT = new PrimitiveTypeInfo("int");

    /**
     * long 型を表すための定数．
     */
    public static final PrimitiveTypeInfo LONG = new PrimitiveTypeInfo("long");

    /**
     * float 型を表すための定数．
     */
    public static final PrimitiveTypeInfo FLOAT = new PrimitiveTypeInfo("float");

    /**
     * double 型を表すための定数．
     */
    public static final PrimitiveTypeInfo DOUBLE = new PrimitiveTypeInfo("double");

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
    private PrimitiveTypeInfo(String name) {
        this.name = name;
    }

    /**
     * 型名を表す変数．
     */
    private final String name;
}
