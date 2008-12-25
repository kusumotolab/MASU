package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * throw文の情報を保有するクラス
 * 
 * @author t-miyake
 *
 */
public class ThrowStatementInfo extends SingleStatementInfo {

    /**
     * throw文によって投げられる例外を表す式と位置情報を与えて初期化
     * 
     * @param ownerSpace 文を直接所有する空間
     * @param thrownEpression throw文によって投げられる例外を表す式
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public ThrowStatementInfo(final LocalSpaceInfo ownerSpace, ExpressionInfo thrownEpression,
            int fromLine, int fromColumn, int toLine, int toColumn) {
        super(ownerSpace, fromLine, fromColumn, toLine, toColumn);

        if (null == thrownEpression) {
            throw new IllegalArgumentException("thrownExpression is null");
        }
        this.thrownEpression = thrownEpression;

        this.thrownEpression.setOwnerExecutableElement(this);
    }

    /**
     * throw文によって投げられる例外を表す式を返す
     * 
     * @return throw文によって投げられる例外を表す式
     */
    public final ExpressionInfo getThrownExpression() {
        return this.thrownEpression;
    }

    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return this.thrownEpression.getVariableUsages();
    }

    /**
     * このthrow文のテキスト表現（型）を返す
     * 
     * @return このthrow文のテキスト表現（型）
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("throw ");

        final ExpressionInfo expression = this.getThrownExpression();
        sb.append(expression.getText());

        sb.append(";");

        return sb.toString();
    }

    /**
     * throw文によって投げられる例外を表す式
     */
    private final ExpressionInfo thrownEpression;

}
