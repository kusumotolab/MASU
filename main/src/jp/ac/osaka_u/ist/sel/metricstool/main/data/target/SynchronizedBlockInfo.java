package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.SortedSet;


/**
 * synchronized ブロックを表すクラス
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public final class SynchronizedBlockInfo extends BlockInfo {

    /**
     * 位置情報を与えて synchronized ブロックを初期化
     * 
     * @param ownerClass 所有クラス
     * @param outerSpace 外側のブロック
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public SynchronizedBlockInfo(final TargetClassInfo ownerClass, final LocalSpaceInfo outerSpace,
            final ExpressionInfo synchronizedExpression, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        super(ownerClass, outerSpace, fromLine, fromColumn, toLine, toColumn);

        if (null == synchronizedExpression) {
            throw new IllegalArgumentException();
        }

        this.synchronizedExpression = synchronizedExpression;
        this.synchronizedExpression.setOwnerExecutableElement(this);
    }

    /**
     * このsynchronized文のテキスト表現（String型）を返す
     * 
     * @return このsynchronized文のテキスト表現（String型）
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("synchronized (");

        final ExpressionInfo expression = this.getSynchronizedExpression();
        sb.append(expression.getText());

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

    public final ExpressionInfo getSynchronizedExpression() {
        return synchronizedExpression;
    }

    private final ExpressionInfo synchronizedExpression;
}
