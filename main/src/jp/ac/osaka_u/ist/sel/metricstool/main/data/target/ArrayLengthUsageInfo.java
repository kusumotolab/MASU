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
     * @param qualifierExpression 親エンティティ
     * @param qualifierType 親エンティティの型（必要ないかも．．．）
     * @param ownerMethod オーナーメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public ArrayLengthUsageInfo(final ExpressionInfo qualifierExpression,
            final ArrayTypeInfo qualifierType, final CallableUnitInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(qualifierExpression, qualifierType, new ArrayLengthInfo(qualifierType), true,
                ownerMethod, fromLine, fromColumn, toLine, toColumn);
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
