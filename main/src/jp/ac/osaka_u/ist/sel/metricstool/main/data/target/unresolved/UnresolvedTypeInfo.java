package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


/**
 * Unresolvedな型を表すインターフェース．
 * 
 * @author y-higo
 * 
 */
public interface UnresolvedTypeInfo {

    String UNRESOLVED = "unresolved";
    
    /**
     * 型名を返す
     */
    String getTypeName();
}
