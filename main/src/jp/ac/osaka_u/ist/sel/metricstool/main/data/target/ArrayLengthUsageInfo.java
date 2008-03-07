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
     * @param ownerEntityUsage 親エンティティ
     */
    public ArrayLengthUsageInfo(final EntityUsageInfo ownerUsage, final ArrayTypeInfo ownerType, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(ownerUsage, ownerType, new ArrayLengthInfo(ownerType), true, fromLine, fromColumn, toLine,
                toColumn);
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
