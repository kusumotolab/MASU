package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


/**
 * switch 文の default エントリを表すクラス
 * 
 * @author y-higo
 */
public final class UnresolvedDefaultEntryInfo extends UnresolvedCaseEntryInfo {

    /**
     * 対応する switch ブロック情報を与えて default エントリを初期化
     * 
     * @param correspondingSwitchBlock
     */
    public UnresolvedDefaultEntryInfo(final UnresolvedSwitchBlockInfo correspondingSwitchBlock) {
        super(correspondingSwitchBlock);
    }
}
