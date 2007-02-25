package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決 synchronized 文を表すクラス
 * 
 * @author y-higo
 */
public final class UnresolvedSynchronizedBlockInfo extends UnresolvedBlockInfo {

    /**
     * synchronized ブロック情報を初期化
     */
    public UnresolvedSynchronizedBlockInfo() {
        MetricsToolSecurityManager.getInstance().checkAccess();
    }
}
