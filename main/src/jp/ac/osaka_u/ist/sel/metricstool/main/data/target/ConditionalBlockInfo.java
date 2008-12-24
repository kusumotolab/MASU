package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * if　や while など，条件節を持ったブロック文を表すクラス
 * 
 * @author higo
 *
 */
public abstract class ConditionalBlockInfo extends BlockInfo {

    /**
     * 位置情報を与えて初期化
     * 
     * @param ownerClass このブロックを所有するクラス
     * @param ownerSpace このブロックを所有するブロック
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    ConditionalBlockInfo(final TargetClassInfo ownerClass, final LocalSpaceInfo outerSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(ownerClass, outerSpace, fromLine, fromColumn, toLine, toColumn);

    }

    /**
     * この条件付ブロックの条件節を返す
     * 
     * @return　この条件付ブロックの条件節
     */
    public final ConditionalClauseInfo getConditionalClause() {
        return this.conditionalClause;
    }

    private ConditionalClauseInfo conditionalClause;
}
