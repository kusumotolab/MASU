package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * 条件文の条件節の情報を表すクラス
 * 
 * @author t-miyake
 *
 */
public class ConditionalClauseInfo extends UnitInfo {

    /**
     * 条件節を保持するブロック文と位置情報
     * @param ownerConditionalBlock 条件文の条件節を保持するブロック
     * @param condition 条件節に記述されている条件
     * @param fromLine 開始行
     * @param fromColumn 開始位置
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public ConditionalClauseInfo(final ConditionalBlockInfo ownerConditionalBlock,
            final ConditionInfo condition, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);

        if (null == ownerConditionalBlock) {
            throw new IllegalArgumentException();
        }

        this.ownerCondtiBlock = ownerConditionalBlock;
        this.condition = null != condition ? condition : new EmptyExpressionInfo(
                ownerConditionalBlock, toLine, toColumn - 1, toLine, toColumn - 1);
    }

    /**
     * 条件節を保持するブロックを返す
     * @return 条件節を保持するブロック
     */
    public final ConditionalBlockInfo getOwnerConditionalBlock() {
        return this.ownerCondtiBlock;
    }

    /**
     * 条件節に記述されている条件を返す
     * @return 条件節に記述されている条件
     */
    public final ConditionInfo getCondition() {
        return this.condition;
    }

    public final String getText() {
        return "";
    }

    /**
     * 条件節を保持するブロックを表す変数
     */
    private final ConditionalBlockInfo ownerCondtiBlock;

    /**
     * 条件節に記述されている条件を表す変数
     */
    private final ConditionInfo condition;
}
