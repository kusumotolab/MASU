package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
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
 * @author y-higo
 * 
 */
public class TargetClassInfo extends ClassInfo implements Visualizable, Member {

    /**
     * 名前空間名，クラス名を与えて暮らす情報オブジェクトを初期化
     * 
     * @param modifiers 修飾子の Set
     * @param namespace 名前空間名
     * @param className クラス名
     * @param loc 行数
     * @param privateVisible クラス内からのみ参照可能
     * @param namespaceVisible 同じ名前空間から参照可能
     * @param inheritanceVisible 子クラスから参照可能
     * @param publicVisible どこからでも参照可能
     * @param instance インスタンスメンバーかどうか
     */
    public TargetClassInfo(final Set<ModifierInfo> modifiers, final NamespaceInfo namespace,
            final String className, final int loc, final boolean privateVisible,
            final boolean namespaceVisible, final boolean inheritanceVisible,
            final boolean publicVisible, final boolean instance) {

        super(namespace, className);

        if (null == modifiers) {
            throw new NullPointerException();
        }

        if (loc < 0) {
            throw new IllegalAccessError("LOC is must be 0 or more!");
        }

        this.loc = loc;
        this.modifiers = new HashSet<ModifierInfo>();
        this.innerClasses = new TreeSet<TargetInnerClassInfo>();
        this.definedMethods = new TreeSet<TargetMethodInfo>();
        this.definedFields = new TreeSet<TargetFieldInfo>();

        this.modifiers.addAll(modifiers);

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
     */
    public TargetClassInfo(final Set<ModifierInfo> modifiers, final String[] fullQualifiedName,
            final int loc, final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final boolean instance) {

        super(fullQualifiedName);

        if (null == modifiers) {
            throw new NullPointerException();
        }

        if (loc < 0) {
            throw new IllegalAccessError("LOC is must be 0 or more!");
        }

        this.loc = loc;
        this.modifiers = new HashSet<ModifierInfo>();
        this.innerClasses = new TreeSet<TargetInnerClassInfo>();
        this.definedMethods = new TreeSet<TargetMethodInfo>();
        this.definedFields = new TreeSet<TargetFieldInfo>();

        this.modifiers.addAll(modifiers);

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
    public void addInnerClass(final TargetInnerClassInfo innerClass) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == innerClass) {
            throw new NullPointerException();
        }

        this.innerClasses.add(innerClass);
    }

    /**
     * このクラスの行数を返す
     * 
     * @return このクラスの行数
     */
    public int getLOC() {
        return this.loc;
    }

    /**
     * このクラスの修飾子の Set を返す
     * 
     * @return このクラスの修飾子の Set
     */
    public Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * このクラスのインナークラスの SortedSet を返す．
     * 
     * @return インナークラスの SortedSet
     */
    public SortedSet<TargetInnerClassInfo> getInnerClasses() {
        return Collections.unmodifiableSortedSet(this.innerClasses);
    }

    /**
     * このクラスに定義されたメソッド情報を追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param definedMethod 追加する定義されたメソッド
     */
    public void addDefinedMethod(final TargetMethodInfo definedMethod) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedMethod) {
            throw new NullPointerException();
        }

        this.definedMethods.add(definedMethod);
    }

    /**
     * このクラスに定義されたフィールド情報を追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param definedField 追加する定義されたフィールド
     */
    public void addDefinedField(final TargetFieldInfo definedField) {

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
    public SortedSet<TargetMethodInfo> getDefinedMethods() {
        return Collections.unmodifiableSortedSet(this.definedMethods);
    }

    /**
     * このクラスに定義されているフィールドの SortedSet を返す．
     * 
     * @return 定義されているフィールドの SortedSet
     */
    public SortedSet<TargetFieldInfo> getDefinedFields() {
        return Collections.unmodifiableSortedSet(this.definedFields);
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
     * 行数を保存するための変数
     */
    private final int loc;

    /**
     * 修飾子を保存する変数
     */
    private final Set<ModifierInfo> modifiers;

    /**
     * このクラスの内部クラス一覧を保存するための変数．直接の内部クラスのみを保有する．
     */
    private final SortedSet<TargetInnerClassInfo> innerClasses;

    /**
     * このクラスで定義されているメソッド一覧を保存するための変数．
     */
    private final SortedSet<TargetMethodInfo> definedMethods;

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
