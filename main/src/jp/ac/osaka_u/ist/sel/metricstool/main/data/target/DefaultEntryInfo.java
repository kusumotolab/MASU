package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * switch 文の default エントリを表すクラス
 * 
 * @author y-higo
 */
public final class DefaultEntryInfo extends CaseEntryInfo {

    /**
     * 対応する switch ブロック情報を与えて default エントリを初期化
     * 
     * @param correspondingSwitchBlock
     */
    public DefaultEntryInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn, final SwitchBlockInfo correspondingSwitchBlock) {
        super(fromLine, fromColumn, toLine, toColumn, correspondingSwitchBlock);
    }
}
