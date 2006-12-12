package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * 対象クラスに定義されているフィールドの情報を現すクラス．
 * 
 * @author y-higo
 */
public final class TargetFieldInfo extends FieldInfo {

    /**
     * フィールド情報オブジェクトを初期化
     * 
     * @param modidiers 修飾子名の Set
     * @param name 名前
     * @param type 型
     * @param ownerClass このフィールドを定義しているクラス
     */
    public TargetFieldInfo(final Set<ModifierInfo> modifiers, final String name,
            final TypeInfo type, final ClassInfo ownerClass) {

        super(name, type, ownerClass);

        this.modifiers = new HashSet<ModifierInfo>();
        this.modifiers.addAll(modifiers);
    }

    /**
     * 修飾子の Set を返す
     * 
     * @return 修飾子の Set
     */
    public Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * 修飾子を保存するための変数
     */
    private final Set<ModifierInfo> modifiers;
}
