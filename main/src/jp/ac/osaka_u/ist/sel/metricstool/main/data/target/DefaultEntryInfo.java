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
     * @param ownerSwitchBlock
     */
    public DefaultEntryInfo(final TargetClassInfo ownerClass, final TargetMethodInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn,
            final SwitchBlockInfo ownerSwitchBlock, final boolean breakStatement) {
        super(ownerClass, ownerMethod, fromLine, fromColumn, toLine, toColumn, ownerSwitchBlock,
                breakStatement);
    }
}
