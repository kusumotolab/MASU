package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決ifブロックを表すクラス
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedIfBlockInfo extends UnresolvedBlockInfo {

    /**
     * if ブロック情報を初期化
     */
    public UnresolvedIfBlockInfo() {
        MetricsToolSecurityManager.getInstance().checkAccess();
    }
}
