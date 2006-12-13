package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * 対象クラスに定義されているフィールドの情報を現すクラス．
 * 
 * @author y-higo
 */
public final class TargetFieldInfo extends FieldInfo implements Visualizable {

    /**
     * フィールド情報オブジェクトを初期化
     * 
     * @param modidiers 修飾子名の Set
     * @param name 名前
     * @param type 型
     * @param ownerClass このフィールドを定義しているクラス
     * @param privateVisible クラス内からのみ参照可能
     * @param namespaceVisible 同じ名前空間から参照可能
     * @param inheritanceVisible 子クラスから参照可能
     * @param publicVisible どこからでも参照可能
     */
    public TargetFieldInfo(final Set<ModifierInfo> modifiers, final String name,
            final TypeInfo type, final ClassInfo ownerClass, final boolean privateVisible,
            final boolean namespaceVisible, final boolean inheritanceVisible,
            final boolean publicVisible) {

        super(name, type, ownerClass);

        this.modifiers = new HashSet<ModifierInfo>();
        this.modifiers.addAll(modifiers);
        
        this.privateVisible = privateVisible;
        this.namespaceVisible = namespaceVisible;
        this.inheritanceVisible = inheritanceVisible;
        this.publicVisible = publicVisible;
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
     * 子クラスから参照可能かどうかを返す
     * 
     * @return 子クラスから参照可能な場合は true, そうでない場合は false
     */
    public boolean isInheritanceVisible() {
        return this.privateVisible;
    }

    /**
     * 同じ名前空間から参照可能かどうかを返す
     * 
     * @return 同じ名前空間から参照可能な場合は true, そうでない場合は false
     */
    public boolean isNamespaceVisible() {
        return this.namespaceVisible;
    }

    /**
     * クラス内からのみ参照可能かどうかを返す
     * 
     * @return クラス内からのみ参照可能な場合は true, そうでない場合は false
     */
    public boolean isPrivateVisible() {
        return this.inheritanceVisible;
    }

    /**
     * どこからでも参照可能かどうかを返す
     * 
     * @return どこからでも参照可能な場合は true, そうでない場合は false
     */
    public boolean isPublicVisible() {
        return this.publicVisible;
    }

    /**
     * 修飾子を保存するための変数
     */
    private final Set<ModifierInfo> modifiers;

    /**
     * クラス内からのみ参照可能かどうか保存するための変数
     */
    private final boolean privateVisible;

    /**
     * 同じ名前空間から参照可能かどうか保存するための変数
     */
    private final boolean namespaceVisible;

    /**
     * 子クラスから参照可能かどうか保存するための変数
     */
    private final boolean inheritanceVisible;

    /**
     * どこからでも参照可能かどうか保存するための変数
     */
    private final boolean publicVisible;
}
