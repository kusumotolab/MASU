package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;




/**
 * 名前解決されていない情報であることを表すインターフェース
 * 
 * @author y-higo
 */
public interface NameResolvable<T> {

    /**
     * 名前解決された情報をセットする
     * 
     * @param resolvedInfo 名前解決された情報
     */
    void setResolvedInfo(T resolvedInfo);

    /**
     * 名前解決された情報を返す
     * 
     * @return 名前解決された情報
     */
    T getResolvedInfo();
}
