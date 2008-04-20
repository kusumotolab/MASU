package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * switch ブロックを表すクラス
 * 
 * @author higo
 * 
 */
public final class SwitchBlockInfo extends ConditionalBlockInfo {

    /**
     * switch ブロック情報を初期化
     *
     * @param ownerClass 所有クラス
     * @param ownerMethod 所有メソッド
     * @param conditionalClause 条件節
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public SwitchBlockInfo(final TargetClassInfo ownerClass, final CallableUnitInfo ownerMethod,
            final ConditionalClauseInfo conditionalClause, final LocalSpaceInfo outerSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(ownerClass, ownerMethod, conditionalClause, outerSpace, fromLine, fromColumn, toLine,
                toColumn);
    }

    /**
     * このswitch ブロックに case エントリを追加する
     * 
     * @param innerBlock 追加する case エントリ
     */
    //    @Override
    //    public void addInnerBlock(final BlockInfo innerBlock) {
    //
    //        MetricsToolSecurityManager.getInstance().checkAccess();
    //        if (null == innerBlock) {
    //            throw new NullPointerException();
    //        }
    //
    //        if (!(innerBlock instanceof CaseEntryInfo)) {
    //            throw new IllegalArgumentException(
    //                    "Inner block of switch statement must be case or default entry!");
    //        }
    //        super.addInnerBlock(innerBlock);
    //    }
}
