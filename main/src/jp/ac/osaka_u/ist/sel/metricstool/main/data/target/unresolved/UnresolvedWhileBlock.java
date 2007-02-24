package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決 while ブロックを表すクラス
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedWhileBlock extends UnresolvedBlock{

    /**
     * while ブロック情報を初期化
     */
    public UnresolvedWhileBlock() {
        MetricsToolSecurityManager.getInstance().checkAccess();
    }
}
