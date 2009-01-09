package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.SortedSet;


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
     * @param outerSpace 外側のブロック
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public WhileBlockInfo(final TargetClassInfo ownerClass, final LocalSpaceInfo outerSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(ownerClass, outerSpace, fromLine, fromColumn, toLine, toColumn);
    }

    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("while (");

        final ConditionalClauseInfo conditionalClause = this.getConditionalClause();
        sb.append(conditionalClause.getText());

        sb.append(") {");
        sb.append(System.getProperty("line.separator"));

        final SortedSet<StatementInfo> statements = this.getStatements();
        for (final StatementInfo statement : statements) {
            sb.append(statement.getText());
            sb.append(System.getProperty("line.separator"));
        }

        sb.append("}");

        return sb.toString();
    }
    
    @Override
    protected boolean isLoopStatement() {
        return true;
    }
}
