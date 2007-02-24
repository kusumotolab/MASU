package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;

/**
 * 未解決 finally ブロック情報を表すクラス
 * 
 * @author y-higo
 */
public final class UnresolvedFinallyBlock extends UnresolvedBlock {

    /**
     * 対応する try ブロック情報を与えて finally ブロックを初期化
     * 
     * @param correspondingTryBlock
     */
    public UnresolvedFinallyBlock(final UnresolvedTryBlock correspondingTryBlock) {

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
    public UnresolvedTryBlock getCorrespondingTryBlock() {
        return this.correspondingTryBlock;
    }

    private final UnresolvedTryBlock correspondingTryBlock;
}
