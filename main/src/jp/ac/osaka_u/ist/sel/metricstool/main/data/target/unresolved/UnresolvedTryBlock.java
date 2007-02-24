package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決 try ブロックを表すクラス
 * 
 * @author y-higo
 */
public final class UnresolvedTryBlock extends UnresolvedBlock {

    /**
     * try ブロック情報を初期化
     */
    public UnresolvedTryBlock() {
        MetricsToolSecurityManager.getInstance().checkAccess();
    }
}
