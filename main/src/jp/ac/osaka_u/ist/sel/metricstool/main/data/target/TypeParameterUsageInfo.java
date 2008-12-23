package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * 型パラメータの使用を表すクラス
 * 
 * @author higo
 *
 */
public final class TypeParameterUsageInfo extends EntityUsageInfo {

    /**
     * 必要な情報を与えて，オブジェクトを初期化
     * 
     * @param ownerExecutableElement オーナーエレメント
     * @param expression 利用されている式
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public TypeParameterUsageInfo(final ExecutableElementInfo ownerExecutableElement,
            final ExpressionInfo expression, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(ownerExecutableElement, fromLine, fromColumn, toLine, toColumn);

        if (null == expression) {
            throw new NullPointerException();
        }

        this.expression = expression;
    }

    @Override
    public TypeInfo getType() {
        return this.expression.getType();
    }

    /**
     * この型パラメータ使用内の式を返す
     * 
     * @return この型パラメータ使用内の式
     */
    public ExpressionInfo getExpression() {
        return this.expression;
    }

    /**
     * 型パラメータの使用に変数使用が含まれることはないので空のセットを返す
     * 
     * @return 空のセット
     */
    @Override
    public final Set<VariableUsageInfo<?>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }

    /**
     * この型パラメータ使用のテキスト表現（型）を返す
     * 
     * @return この型パラメータ使用のテキスト表現（型）
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("<");

        final ExpressionInfo expression = this.getExpression();
        sb.append(expression.getText());

        sb.append(">");

        return sb.toString();
    }

    private final ExpressionInfo expression;
}
