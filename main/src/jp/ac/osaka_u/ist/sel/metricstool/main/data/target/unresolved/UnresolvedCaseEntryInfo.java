package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * switch 文の case エントリを表すクラス
 * 
 * @author y-higo
 */
public class UnresolvedCaseEntryInfo extends UnresolvedBlockInfo {

    /**
     * 対応する switch ブロック情報を与えて case エントリを初期化
     * 
     * @param correspondingSwitchBlock
     */
    public UnresolvedCaseEntryInfo(final UnresolvedSwitchBlockInfo correspondingSwitchBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == correspondingSwitchBlock) {
            throw new NullPointerException();
        }

        this.correspondingSwitchBlock = correspondingSwitchBlock;
        this.breakStatement = false;
    }

    /**
     * この case エントリが属する switch ブロックを返す
     * 
     * @return この case エントリが属する switch ブロック
     */
    public final UnresolvedSwitchBlockInfo getCorrespondingSwitchBlock() {
        return this.correspondingSwitchBlock;
    }

    /**
     * この case エントリが break 文を持つかどうかを設定する
     * 
     * @param breakStatement break 文を持つ場合は true, 持たない場合は false
     */
    public final void setHasBreak(final boolean breakStatement) {
        this.breakStatement = breakStatement;
    }

    /**
     * この case エントリが break 文を持つかどうかを返す
     * 
     * @return break 文を持つ場合はtrue，持たない場合は false
     */
    public final boolean hasBreakStatement() {
        return this.breakStatement;
    }

    /**
     * この case エントリが属する switch ブロックを保存するための変数
     */
    private final UnresolvedSwitchBlockInfo correspondingSwitchBlock;

    /**
     * この case エントリが break 文を持つかどうかを保存する変数
     */
    private boolean breakStatement;
}
