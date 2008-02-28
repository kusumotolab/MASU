package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * while ブロックを表すクラス
 * 
 * @author higo
 * 
 */
public final class WhileBlockInfo extends ConditionalBlockInfo {

    /**
     * 位置情報を与えて while ブロックを初期化
     * 
     * @param ownerClass 所有クラス
     * @param ownerMethod 所有メソッド
     * @param conditionalClause 条件節
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public WhileBlockInfo(final TargetClassInfo ownerClass, final CallableUnitInfo ownerMethod,
            final ConditionalClauseInfo conditionalClause, final LocalSpaceInfo outerSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(ownerClass, ownerMethod, conditionalClause, outerSpace, fromLine, fromColumn, toLine,
                toColumn);
    }
}
