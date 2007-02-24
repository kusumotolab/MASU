package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決 単純ブロック({})を表すクラス
 * 
 * @author y-higo
 */
public final class UnresolvedSimpleBlock extends UnresolvedBlock {

    /**
     * 単純ブロック情報を初期化
     */
    public UnresolvedSimpleBlock() {
        MetricsToolSecurityManager.getInstance().checkAccess();
    }
}
