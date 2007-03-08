package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * finally ブロック情報を表すクラス
 * 
 * @author y-higo
 */
public final class FinallyBlockInfo extends BlockInfo {

    /**
     * 対応する try ブロック情報を与えて finally ブロックを初期化
     * 
     * @param ownerClass 所有クラス
     * @param ownerMethod 所有メソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     * @param ownerTryBlock この finally 節が属する try ブロック
     */
    public FinallyBlockInfo(final TargetClassInfo ownerClass, final TargetMethodInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn,
            final TryBlockInfo ownerTryBlock) {

        super(ownerClass, ownerMethod, fromLine, fromColumn, toLine, toColumn);

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
