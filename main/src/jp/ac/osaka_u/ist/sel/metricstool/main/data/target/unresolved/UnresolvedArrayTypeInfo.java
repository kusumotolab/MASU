package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.HashMap;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決配列型を表すためのクラス．以下の情報を持つ．
 * <ul>
 * <li>未解決型 (UnresolvedTypeInfo)</li>
 * <li>次元 (int)</li>
 * </ul>
 * 
 * @author y-higo
 * @see UnresolvedTypeInfo
 */
public final class UnresolvedArrayTypeInfo implements UnresolvedTypeInfo {

    /**
     * 型名を返す
     */
    public String getTypeName() {
        final UnresolvedTypeInfo elementType = this.getElementType();
        final int dimension = this.getDimension();

        final StringBuffer buffer = new StringBuffer();
        buffer.append(elementType.getTypeName());
        for (int i = 0; i < dimension; i++) {
            buffer.append("[]");
        }
        return buffer.toString();
    }

    /**
     * 等しいかどうかのチェックを行う
     */
    public boolean equals(final UnresolvedTypeInfo typeInfo) {

        if (null == typeInfo) {
            throw new NullPointerException();
        }

        if (!(typeInfo instanceof UnresolvedArrayTypeInfo)) {
            return false;
        }

        final UnresolvedTypeInfo elementTypeInfo = this.getElementType();
        final UnresolvedTypeInfo correspondElementTypeInfo = ((UnresolvedArrayTypeInfo) typeInfo)
                .getElementType();
        if (!elementTypeInfo.equals(correspondElementTypeInfo)) {
            return false;
        } else {

            final int dimension = this.getDimension();
            final int correspondDimension = ((UnresolvedArrayTypeInfo) typeInfo).getDimension();
            return dimension == correspondDimension;
        }
    }

    /**
     * 配列の要素の未解決型を返す
     * 
     * @return 配列の要素の未解決型
     */
    public UnresolvedTypeInfo getElementType() {
        return this.type;
    }

    /**
     * 配列の次元を返す
     * 
     * @return 配列の次元
     */
    public int getDimension() {
        return this.dimension;
    }

    /**
     * このインスタンスが表す配列の次元を1大きくした配列を表すインスタンスを返す．
     * @return このインスタンスが表す配列の次元を1大きくした配列
     */
    public UnresolvedArrayTypeInfo getDimensionInclementedArrayType() {
        return getType(getElementType(), getDimension() + 1);
    }

    /**
     * UnresolvedArrayTypeInfo のインスタンスを返すためのファクトリメソッド．
     * 
     * @param type 未解決型を表す変数
     * @param dimension 次元を表す変数
     * @return 生成した UnresolvedArrayTypeInfo オブジェクト
     */
    public static UnresolvedArrayTypeInfo getType(final UnresolvedTypeInfo type, final int dimension) {

        if (null == type) {
            throw new NullPointerException();
        }
        if (dimension < 1) {
            throw new IllegalArgumentException("Array dimension must be 1 or more!");
        }

        final Key key = new Key(type, dimension);
        UnresolvedArrayTypeInfo arrayType = ARRAY_TYPE_MAP.get(key);
        if (arrayType == null) {
            arrayType = new UnresolvedArrayTypeInfo(type, dimension);
            ARRAY_TYPE_MAP.put(key, arrayType);
        }

        return arrayType;
    }

    /**
     * 未解決配列型オブジェクトの初期化を行う．配列の要素の未解決型と配列の次元が与えられなければならない
     * 
     * @param type 配列の要素の未解決型
     * @param dimension 配列の次元
     */
    private UnresolvedArrayTypeInfo(final UnresolvedTypeInfo type, final int dimension) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == type) {
            throw new NullPointerException();
        }
        if (1 > dimension) {
            throw new IllegalArgumentException("Array dimension must be 1 or more!");
        }

        this.type = type;
        this.dimension = dimension;
    }

    /**
     * 配列の要素の型を保存する変数
     */
    private final UnresolvedTypeInfo type;

    /**
     * 配列の次元を保存する変数
     */
    private final int dimension;

    /**
     * UnresolvedArrayTypeInfo オブジェクトを一元管理するための Map．オブジェクトはファクトリメソッドで生成される．
     */
    private static final Map<Key, UnresolvedArrayTypeInfo> ARRAY_TYPE_MAP = new HashMap<Key, UnresolvedArrayTypeInfo>();

    /**
     * 変数の型と次元を用いてキーとなるクラス．
     * 
     * @author y-higo
     */
    static class Key {

        /**
         * 第一キー
         */
        private final UnresolvedTypeInfo type;

        /**
         * 第二キー
         */
        private final int dimension;

        /**
         * 第一，第二キーから，キーオブジェクトを生成する
         * 
         * @param type 第一キー
         * @param dimension 第二キー
         */
        Key(final UnresolvedTypeInfo type, final int dimension) {

            if (null == type) {
                throw new NullPointerException();
            }
            if (1 > dimension) {
                throw new IllegalArgumentException("Array dimension must be 1 or more!");
            }

            this.type = type;
            this.dimension = dimension;
        }

        /**
         * このオブジェクトのハッシュコードを返す．
         */
        public int hashCode() {
            final StringBuffer buffer = new StringBuffer();
            buffer.append(this.type.getTypeName());
            buffer.append(this.dimension);
            final String hashString = buffer.toString();
            return hashString.hashCode();
        }

        /**
         * このキーオブジェクトの第一キーを返す．
         * 
         * @return 第一キー
         */
        public UnresolvedTypeInfo getFirstKey() {
            return this.type;
        }

        /**
         * このキーオブジェクトの第二キーを返す．
         * 
         * @return 第二キー
         */
        public int getSecondKey() {
            return this.dimension;
        }

        /**
         * このオブジェクトと引数で指定されたオブジェクトが等しいかを返す．
         */
        public boolean equals(Object o) {

            if (null == o) {
                throw new NullPointerException();
            }

            final UnresolvedTypeInfo firstKey = this.getFirstKey();
            final UnresolvedTypeInfo correspondFirstKey = ((Key) o).getFirstKey();
            if (!firstKey.equals(correspondFirstKey)) {
                return false;
            } else {
                final int secondKey = this.getSecondKey();
                final int correspondSecondKey = ((Key) o).getSecondKey();
                return secondKey == correspondSecondKey;
            }
        }
    }
}
