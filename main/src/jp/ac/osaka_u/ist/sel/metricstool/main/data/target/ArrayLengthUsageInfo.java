package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * 配列型の length フィールド使用を表すクラス
 * 
 * @author higo
 * 
 */
public final class ArrayLengthUsageInfo extends FieldUsageInfo {

    /**
     * 親となるエンティティ使用を与えてオブジェクトを初期化
     *
     * @param ownerExecutableElement オーナーエレメント
     * @param qualifierExpression 親エンティティ
     * @param qualifierType 親エンティティの型（必要ないかも．．．）
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public ArrayLengthUsageInfo(final ExecutableElementInfo ownerExecutableElement,
            final ExpressionInfo qualifierExpression, final ArrayTypeInfo qualifierType,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(ownerExecutableElement, qualifierExpression, qualifierType, new ArrayLengthInfo(
                qualifierType), true, fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * length フィールド使用の型を返す．
     * 
     * @return length フィールド使用の型
     */
    @Override
    public TypeInfo getType() {
        return PrimitiveTypeInfo.INT;
    }
}
