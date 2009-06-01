package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;


/**
 * Foreachブロックを表すクラス
 * 
 * @author higo
 *
 */
public final class ForeachBlockInfo extends BlockInfo {

    /**
     * 位置情報を与えてForeachブロックを初期化
     * 
     * @param ownerClass 所属クラス
     * @param outerSpace 外側のブロック
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     * @param iteratorVariable 繰り返し用の変数
     * @param iteratorExpression 繰り返し用の式
     */
    public ForeachBlockInfo(final TargetClassInfo ownerClass, final LocalSpaceInfo outerSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn,
            final VariableDeclarationStatementInfo iteratorVariableDeclaration,
            final ExpressionInfo iteratorExpression) {
        super(ownerClass, outerSpace, fromLine, fromColumn, toLine, toColumn);

        this.iteratorVariableDeclaration = iteratorVariableDeclaration;
        this.iteratorExpression = iteratorExpression;
    }

    /**
     * 繰り返し用の変数を返す
     * 
     * @return　繰り返し用の変数
     */
    public VariableDeclarationStatementInfo getIteratorVariableDeclaration() {
        return this.iteratorVariableDeclaration;
    }

    /**
     * 繰り返し用の式を返す
     * 
     * @return 繰り返し用の式
     */
    public ExpressionInfo getIteratorExpression() {
        return this.iteratorExpression;
    }

    /**
     * このForeachブロックのテキスト表現を返す
     */
    @Override
    public String getText() {

        final StringBuilder text = new StringBuilder();

        text.append("for (");

        text.append(this.getIteratorVariableDeclaration().getText());
        text.deleteCharAt(text.length() - 1);

        text.append(":");

        text.append(this.getIteratorExpression().getText());

        text.append(") {");
        text.append(System.getProperty("line.separator"));

        final SortedSet<StatementInfo> statements = this.getStatements();
        for (final StatementInfo statement : statements) {
            text.append(statement.getText());
            text.append(System.getProperty("line.separator"));
        }

        text.append("}");

        return text.toString();
    }

    /**
     * この式で投げられる可能性がある例外のSetを返す
     * 
     * @return　この式で投げられる可能性がある例外のSet
     */
    @Override
    public Set<ClassTypeInfo> getThrownExceptions() {
        final Set<ClassTypeInfo> thrownExpressions = new HashSet<ClassTypeInfo>();
        thrownExpressions.addAll(super.getThrownExceptions());
        thrownExpressions.addAll(this.getIteratorVariableDeclaration().getThrownExceptions());
        thrownExpressions.addAll(this.getIteratorExpression().getThrownExceptions());
        return Collections.unmodifiableSet(thrownExpressions);
    }

    private final VariableDeclarationStatementInfo iteratorVariableDeclaration;

    private final ExpressionInfo iteratorExpression;
}
