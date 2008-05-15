package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * catch ブロック情報を表すクラス
 * 
 * @author higo
 */
public final class CatchBlockInfo extends BlockInfo {

    /**
     * 対応する try ブロック情報を与えて catch ブロックを初期化
     * 
     * @param ownerClass 所有クラス
     * @param ownerMethod 所有メソッド
     * @param outerSpace 外側のブロック
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     * @param ownerTryBlock このcatch節が属するtryブロック
     */
    public CatchBlockInfo(final TargetClassInfo ownerClass, final CallableUnitInfo ownerMethod,
            final LocalSpaceInfo outerSpace, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn, final TryBlockInfo ownerTryBlock) {

        super(ownerClass, ownerMethod, outerSpace, fromLine, fromColumn, toLine, toColumn);

        if (null == ownerTryBlock) {
            throw new NullPointerException();
        }

        this.ownerTryBlock = ownerTryBlock;
    }

    /**
     * 対応する try ブロックを返す
     * 
     * @return 対応する try ブロック
     */
    public TryBlockInfo getOwnerTryBlock() {
        return this.ownerTryBlock;
    }

    private final TryBlockInfo ownerTryBlock;
}
