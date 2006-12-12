package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * 対象クラスに定義されているフィールドの情報を現すクラス．
 * 
 * @author y-higo
 */
public final class TargetFieldInfo extends FieldInfo {

    /**
     * フィールド情報オブジェクトを初期化
     * 
     * @param modifier 修飾子
     * @param name 名前
     * @param type 型
     * @param ownerClass このフィールドを定義しているクラス
     */
    public TargetFieldInfo(final ModifierInfo modifier, final String name, final TypeInfo type,
            final ClassInfo ownerClass) {

        super(name, type, ownerClass);

        if (null == modifier) {
            throw new NullPointerException();
        }

        this.modifier = modifier;
    }

    /**
     * 修飾子を返す
     * 
     * @return 修飾子
     */
    public ModifierInfo getModifier() {
        return this.modifier;
    }

    /**
     * 修飾子を保存するための変数
     */
    private final ModifierInfo modifier;
}
