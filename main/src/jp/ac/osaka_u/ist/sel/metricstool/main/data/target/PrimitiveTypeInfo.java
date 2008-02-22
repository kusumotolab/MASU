package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


/**
 * プリミティブ型を表すためのクラス．プリミティブ型はプログラミング言語によって提唱されている型であるため， ユーザが新たらしい型を作ることができないよう，コンストラクタは private
 * にしている．
 * 
 * @author higo
 * 
 */
public final class PrimitiveTypeInfo implements TypeInfo, UnresolvedTypeInfo {

    /**
     * プリミティブ型の各要素を表すための列挙型
     * 
     * @author higo
     * 
     */
    public enum TYPE {

        BOOLEAN {

            @Override
            public String getName() {
                return "boolean";
            }
        },

        BYTE {
            @Override
            public String getName() {
                return "byte";
            }
        },

        CHAR {
            @Override
            public String getName() {
                return "char";
            }
        },

        SHORT {
            @Override
            public String getName() {
                return "short";
            }
        },

        INT {
            @Override
            public String getName() {
                return "int";
            }
        },

        LONG {
            @Override
            public String getName() {
                return "long";
            }
        },

        FLOAT {
            @Override
            public String getName() {
                return "float";
            }
        },

        DOUBLE {
            @Override
            public String getName() {
                return "double";
            }
        },

        STRING {
            @Override
            public String getName() {
                return "string";
            }
        };

        public abstract String getName();
    }

    /**
     * boolean を表す定数
     */
    public static final String BOOLEAN_STRING = TYPE.BOOLEAN.getName();

    /**
     * byte を表す定数
     */
    public static final String BYTE_STRING = TYPE.BYTE.getName();

    /**
     * char を表す定数
     */
    public static final String CHAR_STRING = TYPE.CHAR.getName();

    /**
     * short を表す定数
     */
    public static final String SHORT_STRING = TYPE.SHORT.getName();

    /**
     * int を表す定数
     */
    public static final String INT_STRING = TYPE.INT.getName();

    /**
     * long を表す定数
     */
    public static final String LONG_STRING = TYPE.LONG.getName();

    /**
     * float を表す定数
     */
    public static final String FLOAT_STRING = TYPE.FLOAT.getName();

    /**
     * double を表す定数
     */
    public static final String DOUBLE_STRING = TYPE.DOUBLE.getName();

    /**
     * string を表す定数
     */
    public static final String STRING_STRING = TYPE.STRING.getName();

    /**
     * boolean 型を表すための定数．
     */
    public static final PrimitiveTypeInfo BOOLEAN = new PrimitiveTypeInfo(TYPE.BOOLEAN);

    /**
     * byte 型を表すための定数．
     */
    public static final PrimitiveTypeInfo BYTE = new PrimitiveTypeInfo(TYPE.BYTE);

    /**
     * char 型を表すための定数．
     */
    public static final PrimitiveTypeInfo CHAR = new PrimitiveTypeInfo(TYPE.CHAR);

    /**
     * short 型を表すための定数．
     */
    public static final PrimitiveTypeInfo SHORT = new PrimitiveTypeInfo(TYPE.SHORT);

    /**
     * int 型を表すための定数．
     */
    public static final PrimitiveTypeInfo INT = new PrimitiveTypeInfo(TYPE.INT);

    /**
     * long 型を表すための定数．
     */
    public static final PrimitiveTypeInfo LONG = new PrimitiveTypeInfo(TYPE.LONG);

    /**
     * float 型を表すための定数．
     */
    public static final PrimitiveTypeInfo FLOAT = new PrimitiveTypeInfo(TYPE.FLOAT);

    /**
     * double 型を表すための定数．
     */
    public static final PrimitiveTypeInfo DOUBLE = new PrimitiveTypeInfo(TYPE.DOUBLE);

    /**
     * string 型を表すための定数
     */
    public static final PrimitiveTypeInfo STRING = new PrimitiveTypeInfo(TYPE.STRING);

    /**
     * {@link PrimitiveTypeInfo}のファクトリメソッド．
     * 
     * @param type 作成する型の列挙型
     * @return 指定された方を表す {@link PrimitiveTypeInfo} のインスタンス．
     */
    public static PrimitiveTypeInfo getType(final TYPE type) {

        if (null == type) {
            throw new NullPointerException();
        }

        switch (type) {
        case BOOLEAN:
            return BOOLEAN;
        case BYTE:
            return BYTE;
        case CHAR:
            return CHAR;
        case DOUBLE:
            return DOUBLE;
        case FLOAT:
            return FLOAT;
        case INT:
            return INT;
        case LONG:
            return LONG;
        case SHORT:
            return SHORT;
        case STRING:
            return STRING;
        default:
            throw new IllegalArgumentException();
        }
    }

    /**
     * 既に解決済みかどうかを返す
     * 
     * @return 常に true が返される
     */
    public boolean alreadyResolved() {
        return true;
    }

    /**
     * 型解決された情報を返す
     * 
     * @return 自分自身を返す
     */
    public TypeInfo getResolvedType() {
        return this;
    }

    /**
     * 名前解決を行う
     * 
     * @param usingClass 名前解決を行うエンティティがあるクラス
     * @param usingMethod 名前解決を行うエンティティがあるメソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * 
     * @return 解決済みの型（自分自身）
     */
    public TypeInfo resolveType(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {
        return this;
    }

    /**
     * この型のプリミティブ型を返す
     * 
     * @return この型のプリミティブ型
     */
    public TYPE getPrimitiveType() {
        return this.type;
    }

    /**
     * 型名を返す．
     * 
     * @return 型名
     */
    public String getTypeName() {
        return this.type.getName();
    }

    /**
     * オブジェクトの等価性のチェックを行う
     */
    public boolean equals(final TypeInfo typeInfo) {

        if (null == typeInfo) {
            throw new NullPointerException();
        }

        if (!(typeInfo instanceof PrimitiveTypeInfo)) {
            return false;
        }

        return this.getTypeName().equals(typeInfo.getTypeName());
    }

    /**
     * オブジェクトに型を与えて初期化する． 型名は固定であるため，外部からはオブジェクトを生成できないようにしている．
     * 
     * @param type 型
     */
    private PrimitiveTypeInfo(final TYPE type) {

        if (null == type) {
            throw new NullPointerException();
        }

        this.type = type;
    }

    /**
     * 型を表す変数．
     */
    private final TYPE type;
}
