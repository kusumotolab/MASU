package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


/**
 * void 型を表すクラス．
 * 
 * @author y-higo
 * 
 */
public final class NullTypeInfo implements TypeInfo, UnresolvedTypeInfo {

    /**
     * このクラスの単一オブジェクトを返す
     * 
     * @return このクラスの単一オブジェクト
     */
    public static NullTypeInfo getInstance() {
        return SINGLETON;
    }

    /**
     * void 型の名前を返す．
     */
    public String getTypeName() {
        return this.name;
    }

    /**
     * 等しいかどうかのチェックを行う
     */
    public boolean equals(final TypeInfo typeInfo) {

        if (null == typeInfo) {
            throw new NullPointerException();
        }

        return typeInfo instanceof NullTypeInfo;
    }

    /**
     * null 型の型名を表す定数
     */
    public static final String NULL_STRING = new String("null");

    /**
     * 引数なしコンストラクタ
     */
    private NullTypeInfo() {
        this.name = NULL_STRING;
    }

    /**
     * このクラスの単一オブジェクトを保存するための定数
     */
    private static final NullTypeInfo SINGLETON = new NullTypeInfo();

    /**
     * この型の名前を保存するための変数
     */
    private final String name;
}
