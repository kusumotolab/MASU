package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Set;


/**
 * 括弧で括られた式を表すクラス
 * 
 * @author higo
 *
 */
public final class ParenthesesExpressionInfo extends ExpressionInfo {

    /**
     * 
     */
    private static final long serialVersionUID = -742042745531180181L;

    /**
     * オブジェクトを初期化　
     * 
     * @param parentheticExpression 括弧内の式
     * @param ownerMethod 所有メソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public ParenthesesExpressionInfo(final ExpressionInfo parentheticExpression,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        if (null == parentheticExpression) {
            throw new IllegalArgumentException();
        }
        this.parentheticExpression = parentheticExpression;
        this.parentheticExpression.setOwnerExecutableElement(this);
    }

    /**
     * 括弧の内側の式を返す
     * 
     * @return 括弧の内側の式
     */
    public ExpressionInfo getParnentheticExpression() {
        return this.parentheticExpression;
    }

    /**
     * 式の型を返す
     * 
     * @return 式の型
     */
    @Override
    public TypeInfo getType() {
        return this.getParnentheticExpression().getType();
    }

    /**
     * 式のテキスト表現を返す
     * 
     * @return 式のテキスト表現
     */
    @Override
    public String getText() {

        final StringBuilder text = new StringBuilder();
        text.append("(");

        final ExpressionInfo parentheticExpression = this.getParnentheticExpression();
        text.append(parentheticExpression.getText());

        text.append(")");

        return text.toString();
    }

    /**
     * 式内のメソッド呼び出し一覧を返す
     * 
     * @return 式内のメソッド呼び出し一覧
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        return this.getParnentheticExpression().getCalls();
    }

    /**
     * 式内の変数使用一覧を返す
     * 
     * @return 式内の変数使用一覧
     */
    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        return this.getParnentheticExpression().getVariableUsages();
    }

    /**
     * この式で投げられる可能性がある例外のSetを返す
     * 
     * @return　この式で投げられる可能性がある例外のSet
     */
    @Override
    public Set<ReferenceTypeInfo> getThrownExceptions() {
        return Collections.unmodifiableSet(this.getParnentheticExpression().getThrownExceptions());
    }

    final ExpressionInfo parentheticExpression;
}
