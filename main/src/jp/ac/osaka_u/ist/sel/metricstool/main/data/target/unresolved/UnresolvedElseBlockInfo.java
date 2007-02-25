package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決 else ブロックを表すクラス
 * 
 * @author y-higo
 */
public final class UnresolvedElseBlockInfo extends UnresolvedBlockInfo {

    /**
     * 対応する if ブロックを与えて，else ブロック情報を初期化
     * 
     * @param correspondingIfBlock
     */
    UnresolvedElseBlockInfo(final UnresolvedIfBlockInfo correspondingIfBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == correspondingIfBlock) {
            throw new NullPointerException();
        }

        this.correspondingIfBlock = correspondingIfBlock;
    }

    /**
     * この else ブロックと対応する if ブロックを返す
     * 
     * @return この else ブロックと対応する if ブロック
     */
    public UnresolvedIfBlockInfo getCorrespondingIfBlock() {
        return this.correspondingIfBlock;
    }

    /**
     * この else ブロックと対応する if ブロックを保存するための変数
     */
    private final UnresolvedIfBlockInfo correspondingIfBlock;
}
