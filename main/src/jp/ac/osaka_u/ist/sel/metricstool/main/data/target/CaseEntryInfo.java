package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * switch 文の case エントリを表すクラス
 * 
 * @author higo
 */
public class CaseEntryInfo extends BlockInfo {

    /**
     * 対応する switch ブロック情報を与えて case エントリを初期化
     * 
     * @param ownerClass 所有クラス
     * @param ownerMethod 所有メソッド
     * @param ownerSwitchBlock この case エントリが属する switch ブロック
     * @param breakStatement この case エントリが break 文を持つかどうか
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public CaseEntryInfo(final TargetClassInfo ownerClass, final CallableUnitInfo ownerMethod,
            final SwitchBlockInfo ownerSwitchBlock, final boolean breakStatement,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(ownerClass, ownerMethod, fromLine, fromColumn, toLine, toColumn);

        if (null == ownerSwitchBlock) {
            throw new NullPointerException();
        }

        this.ownerSwitchBlock = ownerSwitchBlock;
        this.breakStatement = breakStatement;
    }

    /**
     * この case エントリが属する switch ブロックを返す
     * 
     * @return この case エントリが属する switch ブロック
     */
    public final SwitchBlockInfo getOwnerSwitchBlock() {
        return this.ownerSwitchBlock;
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
    private final SwitchBlockInfo ownerSwitchBlock;

    /**
     * この case エントリが break 文を持つかどうかを保存する変数
     */
    private boolean breakStatement;
}
