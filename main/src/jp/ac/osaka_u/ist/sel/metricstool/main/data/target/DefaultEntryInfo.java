package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * switch 文の default エントリを表すクラス
 * 
 * @author higo
 */
public final class DefaultEntryInfo extends CaseEntryInfo {

    /**
     * 対応する switch ブロック情報を与えて default エントリを初期化
     * 
     * @param ownerSwitchBlock 対応する switchブロック
     */
    public DefaultEntryInfo(final SwitchBlockInfo ownerSwitchBlock, int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(ownerSwitchBlock, "default", fromLine, fromColumn, toLine, toColumn);
    }
}
