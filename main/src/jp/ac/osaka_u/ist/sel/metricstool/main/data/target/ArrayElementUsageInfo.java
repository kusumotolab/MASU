package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * 配列要素の使用を表すクラス
 * 
 * @author higo
 * 
 */
public class ArrayElementUsageInfo extends EntityUsageInfo {

    /**
     * 要素の親，つまり配列型の式とインデックスを与えて，オブジェクトを初期化
     * 
     * @param ownerExecutableElement オーナー要素
     * @param qualifierExpression 配列型の式
     * @param indexExpression インデックス
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public ArrayElementUsageInfo(final ExecutableElementInfo ownerExecutableElement,
            final ExpressionInfo qualifierExpression, final ExpressionInfo indexExpression,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(ownerExecutableElement, fromLine, fromColumn, toLine, toColumn);

        if (null == qualifierExpression) {
            throw new NullPointerException();
        }

        this.qualifierExpression = qualifierExpression;
        this.indexExpression = indexExpression;
    }

    /**
     * この配列要素の使用の型を返す
     * 
     * @return この配列要素の使用の型
     */
    @Override
    public TypeInfo getType() {

        final TypeInfo ownerType = this.getQualifierExpression().getType();

        // 親が配列型である，と解決できている場合
        if (ownerType instanceof ArrayTypeInfo) {
            // 配列の次元に応じて型を生成
            final int ownerArrayDimension = ((ArrayTypeInfo) ownerType).getDimension();
            final TypeInfo ownerArrayElement = ((ArrayTypeInfo) ownerType).getElementType();

            // 配列が二次元以上の場合は，次元を一つ落とした配列を返し，一次元の場合は，要素の型を返す．
            return 1 < ownerArrayDimension ? ArrayTypeInfo.getType(ownerArrayElement,
                    ownerArrayDimension - 1) : ownerArrayElement;
        }

        // 配列型でない，かつ不明型でない場合はおかしい
        assert ownerType instanceof UnknownTypeInfo : "ArrayElementUsage attaches unappropriate type!";

        return ownerType;
    }

    /**
     * この要素の親，つまり配列型の式を返す
     * 
     * @return この要素の親を返す
     */
    public ExpressionInfo getQualifierExpression() {
        return this.qualifierExpression;
    }

    /**
     * この要素のインデックスを返す
     * 
     * @return　この要素のインデックス
     */
    public ExpressionInfo getIndexExpression() {
        return this.indexExpression;
    }

    /**
     * この式（配列要素の使用）における変数利用の一覧を返す
     * 
     * @return 変数利用のSet
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        final Set<VariableUsageInfo<?>> variableUsages = new HashSet<VariableUsageInfo<?>>(
                this.indexExpression.getVariableUsages());
        variableUsages.addAll(this.getQualifierExpression().getVariableUsages());
        return Collections.unmodifiableSet(variableUsages);
        //return this.getOwnerEntityUsage().getVariableUsages();
    }

    /**
     * この配列要素使用のテキスト表現（String型）を返す
     * 
     * @return この配列要素使用のテキスト表現
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        final ExpressionInfo expression = this.getQualifierExpression();
        sb.append(expression.getText());

        sb.append("[");

        final ExpressionInfo indexExpression = this.getIndexExpression();
        sb.append(indexExpression.getText());

        sb.append("]");

        return sb.toString();
    }

    private final ExpressionInfo qualifierExpression;

    private final ExpressionInfo indexExpression;
}
