package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


/**
 * switch 文の default エントリを表すクラス
 * 
 * @author y-higo
 */
public class UnresolvedDefaultEntry extends UnresolvedCaseEntry {

    /**
     * 対応する switch ブロック情報を与えて default エントリを初期化
     * 
     * @param correspondingSwitchBlock
     */
    public UnresolvedDefaultEntry(final UnresolvedSwitchBlock correspondingSwitchBlock) {
        super(correspondingSwitchBlock);
    }
}
