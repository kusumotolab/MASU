package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * if ブロックを表すクラス
 * 
 * @author higo
 * 
 */
public final class IfBlockInfo extends ConditionalBlockInfo {

    /**
     * 位置情報を与えて if ブロックを初期化
     * 
     * @param ownerClass 所有クラス
     * @param ownerMethod 所有メソッド
     * @param outerSpace 外側のブロック
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public IfBlockInfo(final TargetClassInfo ownerClass, final CallableUnitInfo ownerMethod,
            final LocalSpaceInfo outerSpace, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        super(ownerClass, ownerMethod, outerSpace, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * else 文を追加する
     * 
     * @param sequentElseBlock 追加する else 文
     */
    public void setSequentElseBlock(final ElseBlockInfo sequentElseBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == sequentElseBlock) {
            throw new NullPointerException();
        }

        this.sequentElseBlock = sequentElseBlock;
    }

    /**
     * このIf文に対応するElse文を返す
     * 
     * @return このIf文に対応するElse文
     */
    public ElseBlockInfo getSequentElseBlock() {
        return this.sequentElseBlock;
    }

    /**
     * 対応するelseブロックが存在するかどうか表す
     * @return 対応するelseブロックが存在するならtrue
     */
    public boolean hasElseBlock() {
        return null != this.sequentElseBlock;
    }

    private ElseBlockInfo sequentElseBlock;
}
