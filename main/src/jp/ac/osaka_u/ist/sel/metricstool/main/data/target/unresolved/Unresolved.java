package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Resolved;


/**
 * 名前解決されていない情報であることを表すインターフェース
 * 
 * @author y-higo
 */
public interface Unresolved {

    /**
     * 名前解決された情報をセットする
     * 
     * @param resolvedInfo 名前解決された情報
     */
    void setResolvedInfo(Resolved resolvedInfo);

    /**
     * 名前解決された情報を返す
     * 
     * @return 名前解決された情報
     */
    Resolved getResolvedInfo();
}
