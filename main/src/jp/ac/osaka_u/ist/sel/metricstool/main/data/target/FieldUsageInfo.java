package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * フィールドの使用を表すクラス
 * 
 * @author y-higo
 * 
 */
public final class FieldUsageInfo extends EntityUsageInfo {

    /**
     * 使用されているフィールドを与えてオブジェクトを初期化
     * 
     * @param usedField 使用されているフィールド
     * @param reference 参照である場合は true, 代入である場合は false
     */
    public FieldUsageInfo(final FieldInfo usedField, final boolean reference) {

        super();

        if (null == usedField) {
            throw new NullPointerException();
        }

        this.usedField = usedField;
        this.reference = reference;
    }

    @Override
    public TypeInfo getType() {
        return this.getUsedField().getType();
    }

    public FieldInfo getUsedField() {
        return this.usedField;
    }

    /**
     * このフィールド使用が参照であるかどうかを返す
     * 
     * @return 参照である場合は true，代入の場合は false
     */
    public boolean isReference() {
        return this.reference;
    }

    /**
     * このフィールド使用が代入であるかどうかを返す
     * 
     * @return 代入である場合は true，参照である場合は false
     */
    public boolean isAssignment() {
        return !this.reference;
    }

    private final FieldInfo usedField;

    private final boolean reference;
}
