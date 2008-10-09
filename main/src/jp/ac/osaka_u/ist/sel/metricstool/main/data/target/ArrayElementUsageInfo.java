package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * 配列要素の使用を表すクラス
 * 
 * @author higo
 * 
 */
public class ArrayElementUsageInfo extends EntityUsageInfo {

    /**
     * 要素の親，つまり配列型のエンティティ使用とインデックスを与えて，オブジェクトを初期化
     * 
     * @param ownerEntityUsage 配列型のエンティティ使用
     * @param indexExpression インデックス
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public ArrayElementUsageInfo(final EntityUsageInfo ownerEntityUsage,
            final ExpressionInfo indexExpression, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        if (null == ownerEntityUsage) {
            throw new NullPointerException();
        }

        this.ownerEntityUsage = ownerEntityUsage;
        this.indexExpression = indexExpression;
    }

    /**
     * この配列要素の使用の型を返す
     * 
     * @return この配列要素の使用の型
     */
    @Override
    public TypeInfo getType() {

        final TypeInfo ownerType = this.getOwnerEntityUsage().getType();

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
     * この要素の親，つまり配列型のエンティティ使用を返す
     * 
     * @return この要素の親を返す
     */
    public EntityUsageInfo getOwnerEntityUsage() {
        return this.ownerEntityUsage;
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
        return this.getOwnerEntityUsage().getVariableUsages();
    }

    private final EntityUsageInfo ownerEntityUsage;

    private final ExpressionInfo indexExpression;
}
