package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public abstract class UnresolvedCallableUnitInfo<T extends CallableUnitInfo> extends
        UnresolvedLocalSpaceInfo<T> implements VisualizableSetting {

    UnresolvedCallableUnitInfo() {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();

        this.modifiers = new HashSet<ModifierInfo>();
        this.typeParameters = new LinkedList<UnresolvedTypeParameterInfo>();
        this.parameters = new LinkedList<UnresolvedParameterInfo>();

        this.privateVisible = false;
        this.inheritanceVisible = false;
        this.namespaceVisible = false;
        this.publicVisible = false;
    }

    UnresolvedCallableUnitInfo(final UnresolvedClassInfo ownerClass) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == ownerClass) {
            throw new NullPointerException();
        }

        this.ownerClass = ownerClass;

        this.modifiers = new HashSet<ModifierInfo>();
        this.typeParameters = new LinkedList<UnresolvedTypeParameterInfo>();
        this.parameters = new LinkedList<UnresolvedParameterInfo>();

        this.privateVisible = false;
        this.inheritanceVisible = false;
        this.namespaceVisible = false;
        this.publicVisible = false;
    }

    /**
     * コンストラクタに引数を追加する
     * 
     * @param parameterInfo 追加する引数
     */
    public final void addParameter(final UnresolvedParameterInfo parameterInfo) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == parameterInfo) {
            throw new NullPointerException();
        }

        this.parameters.add(parameterInfo);
    }

    /**
     * 修飾子の Set を返す
     * 
     * @return 修飾子の Set
     */
    public final Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * 修飾子を追加する
     * 
     * @param modifier 追加する修飾子
     */
    public final void addModifier(final ModifierInfo modifier) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == modifier) {
            throw new NullPointerException();
        }

        this.modifiers.add(modifier);
    }

    /**
     * コンストラクタの引数のリストを返す
     * 
     * @return メソッドの引数のリスト
     */
    public final List<UnresolvedParameterInfo> getParameters() {
        return Collections.unmodifiableList(this.parameters);
    }

    /**
     * 未解決型パラメータの List を返す
     * 
     * @return 未解決型パラメータの List
     */
    public final List<UnresolvedTypeParameterInfo> getTypeParameters() {
        return Collections.unmodifiableList(this.typeParameters);
    }

    /**
     * 未解決型パラメータを追加する
     * 
     * @param typeParameter 追加する未解決型パラメータ
     */
    public final void addTypeParameter(final UnresolvedTypeParameterInfo typeParameter) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameter) {
            throw new NullPointerException();
        }

        this.typeParameters.add(typeParameter);
    }

    /**
     * このメソッドを定義しているクラスを返す
     * 
     * @return このメソッドを定義しているクラス
     */
    public final UnresolvedClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * メソッドを定義しているクラスをセットする
     * 
     * @param ownerClass メソッドを定義しているクラス
     */
    public final void setOwnerClass(final UnresolvedClassInfo ownerClass) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
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
    public final void setInheritanceVisible(final boolean inheritanceVisible) {
        this.inheritanceVisible = inheritanceVisible;
    }

    /**
     * 同じ名前空間内から参照可能かどうかを設定する
     * 
     * @param namespaceVisible 同じ名前空間から参照可能な場合は true，そうでない場合は false
     */
    public final void setNamespaceVisible(final boolean namespaceVisible) {
        this.namespaceVisible = namespaceVisible;
    }

    /**
     * クラス内からのみ参照可能かどうかを設定する
     * 
     * @param privateVisible クラス内からのみ参照可能な場合は true，そうでない場合は false
     */
    public final void setPrivateVibible(final boolean privateVisible) {
        this.privateVisible = privateVisible;
    }

    /**
     * どこからでも参照可能かどうかを設定する
     * 
     * @param publicVisible どこからでも参照可能な場合は true，そうでない場合は false
     */
    public final void setPublicVisible(final boolean publicVisible) {
        this.publicVisible = publicVisible;
    }

    /**
     * 子クラスから参照可能かどうかを返す
     * 
     * @return 子クラスから参照可能な場合は true, そうでない場合は false
     */
    public final boolean isInheritanceVisible() {
        return this.inheritanceVisible;
    }

    /**
     * 同じ名前空間から参照可能かどうかを返す
     * 
     * @return 同じ名前空間から参照可能な場合は true, そうでない場合は false
     */
    public final boolean isNamespaceVisible() {
        return this.namespaceVisible;
    }

    /**
     * クラス内からのみ参照可能かどうかを返す
     * 
     * @return クラス内からのみ参照可能な場合は true, そうでない場合は false
     */
    public final boolean isPrivateVisible() {
        return this.privateVisible;
    }

    /**
     * どこからでも参照可能かどうかを返す
     * 
     * @return どこからでも参照可能な場合は true, そうでない場合は false
     */
    public final boolean isPublicVisible() {
        return this.publicVisible;
    }

    /**
     * このメソッドを定義しているクラスを保存するための変数
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
     * 未解決型パラメータ名を保存するための変数
     */
    private final List<UnresolvedTypeParameterInfo> typeParameters;

    /**
     * コンストラクタ引数を保存するための変数
     */
    private final List<UnresolvedParameterInfo> parameters;

    /**
     * 修飾子を保存する
     */
    private Set<ModifierInfo> modifiers;

}
