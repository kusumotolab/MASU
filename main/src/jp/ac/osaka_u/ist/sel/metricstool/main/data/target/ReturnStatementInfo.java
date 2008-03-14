package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * return文の情報を保存するためのクラス
 * 
 * @author t-miyake
 *
 */
public class ReturnStatementInfo extends SingleStatementInfo {

    /**
     * return文の戻り値を表す式と位置情報を与えて初期化
     * @param returnedExpression
     * @param fromLine
     * @param fromColumn
     * @param toLine
     * @param toColumn
     */
    public ReturnStatementInfo(final ExpressionInfo returnedExpression, int fromLine,
            int fromColumn, int toLine, int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);

        if (null == returnedExpression) {
            throw new IllegalArgumentException("returnedExpression is null");
        }

        this.returnedExpression = returnedExpression;
    }

    /**
     * return文の戻り値を表す式を返す
     * 
     * @return return文の戻り値を表す式
     */
    public final ExpressionInfo getReturnedExpression() {
        return this.returnedExpression;
    }

    /**
     * return文の戻り値を表す式を保存するための変数
     */
    private final ExpressionInfo returnedExpression;
}
