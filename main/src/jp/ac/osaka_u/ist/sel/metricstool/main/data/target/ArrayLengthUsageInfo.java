package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * 配列型の length フィールド使用を表すクラス
 * 
 * @author higo
 * 
 */
public final class ArrayLengthUsageInfo extends EntityUsageInfo {

    /**
     * 親となるエンティティ使用を与えてオブジェクトを初期化
     * 
     * @param ownerEntityUsage 親エンティティ
     */
    public ArrayLengthUsageInfo(final EntityUsageInfo ownerEntityUsage, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        if (null == ownerEntityUsage) {
            throw new NullPointerException();
        }

        if (!(ownerEntityUsage.getType() instanceof ArrayTypeInfo)) {
            throw new IllegalArgumentException();
        }

        this.ownerEntityUsage = ownerEntityUsage;
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

    /**
     * 親エンティティを返す
     * 
     * @return 親エンティティ
     */
    public EntityUsageInfo getOwnerEntity() {
        return this.ownerEntityUsage;
    }

    private final EntityUsageInfo ownerEntityUsage;
}
