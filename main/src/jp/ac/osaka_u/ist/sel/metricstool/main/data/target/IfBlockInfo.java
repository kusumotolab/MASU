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
     * @param conditionalClause 条件節
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public IfBlockInfo(final TargetClassInfo ownerClass, final CallableUnitInfo ownerMethod,
            final ConditionalClauseInfo conditionalClause, final LocalSpaceInfo outerSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(ownerClass, ownerMethod, conditionalClause, outerSpace, fromLine, fromColumn, toLine,
                toColumn);
    }

    public void setSequentElseBlock(final ElseBlockInfo sequentElseBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == sequentElseBlock) {
            throw new NullPointerException();
        }

        this.sequentElseBlock = sequentElseBlock;
    }

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
