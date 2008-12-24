package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * for ブロックを表すクラス
 * 
 * @author higo
 * 
 */
public final class ForBlockInfo extends ConditionalBlockInfo {

    /**
     * 位置情報を与えて for ブロックを初期化
     * 
     * @param ownerClass 所属クラス
     * @param outerSpace 外側のブロック
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public ForBlockInfo(final TargetClassInfo ownerClass, final LocalSpaceInfo outerSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(ownerClass, outerSpace, fromLine, fromColumn, toLine, toColumn);

        this.initilizerExpressions = new TreeSet<ConditionInfo>();
        this.iteratorExpressions = new TreeSet<ExpressionInfo>();

    }

    /**
     * このfor文のテキスト表現（String型）を返す
     * 
     * @return このfor文のテキスト表現（String型）
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("for (");

        final SortedSet<ConditionInfo> initializerExpressions = this.getInitializerExpressions();
        for (final ConditionInfo initializerExpression : initializerExpressions) {
            sb.append(initializerExpression.getText());
            sb.append(",");
        }

        sb.append(" ; ");

        final ConditionalClauseInfo conditionalClause = this.getConditionalClause();
        sb.append(conditionalClause.getText());

        sb.append(" ; ");

        final SortedSet<ExpressionInfo> iteratorExpressions = this.getIteratorExpressions();
        for (final ExpressionInfo iteratorExpression : iteratorExpressions) {
            sb.append(iteratorExpression.getText());
            sb.append(",");
        }

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

    /**
     * for文の初期化式を追加
     * @param initializerExpression 初期化式
     */
    public final void addInitializerExpressions(final ConditionInfo initializerExpression) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == initializerExpression) {
            throw new IllegalArgumentException("initializerExpression is null");
        }

        this.initilizerExpressions.add(initializerExpression);
    }

    /**
     * for文の更新式を追加
     * @param iteratorExpression 繰り返し式
     */
    public final void addIteratorExpressions(final ExpressionInfo iteratorExpression) {
        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == iteratorExpression) {
            throw new IllegalArgumentException("updateExpression is null");
        }

        this.iteratorExpressions.add(iteratorExpression);
    }

    /**
     * 初期化式のセットを返す
     * @return 初期化式のセット
     */
    public final SortedSet<ConditionInfo> getInitializerExpressions() {
        return Collections.unmodifiableSortedSet(this.initilizerExpressions);
    }

    /**
     * 更新式のセットを返す
     * @return 更新式
     */
    public final SortedSet<ExpressionInfo> getIteratorExpressions() {
        return Collections.unmodifiableSortedSet(this.iteratorExpressions);
    }

    /**
     * 初期化式を保存するための変数
     */
    private final SortedSet<ConditionInfo> initilizerExpressions;

    /**
     * 更新式を保存するための変数
     */
    private final SortedSet<ExpressionInfo> iteratorExpressions;
}
