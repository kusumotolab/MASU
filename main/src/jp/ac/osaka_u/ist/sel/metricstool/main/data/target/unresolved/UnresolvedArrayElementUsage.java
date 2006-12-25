package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


/**
 * 未解決配列に対する要素の参照を表すためのクラス．以下の情報を持つ．
 * 
 * @author kou-tngt
 * @see UnresolvedTypeInfo
 */
public class UnresolvedArrayElementUsage implements UnresolvedTypeInfo {

    /**
     * 要素が参照された配列の型を与える.
     * 
     * @param ownerArrayType 要素が参照された配列の型
     */
    public UnresolvedArrayElementUsage(final UnresolvedTypeInfo ownerArrayType) {
        if (null == ownerArrayType) {
            throw new NullPointerException("ownerArrayType is null.");
        }

        this.ownerArrayType = ownerArrayType;
    }

    /**
     * 要素が参照された配列の型を返す
     * 
     * @return 要素が参照された配列の型
     */
    public UnresolvedTypeInfo getOwnerArrayType() {
        return this.ownerArrayType;
    }

    /**
     * この配列要素の参照の型としての名前.
     */
    public String getTypeName() {
        return this.ownerArrayType.getTypeName() + "[]";
    }

    /**
     * 要素が参照された配列の型
     */
    private final UnresolvedTypeInfo ownerArrayType;

}
