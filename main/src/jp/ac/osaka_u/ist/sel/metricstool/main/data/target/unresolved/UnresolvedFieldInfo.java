package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;


/**
 * ASTパースで取得したフィールド情報を一時的に格納するためのクラス．
 * 
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedFieldInfo extends UnresolvedVariableInfo implements
        VisualizableSetting, MemberSetting {

    /**
     * Unresolvedフィールドオブジェクトを初期化する． フィールド名と型，定義しているクラスが与えられなければならない．
     * 
     * @param name フィールド名
     * @param type フィールドの型
     * @param ownerClass フィールドを定義しているクラス
     */
    public UnresolvedFieldInfo(final String name, final UnresolvedTypeInfo type,
            final UnresolvedClassInfo ownerClass) {

        super(name, type);

        if (null == ownerClass) {
            throw new NullPointerException();
        }

        this.ownerClass = ownerClass;
        this.modifiers = new HashSet<ModifierInfo>();

        this.privateVisible = false;
        this.inheritanceVisible = false;
        this.namespaceVisible = false;
        this.publicVisible = false;
        
        this.instance = true;
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
     * 修飾子を追加する
     * 
     * @param modifier 追加する修飾子
     */
    public void addModifiar(final ModifierInfo modifier) {

        if (null == modifier) {
            throw new NullPointerException();
        }

        this.modifiers.add(modifier);
    }

    /**
     * このフィールドを定義している未解決クラス情報を返す
     * 
     * @return このフィールドを定義している未解決クラス情報
     */
    public UnresolvedClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * このフィールドを定義している未解決クラス情報をセットする
     * 
     * @param ownerClass このフィールドを定義している未解決クラス情報
     */
    public void setOwnerClass(final UnresolvedClassInfo ownerClass) {

        if (null == ownerClass) {
            throw new NullPointerException();
        }

        this.ownerClass = ownerClass;
    }

    /**
     * 子クラスから参照可能かどうかを設定する
     * 
     * @param inheritanceVisible 子クラスから参照可能な場合は true，そうでない場合は false
     */
    public void setInheritanceVisible(final boolean inheritanceVisible) {
        this.inheritanceVisible = inheritanceVisible;
    }

    /**
     * 同じ名前空間内から参照可能かどうかを設定する
     * 
     * @param namespaceVisible 同じ名前空間から参照可能な場合は true，そうでない場合は false
     */
    public void setNamespaceVisible(final boolean namespaceVisible) {
        this.namespaceVisible = namespaceVisible;
    }

    /**
     * クラス内からのみ参照可能かどうかを設定する
     * 
     * @param privateVisible クラス内からのみ参照可能な場合は true，そうでない場合は false
     */
    public void setPrivateVibible(final boolean privateVisible) {
        this.privateVisible = privateVisible;
    }

    /**
     * どこからでも参照可能かどうかを設定する
     * 
     * @param publicVisible どこからでも参照可能な場合は true，そうでない場合は false
     */
    public void setPublicVisible(final boolean publicVisible) {
        this.publicVisible = publicVisible;
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
     * インスタンスメンバーかどうかを返す
     * 
     * @return インスタンスメンバーの場合 true，そうでない場合 false
     */
    public boolean isInstanceMember() {
        return this.instance;
    }

    /**
     * スタティックメンバーかどうかを返す
     * 
     * @return スタティックメンバーの場合 true，そうでない場合 false
     */
    public boolean isStaticMember() {
        return !this.instance;
    }

    /**
     * インスタンスメンバーかどうかをセットする
     * 
     * @param instance インスタンスメンバーの場合は true， スタティックメンバーの場合は false
     */
    public void setInstanceMember(final boolean instance) {
        this.instance = instance;
    }

    /**
     * このフィールドの修飾子を保存するための変数
     */
    private Set<ModifierInfo> modifiers;

    /**
     * このフィールドを定義しているクラスを保存するための変数
     */
    private UnresolvedClassInfo ownerClass;

    /**
     * クラス内からのみ参照可能かどうか保存するための変数
     */
    private boolean privateVisible;

    /**
     * 同じ名前空間から参照可能かどうか保存するための変数
     */
    private boolean namespaceVisible;

    /**
     * 子クラスから参照可能かどうか保存するための変数
     */
    private boolean inheritanceVisible;

    /**
     * どこからでも参照可能かどうか保存するための変数
     */
    private boolean publicVisible;

    /**
     * インスタンスメンバーかどうかを保存するための変数
     */
    private boolean instance;
}
