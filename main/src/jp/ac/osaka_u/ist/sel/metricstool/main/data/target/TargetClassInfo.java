package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * クラスの情報を保有するクラス．以下の情報を持つ．
 * <ul>
 * <li>クラス名</li>
 * <li>修飾子</li>
 * <li>名前空間（パッケージ名）</li>
 * <li>行数</li>
 * <li>継承しているクラス</li>
 * <li>継承されているクラス</li>
 * <li>参照しているクラス</li>
 * <li>参照されているクラス</li>
 * <li>内部クラス</li>
 * <li>このクラス内で定義されているメソッド</li>
 * <li>このクラス内で定義されているフィールド</li>
 * </ul>
 * 
 * @author higo
 * 
 */
public class TargetClassInfo extends ClassInfo implements Visualizable, Member {

    /**
     * 名前空間名，クラス名を与えてクラス情報オブジェクトを初期化
     * 
     * @param modifiers 修飾子の Set
     * @param namespace 名前空間名
     * @param className クラス名
     * @param privateVisible クラス内からのみ参照可能
     * @param namespaceVisible 同じ名前空間から参照可能
     * @param inheritanceVisible 子クラスから参照可能
     * @param publicVisible どこからでも参照可能
     * @param instance インスタンスメンバーかどうか
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public TargetClassInfo(final Set<ModifierInfo> modifiers, final NamespaceInfo namespace,
            final String className, final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final boolean instance,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(modifiers, namespace, className, fromLine, fromColumn, toLine, toColumn);

        if (null == modifiers) {
            throw new NullPointerException();
        }

        this.typeParameters = new LinkedList<TypeParameterInfo>();
        this.innerClasses = new TreeSet<TargetInnerClassInfo>();
        this.definedMethods = new TreeSet<TargetMethodInfo>();
        this.definedConstructors = new TreeSet<TargetConstructorInfo>();
        this.definedFields = new TreeSet<TargetFieldInfo>();

        this.privateVisible = privateVisible;
        this.namespaceVisible = namespaceVisible;
        this.inheritanceVisible = inheritanceVisible;
        this.publicVisible = publicVisible;

        this.instance = instance;
    }

    /**
     * 完全限定名を与えて，クラス情報オブジェクトを初期化
     * 
     * @param modifiers 修飾子の Set
     * @param fullQualifiedName 完全限定名
     * @param loc 行数
     * @param privateVisible クラス内からのみ参照可能
     * @param namespaceVisible 同じ名前空間から参照可能
     * @param inheritanceVisible 子クラスから参照可能
     * @param publicVisible どこからでも参照可能
     * @param instance インスタンスメンバーかどうか
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public TargetClassInfo(final Set<ModifierInfo> modifiers, final String[] fullQualifiedName,
            final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final boolean instance,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(modifiers, fullQualifiedName, fromLine, fromColumn, toLine, toColumn);

        if (null == modifiers) {
            throw new NullPointerException();
        }

        this.typeParameters = new LinkedList<TypeParameterInfo>();
        this.innerClasses = new TreeSet<TargetInnerClassInfo>();
        this.definedMethods = new TreeSet<TargetMethodInfo>();
        this.definedConstructors = new TreeSet<TargetConstructorInfo>();
        this.definedFields = new TreeSet<TargetFieldInfo>();

        this.privateVisible = privateVisible;
        this.namespaceVisible = namespaceVisible;
        this.inheritanceVisible = inheritanceVisible;
        this.publicVisible = publicVisible;

        this.instance = instance;
    }

    /**
     * このクラスにインナークラスを追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param innerClass 追加するインナークラス
     */
    public final void addInnerClass(final TargetInnerClassInfo innerClass) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == innerClass) {
            throw new NullPointerException();
        }

        this.innerClasses.add(innerClass);
    }

    /**
     * このクラスのインナークラスの SortedSet を返す．
     * 
     * @return インナークラスの SortedSet
     */
    public final SortedSet<TargetInnerClassInfo> getInnerClasses() {
        return Collections.unmodifiableSortedSet(this.innerClasses);
    }

    /**
     * このクラスに定義されたメソッド情報を追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param definedMethod 追加する定義されたメソッド
     */
    public final void addDefinedMethod(final TargetMethodInfo definedMethod) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedMethod) {
            throw new NullPointerException();
        }

        this.definedMethods.add(definedMethod);
    }

    /**
     * このクラスに定義されたコンストラクタ情報を追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param definedConstructor 追加する定義されたコンストラクタ
     */
    public final void addDefinedConstructor(final TargetConstructorInfo definedConstructor) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedConstructor) {
            throw new NullPointerException();
        }

        this.definedConstructors.add(definedConstructor);
    }

    /**
     * 引数で指定された型パラメータを追加する
     * 
     * @param typeParameter 追加する型パラメータ
     */
    public final void addTypeParameter(final TypeParameterInfo typeParameter) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameter) {
            throw new NullPointerException();
        }

        this.typeParameters.add(typeParameter);
    }

    /**
     * このクラスの型パラメータの List を返す．
     * 
     * @return このクラスの型パラメータの List
     */
    public final List<TypeParameterInfo> getTypeParameters() {
        return Collections.unmodifiableList(this.typeParameters);
    }

    /**
     * 指定されたインデックスの型パラメータを返す
     * 
     * @param index 型パラメータのインデックス
     * @return　指定されたインデックスの型パラメータ
     */
    public final TypeParameterInfo getIndex(final int index) {
        return this.typeParameters.get(index);
    }

    /**
     * このクラスに定義されたフィールド情報を追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param definedField 追加する定義されたフィールド
     */
    public final void addDefinedField(final TargetFieldInfo definedField) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedField) {
            throw new NullPointerException();
        }

        this.definedFields.add(definedField);
    }

    /**
     * このクラスに定義されているメソッドの SortedSet を返す．
     * 
     * @return 定義されているメソッドの SortedSet
     */
    public final SortedSet<TargetMethodInfo> getDefinedMethods() {
        return Collections.unmodifiableSortedSet(this.definedMethods);
    }

    /**
     * このクラスに定義されているコンストラクタの SortedSet を返す．
     * 
     * @return 定義されているメソッドの SortedSet
     */
    public final SortedSet<TargetConstructorInfo> getDefinedConstructors() {
        return Collections.unmodifiableSortedSet(this.definedConstructors);
    }

    /**
     * このクラスに定義されているフィールドの SortedSet を返す．
     * 
     * @return 定義されているフィールドの SortedSet
     */
    public final SortedSet<TargetFieldInfo> getDefinedFields() {
        return Collections.unmodifiableSortedSet(this.definedFields);
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
     * インスタンスメンバーかどうかを返す
     * 
     * @return インスタンスメンバーの場合 true，そうでない場合 false
     */
    public final boolean isInstanceMember() {
        return this.instance;
    }

    /**
     * スタティックメンバーかどうかを返す
     * 
     * @return スタティックメンバーの場合 true，そうでない場合 false
     */
    public final boolean isStaticMember() {
        return !this.instance;
    }

    /**
     * 型パラメータを保存する変数
     */
    private final List<TypeParameterInfo> typeParameters;

    /**
     * このクラスの内部クラス一覧を保存するための変数．直接の内部クラスのみを保有する．
     */
    private final SortedSet<TargetInnerClassInfo> innerClasses;

    /**
     * このクラスで定義されているメソッド一覧を保存するための変数．
     */
    private final SortedSet<TargetMethodInfo> definedMethods;

    /**
     * このクラスで定義されているコンストラクタ一覧を保存するための変数．
     */
    private final SortedSet<TargetConstructorInfo> definedConstructors;

    /**
     * このクラスで定義されているフィールド一覧を保存するための変数．
     */
    private final SortedSet<TargetFieldInfo> definedFields;

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

    /**
     * インスタンスメンバーかどうかを保存するための変数
     */
    private final boolean instance;
}
