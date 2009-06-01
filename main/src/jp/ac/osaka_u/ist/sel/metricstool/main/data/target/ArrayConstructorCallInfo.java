package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 配列コンストラクタ呼び出しを表すクラス
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class ArrayConstructorCallInfo extends ConstructorCallInfo<ArrayTypeInfo> {

    /**
     * 型を与えて配列コンストラクタ呼び出しを初期化
     * 
     * @param arrayType 呼び出しの型
     * @param indexExpressions インデックスの式
     * @param ownerMethod オーナーメソッド 
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列 
     */
    public ArrayConstructorCallInfo(final ArrayTypeInfo arrayType,
            final List<ExpressionInfo> indexExpressions, final CallableUnitInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(arrayType, null, ownerMethod, fromLine, fromColumn, toLine, toColumn);

        if (null == indexExpressions) {
            throw new IllegalArgumentException();
        }
        this.indexExpressions = Collections.unmodifiableList(indexExpressions);

        for (final ExpressionInfo element : this.indexExpressions) {
            element.setOwnerExecutableElement(this);
        }
    }

    /**
     * インデックスの式を取得
     * @param dimention インデックスの式を取得する配列の次元
     * @return 指定した次元のインデックスの式
     */
    public ExpressionInfo getIndexExpression(final int dimention) {
        return this.indexExpressions.get(dimention - 1);
    }

    /**
     * インデックスの式のリストを取得
     * 
     * @return インデックスの式のリスト 
     */
    public List<ExpressionInfo> getIndexExpressions() {
        return this.indexExpressions;
    }

    /**
     * 配列の初期化式のテキスト表現を返す
     * 
     * @return 配列の初期化式のテキスト表現
     * 
     */
    @Override
    public String getText() {

        final StringBuilder text = new StringBuilder();
        text.append("new ");

        final ArrayTypeInfo arrayType = this.getType();
        final TypeInfo elementType = arrayType.getElementType();
        text.append(elementType.getTypeName());

        final int dimension = arrayType.getDimension();
        for (int i = 1; i <= dimension; i++) {
            final ExpressionInfo indexExpression = this.getIndexExpression(i);
            text.append("[");
            text.append(indexExpression.getText());
            text.append("]");
        }

        return text.toString();
    }

    /**
     * この式で投げられる可能性がある例外のSetを返す
     * 
     * @return　この式で投げられる可能性がある例外のSet
     */
    @Override
    public Set<ClassTypeInfo> getThrownExceptions() {
        final Set<ClassTypeInfo> thrownExceptions = new HashSet<ClassTypeInfo>();
        for (final ExpressionInfo indexExpression : this.getIndexExpressions()) {
            thrownExceptions.addAll(indexExpression.getThrownExceptions());
        }
        return Collections.unmodifiableSet(thrownExceptions);
    }

    private final List<ExpressionInfo> indexExpressions;
}
