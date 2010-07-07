package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決の呼び出される単位のブロック（メソッドやコンストラクタ）を表すクラス
 * 
 * @author higo
 * @param <T> 解決済みの型
 */
public abstract class UnresolvedCallableUnitInfo<T extends CallableUnitInfo> extends
        UnresolvedLocalSpaceInfo<T> implements VisualizableSetting, StaticOrInstanceSetting,
        ModifierSetting {

    protected UnresolvedCallableUnitInfo(final UnresolvedClassInfo ownerClass) {

        super(ownerClass);

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();

        this.modifiers = new HashSet<ModifierInfo>();
        this.typeParameters = new LinkedList<UnresolvedTypeParameterInfo>();
        this.parameters = new LinkedList<UnresolvedParameterInfo>();
        this.thrownExceptions = new LinkedList<UnresolvedClassTypeInfo>();

        this.privateVisible = false;
        this.inheritanceVisible = false;
        this.namespaceVisible = false;
        this.publicVisible = false;
    }

    protected UnresolvedCallableUnitInfo(final UnresolvedClassInfo ownerClass, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        this(ownerClass);

        this.setFromLine(fromLine);
        this.setFromColumn(fromColumn);
        this.setToLine(toLine);
        this.setToColumn(toColumn);
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
     * 未解決例外の List を返す
     * 
     * @return 未解決例外の List
    */
    public final List<UnresolvedClassTypeInfo> getThrownExceptions() {
        return Collections.unmodifiableList(this.thrownExceptions);
    }

    /**
      * 未解決例外を追加する
      * 
      * @param typeParameter 追加する未解決例外
      */
    public final void addTypeParameter(final UnresolvedClassTypeInfo thrownException) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == thrownException) {
            throw new NullPointerException();
        }

        this.thrownExceptions.add(thrownException);
    }

    /**
     * このメソッドを定義しているクラスを返す
     * 
     * @return このメソッドを定義しているクラス
     */
    public final UnresolvedClassInfo getOwnerClass() {
        return this.getOuterClass();
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
     * 未解決型パラメータを解決する
     * すでにresolveメソッドが呼び出された状態で用いなければならない
     * 
     * @param classInfoManager
     * @return
     */
    public final T resolveTypeParameter(final ClassInfoManager classInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new IllegalArgumentException();
        }

        final T resolved = this.getResolved();
        final TargetClassInfo ownerClass = this.getOwnerClass().getResolved();

        for (final UnresolvedTypeParameterInfo unresolvedTypeParameter : this.getTypeParameters()) {

            final TypeParameterInfo typeParameter = unresolvedTypeParameter.getResolved();
            for (final UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> unresolvedExtendsType : unresolvedTypeParameter
                    .getExtendsTypes()) {
                final ReferenceTypeInfo extendsType = unresolvedExtendsType.resolve(ownerClass,
                        resolved, classInfoManager, null, null);
                typeParameter.addExtendsType(extendsType);
            }
        }

        return resolved;
    }

    /**
     * 未解決型パラメータを解決する
     * すでにresolveメソッドが呼び出された状態で用いなければならない
     * 
     * @param classInfoManager
     * @return
     */
    public final T resolveParameter(final ClassInfoManager classInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new IllegalArgumentException();
        }

        final T resolved = this.getResolved();
        final TargetClassInfo ownerClass = this.getOwnerClass().getResolved();

        for (final UnresolvedParameterInfo unresolvedParameter : this.getParameters()) {
            final TargetParameterInfo parameter = unresolvedParameter.resolve(ownerClass, resolved,
                    classInfoManager, null, null);
            resolved.addParameter(parameter);
        }

        return resolved;
    }

    /**
     * 未解決のスローされる例外情報を解決する
     * すでにresolveメソッドが呼び出された状態で用いなければならない
     * 
     * @param classInfoManager
     * @return
     */
    public final T resolveThrownException(final ClassInfoManager classInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new IllegalArgumentException();
        }

        final T resolved = this.getResolved();
        final TargetClassInfo ownerClass = this.getOwnerClass().getResolved();

        for (final UnresolvedClassTypeInfo unresolvedThrownException : this.getThrownExceptions()) {
            final ReferenceTypeInfo thrownException = unresolvedThrownException.resolve(ownerClass,
                    resolved, classInfoManager, null, null);
            this.resolvedInfo.addThrownException(thrownException);
        }

        return resolved;
    }

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
     * throwされる例外を保存するための変数
     */
    private final List<UnresolvedClassTypeInfo> thrownExceptions;

    /**
     * 修飾子を保存する
     */
    private Set<ModifierInfo> modifiers;

}
