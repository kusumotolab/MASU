package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * 配列型の length フィールド使用を表すクラス
 * 
 * @author y-higo
 * 
 */
public final class ArrayLengthUsageInfo extends EntityUsageInfo {

    /**
     * 親となるエンティティ使用を与えてオブジェクトを初期化
     * 
     * @param ownerEntityUsage 親エンティティ
     */
    public ArrayLengthUsageInfo(final EntityUsageInfo ownerEntityUsage) {

        if (null == ownerEntityUsage) {
            throw new NullPointerException();
        }

        if (!(ownerEntityUsage instanceof ArrayTypeInfo)) {
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
