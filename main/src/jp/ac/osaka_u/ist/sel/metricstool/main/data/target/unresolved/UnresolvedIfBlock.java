package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決ifブロックを表すクラス
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedIfBlock extends UnresolvedBlock {

    /**
     * if ブロック情報を初期化
     */
    public UnresolvedIfBlock() {
        MetricsToolSecurityManager.getInstance().checkAccess();
    }
}
