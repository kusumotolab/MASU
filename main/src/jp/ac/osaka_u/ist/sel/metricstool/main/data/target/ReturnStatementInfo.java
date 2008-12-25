package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * return文の情報を保存するためのクラス
 * 
 * @author t-miyake
 *
 */
public class ReturnStatementInfo extends SingleStatementInfo {

    /**
     * return文の戻り値を表す式と位置情報を与えて初期化
     * @param ownerMethod オーナーメソッド
     * @param ownerSpaceInfo 文を直接所有する空間
     * @param returnedExpression
     * @param fromLine
     * @param fromColumn
     * @param toLine
     * @param toColumn
     */
    public ReturnStatementInfo(final LocalSpaceInfo ownerSpaceInfo,
            final ExpressionInfo returnedExpression, int fromLine, int fromColumn, int toLine,
            int toColumn) {
        super(ownerSpaceInfo, fromLine, fromColumn, toLine, toColumn);

        if (null != returnedExpression) {
            this.returnedExpression = returnedExpression;
        } else {
            this.returnedExpression = new EmptyExpressionInfo(null, toLine, toColumn - 1, toLine,
                    toColumn - 1);
        }
        this.returnedExpression.setOwnerExecutableElement(this);
    }

    /**
     * return文の戻り値を表す式を返す
     * 
     * @return return文の戻り値を表す式
     */
    public final ExpressionInfo getReturnedExpression() {
        return this.returnedExpression;
    }

    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return this.returnedExpression.getVariableUsages();
    }

    /**
     * このreturn文のテキスト表現（型）を返す
     * 
     * @return このreturn文のテキスト表現（型）
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("return ");

        final ExpressionInfo statement = this.getReturnedExpression();
        sb.append(statement.getText());

        sb.append(";");

        return sb.toString();
    }

    /**
     * return文の戻り値を表す式を保存するための変数
     */
    private final ExpressionInfo returnedExpression;
}
