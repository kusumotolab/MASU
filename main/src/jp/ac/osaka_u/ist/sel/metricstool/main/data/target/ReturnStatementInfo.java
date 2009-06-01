package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Set;


/**
 * return文の情報を保存するためのクラス
 * 
 * @author t-miyake
 *
 */
@SuppressWarnings("serial")
public class ReturnStatementInfo extends SingleStatementInfo {

    /**
     * return文の戻り値を表す式と位置情報を与えて初期化
     * @param ownerMethod オーナーメソッド
     * @param ownerSpace 文を直接所有する空間
     * @param returnedExpression
     * @param fromLine
     * @param fromColumn
     * @param toLine
     * @param toColumn
     */
    public ReturnStatementInfo(final LocalSpaceInfo ownerSpace,
            final ExpressionInfo returnedExpression, int fromLine, int fromColumn, int toLine,
            int toColumn) {
        super(ownerSpace, fromLine, fromColumn, toLine, toColumn);

        if (null != returnedExpression) {
            this.returnedExpression = returnedExpression;
        } else {

            // ownerSpaceInfoがメソッドまたはコンストラクタの時
            if (ownerSpace instanceof CallableUnitInfo) {
                this.returnedExpression = new EmptyExpressionInfo((CallableUnitInfo) ownerSpace,
                        toLine, toColumn - 1, toLine, toColumn - 1);
            }

            // ownerSpaceInfoがブロック文の時            
            else if (ownerSpace instanceof BlockInfo) {
                final CallableUnitInfo ownerMethod = ((BlockInfo) ownerSpace).getOwnerMethod();
                this.returnedExpression = new EmptyExpressionInfo(ownerMethod, toLine,
                        toColumn - 1, toLine, toColumn - 1);
            }

            // それ以外の時はエラー
            else {
                throw new IllegalStateException();
            }
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
        return this.getReturnedExpression().getVariableUsages();
    }

    /**
     * 定義された変数のSetを返す
     * 
     * @return 定義された変数のSet
     */
    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        return VariableInfo.EmptySet;
    }

    /**
     * 呼び出しのSetを返す
     * 
     * @return 呼び出しのSet
     */
    @Override
    public Set<CallInfo<?>> getCalls() {
        return this.getReturnedExpression().getCalls();
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
     * この式で投げられる可能性がある例外のSetを返す
     * 
     * @return　この式で投げられる可能性がある例外のSet
     */
    @Override
    public Set<ClassTypeInfo> getThrownExceptions() {
        return Collections.unmodifiableSet(this.getReturnedExpression().getThrownExceptions());
    }

    /**
     * return文の戻り値を表す式を保存するための変数
     */
    private final ExpressionInfo returnedExpression;
}
