package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * else ブロックを表すクラス
 * 
 * @author higo
 */
public final class ElseBlockInfo extends BlockInfo {

    /**
     * 対応する if ブロックを与えて，else ブロック情報を初期化
     * 
     * @param ownerClass 所有クラス
     * @param ownerMethod 所有メソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     * @param ownerIfBlock 対応するifブロック
     */
    public ElseBlockInfo(final TargetClassInfo ownerClass, final CallableUnitInfo ownerMethod,
            final LocalSpaceInfo outerSpace, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn, final IfBlockInfo ownerIfBlock) {

        super(ownerClass, ownerMethod, outerSpace, fromLine, fromColumn, toLine, toColumn);

        if (null == ownerIfBlock) {
            throw new NullPointerException();
        }

        this.ownerIfBlock = ownerIfBlock;
    }

    /**
     * この else ブロックと対応する if ブロックを返す
     * 
     * @return この else ブロックと対応する if ブロック
     */
    public IfBlockInfo getOwnerIfBlock() {
        return this.ownerIfBlock;
    }

    /**
     * この else ブロックと対応する if ブロックを保存するための変数
     */
    private final IfBlockInfo ownerIfBlock;
}
