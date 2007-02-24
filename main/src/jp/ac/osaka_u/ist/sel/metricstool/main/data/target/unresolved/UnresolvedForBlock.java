package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決 for ブロックを表すクラス
 * 
 * @author y-higo
 */
public final class UnresolvedForBlock extends UnresolvedBlock {

    /**
     * for ブロック情報を初期化
     */
    public UnresolvedForBlock() {
        MetricsToolSecurityManager.getInstance().checkAccess();
    }
}
