package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * 「型」を表すインターフェース．
 * 
 * @author y-higo
 * 
 */
public interface TypeInfo {

    /**
     * 型名を返す
     */
    String getTypeName();
    
    /**
     * 等価性のチェック
     */
    boolean equals(TypeInfo typeInfo);
    
}
