package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * switch ブロックを表すクラス
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedSwitchBlockInfo extends UnresolvedBlockInfo {

    /**
     * switch ブロック情報を初期化
     * 
     */
    public UnresolvedSwitchBlockInfo() {
        MetricsToolSecurityManager.getInstance().checkAccess();
    }

    /**
     * このswitch ブロックに case エントリを追加する
     * 
     * @param innerBlock 追加する case エントリ
     */
    @Override
    public void addInnerBlock(final UnresolvedBlockInfo innerBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == innerBlock) {
            throw new NullPointerException();
        }

        if (!(innerBlock instanceof UnresolvedCaseEntryInfo)) {
            throw new IllegalArgumentException(
                    "Inner block of switch statement must be case or default entry!");
        }

        super.addInnerBlock(innerBlock);
    }
}
