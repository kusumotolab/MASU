package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


/**
 * Unresolvedな型を表すインターフェース．
 * 
 * @author y-higo
 * 
 */
public interface UnresolvedTypeInfo extends Comparable<UnresolvedTypeInfo> {

    /**
     * 型名を返す
     */
    String getTypeName();

    /**
     * オブジェクトの等価性をチェックする
     * 
     * @param typeInfo 比較対象オブジェクト
     * @return 等しい場合は true,そうでない場合は false
     */
    boolean equals(UnresolvedTypeInfo typeInfo);
}
