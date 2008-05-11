package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * フィールドの使用を表すクラス
 * 
 * @author higo
 * 
 */
public class FieldUsageInfo extends VariableUsageInfo<FieldInfo> {

    /**
     * 使用されているフィールドを与えてオブジェクトを初期化
     * 
     * @param ownerUsage フィールド使用が実行される親エンティティ
     * @param usedField 使用されているフィールド
     * @param reference 参照である場合は true, 代入である場合は false
     */
    public FieldUsageInfo(final EntityUsageInfo ownerUsage, final TypeInfo ownerType,
            final FieldInfo usedField, final boolean reference, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(usedField, reference, fromLine, fromColumn, toLine, toColumn);

        this.ownerUsage = ownerUsage;
        this.ownerType = ownerType;
    }

    /**
     * このフィールド使用の型を返す
     * 
     * @return このフィールド使用の型
     */
    @Override
    public TypeInfo getType() {

        final VariableInfo<?, ?> usedVariable = this.getUsedVariable();
        final TypeInfo definitionType = usedVariable.getType();

        // 定義の返り値が型パラメータでなければそのまま返せる
        if (!(definitionType instanceof TypeParameterInfo)) {
            return definitionType;
        }

        //　準備
        final int typeParameterIndex = ((TypeParameterInfo) definitionType).getIndex();
        final ClassTypeInfo callOwnerType = (ClassTypeInfo) this.getOwnerType();
        final TypeInfo typeArgument = callOwnerType.getTypeArgument(typeParameterIndex);
        return typeArgument;
    }

    /**
     * このフィールド使用の親，つまりこのフィールド使用がくっついている要素を返す
     * 
     * @return このフィールド使用の親
     */
    public final TypeInfo getOwnerType() {
        return this.ownerType;
    }

    /**
     * フィールド使用が実行される親エンティティを返す
     * @return フィールド使用が実行される親エンティティ
     */
    public final EntityUsageInfo getOwnerUsage() {
        return this.ownerUsage;
    }

    /**
     * この式（フィールド使用）における変数利用の一覧を返す
     * 
     * @return 変数利用のSet
     */
    @Override
    public SortedSet<VariableUsageInfo<?>> getVariableUsages() {
        final SortedSet<VariableUsageInfo<?>> variableUsages = new TreeSet<VariableUsageInfo<?>>(
                super.getVariableUsages());
        variableUsages.addAll(getOwnerUsage().getVariableUsages());
        return Collections.unmodifiableSortedSet(variableUsages);
    }

    private final TypeInfo ownerType;

    /**
     * フィールド参照が実行される親エンティティを保存する変数
     */
    private final EntityUsageInfo ownerUsage;

}
