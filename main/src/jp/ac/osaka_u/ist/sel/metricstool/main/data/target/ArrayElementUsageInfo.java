package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * 配列要素の使用を表すクラス
 * 
 * @author higo
 * 
 */
public class ArrayElementUsageInfo extends EntityUsageInfo {

    /**
     * 要素の親，つまり配列型のエンティティ使用を与えて，オブジェクトを初期化
     * 
     * @param ownerEntityUsage 配列型のエンティティ使用
     */
    public ArrayElementUsageInfo(final EntityUsageInfo ownerEntityUsage) {

        super();

        if (null == ownerEntityUsage) {
            throw new NullPointerException();
        }

        this.ownerEntityUsage = ownerEntityUsage;
    }

    /**
     * この配列要素の使用の型を返す
     * 
     * @return この配列要素の使用の型
     */
    @Override
    public TypeInfo getType() {

        final TypeInfo ownerType = this.getOwnerEntityUsage().getType();
        assert ownerType instanceof ArrayTypeInfo : "ArrayElementUsage attaches unappropriate type!";

        // 配列の次元に応じて型を生成
        final int ownerArrayDimension = ((ArrayTypeInfo) ownerType).getDimension();
        final EntityUsageInfo ownerArrayElement = ((ArrayTypeInfo) ownerType).getElement();

        // 配列が二次元以上の場合は，次元を一つ落とした配列を返し，一次元の場合は，要素の型を返す．
        return 1 < ownerArrayDimension ? ArrayTypeInfo.getType(ownerArrayElement,
                ownerArrayDimension - 1).getType() : ownerArrayElement.getType();
    }

    /**
     * この要素の親，つまり配列型のエンティティ使用を返す
     * 
     * @return この要素の親を返す
     */
    public EntityUsageInfo getOwnerEntityUsage() {
        return this.ownerEntityUsage;
    }

    private final EntityUsageInfo ownerEntityUsage;
}
