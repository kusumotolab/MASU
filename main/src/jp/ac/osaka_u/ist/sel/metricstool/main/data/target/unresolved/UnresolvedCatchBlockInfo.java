package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決 catch ブロック情報を表すクラス
 * 
 * @author y-higo
 */
public final class UnresolvedCatchBlockInfo extends UnresolvedBlockInfo {

    /**
     * 対応する try ブロック情報を与えて catch ブロックを初期化
     * 
     * @param correspondingTryBlock
     */
    public UnresolvedCatchBlockInfo(final UnresolvedTryBlockInfo correspondingTryBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == correspondingTryBlock) {
            throw new NullPointerException();
        }

        this.correspondingTryBlock = correspondingTryBlock;
    }

    /**
     * 対応する try ブロックを返す
     * 
     * @return 対応する try ブロック
     */
    public UnresolvedTryBlockInfo getCorrespondingTryBlock() {
        return this.correspondingTryBlock;
    }

    private final UnresolvedTryBlockInfo correspondingTryBlock;
}
