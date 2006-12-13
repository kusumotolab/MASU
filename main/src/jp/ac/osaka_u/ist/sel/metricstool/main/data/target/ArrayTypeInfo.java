package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashMap;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 配列型を表すためのクラス．
 * 
 * @author y-higo
 * 
 */
public final class ArrayTypeInfo implements TypeInfo {

    /**
     * 型名を返す
     */
    public String getTypeName() {
        TypeInfo elementType = this.getElementType();
        int dimension = this.getDimension();

        StringBuffer buffer = new StringBuffer();
        buffer.append(elementType.getTypeName());
        for (int i = 0; i < dimension; i++) {
            buffer.append("[]");
        }
        return buffer.toString();
    }

    /**
     * 等しいかどうかのチェックを行う
     */
    public boolean equals(final TypeInfo typeInfo) {

        if (null == typeInfo) {
            throw new NullPointerException();
        }

        if (!(typeInfo instanceof ArrayTypeInfo)) {
            return false;
        }

        TypeInfo elementTypeInfo = this.getElementType();
        TypeInfo correspondElementTypeInfo = ((ArrayTypeInfo) typeInfo).getElementType();
        if (!elementTypeInfo.equals(correspondElementTypeInfo)) {
            return false;
        } else {

            int dimension = this.getDimension();
            int correspondDimension = ((ArrayTypeInfo) typeInfo).getDimension();
            return dimension == correspondDimension;
        }
    }

    /**
     * 配列の要素の型を返す
     * 
     * @return 配列の要素の型
     */
    public TypeInfo getElementType() {
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
     * ArrayTypeInfo のインスタンスを返すためのファクトリメソッド．
     * 
     * @param type 型を表す変数
     * @param dimension 次元を表す変数
     * @return 生成した ArrayTypeInfo オブジェクト
     */
    public static ArrayTypeInfo getType(final TypeInfo type, final int dimension) {

        if (null == type) {
            throw new NullPointerException();
        }
        if (dimension < 1) {
            throw new IllegalArgumentException("Array dimension must be 1 or more!");
        }

        Key key = new Key(type, dimension);
        ArrayTypeInfo arrayType = ARRAY_TYPE_MAP.get(key);
        if (arrayType == null) {
            arrayType = new ArrayTypeInfo(type, dimension);
            ARRAY_TYPE_MAP.put(key, arrayType);
        }

        return arrayType;
    }

    /**
     * オブジェクトの初期化を行う．配列の要素の型と配列の次元が与えられなければならない
     * 
     * @param type 配列の要素の型
     * @param dimension 配列の事件
     */
    private ArrayTypeInfo(final TypeInfo type, final int dimension) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == type) {
            throw new NullPointerException();
        }
        if (1 < dimension) {
            throw new IllegalArgumentException("Array dimension must be 1 or more!");
        }

        this.type = type;
        this.dimension = dimension;
    }

    /**
     * 配列の要素の型を保存する変数
     */
    private final TypeInfo type;

    /**
     * 配列の次元を保存する変数
     */
    private final int dimension;

    /**
     * ArrayTypeInfo オブジェクトを一元管理するための Map．オブジェクトはファクトリメソッドで生成される．
     */
    private static final Map<Key, ArrayTypeInfo> ARRAY_TYPE_MAP = new HashMap<Key, ArrayTypeInfo>();

    /**
     * 変数の型と次元を用いてキーとなるクラス．
     * 
     * @author y-higo
     */
    static class Key {

        /**
         * 第一キー
         */
        private final TypeInfo type;

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
        Key(final TypeInfo type, final int dimension) {

            if (null == type) {
                throw new NullPointerException();
            }
            if (1 < dimension) {
                throw new IllegalArgumentException("Array dimension must be 1 or more!");
            }

            this.type = type;
            this.dimension = dimension;
        }

        /**
         * このオブジェクトのハッシュコードを返す．
         */
        public int hashCode() {
            StringBuffer buffer = new StringBuffer();
            buffer.append(this.type.getTypeName());
            buffer.append(this.dimension);
            String hashString = buffer.toString();
            return hashString.hashCode();
        }

        /**
         * このキーオブジェクトの第一キーを返す．
         * 
         * @return 第一キー
         */
        public String getFirstKey() {
            return this.type.getTypeName();
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

            String firstKey = this.getFirstKey();
            String correspondFirstKey = ((Key) o).getFirstKey();
            if (!firstKey.equals(correspondFirstKey)) {
                return false;
            } else {
                int secondKey = this.getSecondKey();
                int correspondSecondKey = ((Key) o).getSecondKey();
                return secondKey == correspondSecondKey;
            }
        }
    }
}
