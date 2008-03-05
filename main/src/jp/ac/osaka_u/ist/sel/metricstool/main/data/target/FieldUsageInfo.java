package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


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
     * @param usedField 使用されているフィールド
     * @param reference 参照である場合は true, 代入である場合は false
     */
    public FieldUsageInfo(final TypeInfo ownerType, final FieldInfo usedField,
            final boolean reference, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super(usedField, reference, fromLine, fromColumn, toLine, toColumn);

        this.ownerType = ownerType;
    }

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

    public final TypeInfo getOwnerType() {
        return this.ownerType;
    }

    private final TypeInfo ownerType;
}
