package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collection;
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
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public class TargetClassInfo extends
        ClassInfo<TargetFieldInfo, TargetMethodInfo, TargetConstructorInfo, TargetInnerClassInfo>
        implements Visualizable, StaticOrInstance {

    /**
     * 指定されたクラスに含まれる全てのインナークラスを返す
     * 
     * @param classInfo 指定するクラス
     * @return　指定されたクラスに含まれる全てのインナークラス
     */
    static public Collection<TargetInnerClassInfo> getAllInnerClasses(
            final TargetClassInfo classInfo) {

        if (null == classInfo) {
            throw new IllegalArgumentException();
        }

        final SortedSet<TargetInnerClassInfo> innerClassInfos = new TreeSet<TargetInnerClassInfo>();
        for (final TargetInnerClassInfo innerClassInfo : classInfo.getInnerClasses()) {

            innerClassInfos.add(innerClassInfo);
            innerClassInfos.addAll(getAllInnerClasses(innerClassInfo));
        }

        return Collections.unmodifiableSortedSet(innerClassInfos);
    }

    /**
     * 指定したクラスにおいてアクセス可能なインナークラス一覧を返す．
     * アクセス可能なクラスとは，指定されたクラス，もしくはその親クラス内に定義されたクラスある．
     * 
     * @param classInfo 指定されたクラス
     * @return 指定したクラスにおいてアクセス可能なインナークラス一覧を返す．
     */
    static public Collection<TargetInnerClassInfo> getAccessibleInnerClasses(
            final TargetClassInfo classInfo) {

        if (null == classInfo) {
            throw new IllegalArgumentException();
        }

        final Set<TargetClassInfo> classCache = new HashSet<TargetClassInfo>();

        return Collections.unmodifiableCollection(getAccessibleInnerClasses(classInfo, classCache));
    }

    static private Collection<TargetInnerClassInfo> getAccessibleInnerClasses(
            final TargetClassInfo classInfo, final Set<TargetClassInfo> classCache) {

        if ((null == classInfo) || (null == classCache)) {
            throw new IllegalArgumentException();
        }

        if (classCache.contains(classInfo)) {
            return Collections.unmodifiableCollection(new TreeSet<TargetInnerClassInfo>());
        }

        classCache.add(classInfo);

        final SortedSet<TargetInnerClassInfo> innerClassInfos = new TreeSet<TargetInnerClassInfo>();

        for (final TargetInnerClassInfo innerClassInfo : classInfo.getInnerClasses()) {
            innerClassInfos.add(innerClassInfo);
            innerClassInfos.addAll(getAccessibleInnerClasses(innerClassInfo, classCache));
        }

        for (final ClassInfo<?, ?, ?, ?> superClassInfo : ClassTypeInfo.convert(classInfo
                .getSuperClasses())) {
            if (superClassInfo instanceof TargetClassInfo) {
                if (superClassInfo instanceof TargetInnerClassInfo) {
                    innerClassInfos.add((TargetInnerClassInfo) superClassInfo);
                }
                innerClassInfos.addAll(getAccessibleInnerClasses((TargetClassInfo) superClassInfo,
                        classCache));
            }
        }

        return Collections.unmodifiableCollection(innerClassInfos);
    }

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
     * @param isInterface インターフェースかどうか
     * @param fileInfo このクラスを宣言しているファイル情報
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public TargetClassInfo(final Set<ModifierInfo> modifiers, final NamespaceInfo namespace,
            final String className, final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final boolean instance,
            final boolean isInterface, final FileInfo fileInfo, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(modifiers, namespace, className, fromLine, fromColumn, toLine, toColumn);

        if (null == modifiers) {
            throw new NullPointerException();
        }

        this.implicitInstanceInitializer = new InstanceInitializerInfo(this, 0, 0, 0, 0);
        this.implicitStaticInitializer = new StaticInitializerInfo(this, 0, 0, 0, 0);
        this.instanceInitializers = new TreeSet<InstanceInitializerInfo>();
        this.instanceInitializers.add(this.implicitInstanceInitializer);
        this.staticInitializers = new TreeSet<StaticInitializerInfo>();
        this.staticInitializers.add(this.implicitStaticInitializer);
        this.accessibleClasses = new TreeSet<ClassInfo<?, ?, ?, ?>>();

        this.privateVisible = privateVisible;
        this.namespaceVisible = namespaceVisible;
        this.inheritanceVisible = inheritanceVisible;
        this.publicVisible = publicVisible;
        this.isInterface = isInterface;

        this.instance = instance;

        this.ownerFile = fileInfo;
    }

    /**
     * 完全限定名を与えて，クラス情報オブジェクトを初期化
     * 
     * @param modifiers 修飾子の Set
     * @param fullQualifiedName 完全限定名
     * @param privateVisible クラス内からのみ参照可能
     * @param namespaceVisible 同じ名前空間から参照可能
     * @param inheritanceVisible 子クラスから参照可能
     * @param publicVisible どこからでも参照可能
     * @param instance インスタンスメンバーかどうか
     * @param isInterface インタフェースであるかどうか
     * @param fileInfo このクラスを宣言しているファイル情報
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public TargetClassInfo(final Set<ModifierInfo> modifiers, final String[] fullQualifiedName,
            final boolean privateVisible, final boolean namespaceVisible,
            final boolean inheritanceVisible, final boolean publicVisible, final boolean instance,
            final boolean isInterface, final FileInfo fileInfo, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(modifiers, fullQualifiedName, fromLine, fromColumn, toLine, toColumn);

        if (null == modifiers || null == fileInfo) {
            throw new NullPointerException();
        }

        this.implicitInstanceInitializer = new InstanceInitializerInfo(this, 0, 0, 0, 0);
        this.implicitStaticInitializer = new StaticInitializerInfo(this, 0, 0, 0, 0);
        this.instanceInitializers = new TreeSet<InstanceInitializerInfo>();
        this.instanceInitializers.add(this.implicitInstanceInitializer);
        this.staticInitializers = new TreeSet<StaticInitializerInfo>();
        this.staticInitializers.add(this.implicitStaticInitializer);
        this.accessibleClasses = new TreeSet<ClassInfo<?, ?, ?, ?>>();

        this.privateVisible = privateVisible;
        this.namespaceVisible = namespaceVisible;
        this.inheritanceVisible = inheritanceVisible;
        this.publicVisible = publicVisible;
        this.isInterface = isInterface;

        this.instance = instance;

        this.ownerFile = fileInfo;
    }

    /**
     * インスタンスイニシャライザーを追加する
     * 
     * @param instanceInitializer 追加されるインスタンスイニシャライザー
     */
    public final void addInstanceInitializer(final InstanceInitializerInfo instanceInitializer) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == instanceInitializer) {
            throw new NullPointerException();
        }

        this.instanceInitializers.add(instanceInitializer);
    }

    /**
     * スタティックイニシャライザーを追加する
     * 
     * @param staticInitializer 追加されるスタティックイニシャライザー
     */
    public final void addStaticInitializer(final StaticInitializerInfo staticInitializer) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == staticInitializer) {
            throw new NullPointerException();
        }

        this.staticInitializers.add(staticInitializer);
    }

    /**
     * このクラスにおいてアクセス可能なクラスを追加する．プラグインから呼ぶとランタイムエラー.
     * 
     * @param accessibleClass アクセス可能なクラス
     */
    public final void addAccessibleClass(final ClassInfo<?, ?, ?, ?> accessibleClass) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == accessibleClass) {
            throw new IllegalArgumentException();
        }

        this.accessibleClasses.add(accessibleClass);
    }

    /**
     * このクラスにおいてアクセス可能なクラス群を追加する．プラグインから呼ぶとランタイムエラー
     * 
     * @param accessibleClasses アクセス可能なクラス群
     */
    public final void addaccessibleClasses(final Set<ClassInfo<?, ?, ?, ?>> accessibleClasses) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == accessibleClasses) {
            throw new IllegalArgumentException();
        }

        this.accessibleClasses.addAll(accessibleClasses);
    }

    /**
     * このクラスの暗黙のインスタンスイニシャライザを返す
     * @return 暗黙のインスタンスイニシャライザ
     */
    public InstanceInitializerInfo getImplicitInstanceInitializer() {
        return this.implicitInstanceInitializer;
    }

    /**
     * このクラスのインスタンスイニシャライザ一覧を返す
     * @return このクラスのインスタンスイニシャライザ一覧
     */
    public SortedSet<InstanceInitializerInfo> getInstanceInitializers() {
        return this.instanceInitializers;
    }

    /**
     * このクラスの暗黙のスタティックイニシャライザを返す
     * @return 暗黙のスタティックイニシャライザ
     */
    public StaticInitializerInfo getImplicitStaticInitializer() {
        return this.implicitStaticInitializer;
    }

    /**
     * このクラスのスタティックイニシャライザ一覧を返す
     * @return スタティックイニシャライザ一覧
     */
    public SortedSet<StaticInitializerInfo> getStaticInitializers() {
        return this.staticInitializers;
    }

    /**
     * このクラスにおいてアクセス可能なクラスのSortedSetを返す．
     * 
     * @return このクラスにおいてアクセス可能なクラスのSortedSet
     */
    public final Set<ClassInfo<?, ?, ?, ?>> getAccessibleClasses() {
        return Collections.unmodifiableSet(this.accessibleClasses);
    }

    /**
     * このクラス内における変数使用のSetを返す
     * 
     * @return このクラス内における変数使用のSet
     */
    @Override
    public final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {

        final Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> variableUsages = new HashSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>();

        // メソッド内で使用されている変数を追加
        for (final TargetMethodInfo definedMethod : this.getDefinedMethods()) {
            variableUsages.addAll(definedMethod.getVariableUsages());
        }

        // コンストラクタ内で使用されている変数を追加
        for (final TargetConstructorInfo definedConstructor : this.getDefinedConstructors()) {
            variableUsages.addAll(definedConstructor.getVariableUsages());
        }

        // 内部クラスで使用されている変数を追加
        for (final TargetInnerClassInfo innerClass : this.getInnerClasses()) {
            variableUsages.addAll(innerClass.getVariableUsages());
        }

        return Collections.unmodifiableSet(variableUsages);
    }

    /**
     * このクラス内で定義されている変数のSetを返す
     * 
     * @return このクラス内で定義されている変数のSet
     */
    @Override
    public final Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {

        final Set<VariableInfo<? extends UnitInfo>> definedVariables = new HashSet<VariableInfo<? extends UnitInfo>>();

        // 定義されているフィールドを追加
        definedVariables.addAll(this.getDefinedFields());

        // メソッド内で定義されている変数を追加
        for (final TargetMethodInfo definedMethod : this.getDefinedMethods()) {
            definedVariables.addAll(definedMethod.getDefinedVariables());
        }

        // コンストラクタ内で定義されている変数を追加
        for (final TargetConstructorInfo definedConstructor : this.getDefinedConstructors()) {
            definedVariables.addAll(definedConstructor.getDefinedVariables());
        }

        // 内部クラスで定義されている変数を追加
        for (final TargetInnerClassInfo innerClass : this.getInnerClasses()) {
            definedVariables.addAll(innerClass.getDefinedVariables());
        }

        return Collections.unmodifiableSet(definedVariables);
    }

    /**
     * このクラス内における呼び出しのSetを返す
     * 
     * @return このクラス内における呼び出しのSet
     */
    @Override
    public final Set<CallInfo<? extends CallableUnitInfo>> getCalls() {

        final Set<CallInfo<? extends CallableUnitInfo>> calls = new HashSet<CallInfo<? extends CallableUnitInfo>>();

        // メソッド内での呼び出しを追加
        for (final TargetMethodInfo definedMethod : this.getDefinedMethods()) {
            calls.addAll(definedMethod.getCalls());
        }

        // コンストラクタ内での呼び出しを追加
        for (final TargetConstructorInfo definedConstructor : this.getDefinedConstructors()) {
            calls.addAll(definedConstructor.getCalls());
        }

        // 内部クラスでの呼び出しを追加
        for (final TargetInnerClassInfo innerClass : this.getInnerClasses()) {
            calls.addAll(innerClass.getCalls());
        }

        return Collections.unmodifiableSet(calls);
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
     * インターフェースかどうか返す．
     * 
     * @return インターフェースの場合 true，クラスの場合 false
     */
    public final boolean isInterface() {
        return this.isInterface;
    }

    /**
     * クラスかどうか返す．
     * 
     *  @return クラスの場合 true，インターフェースの場合 false
     */
    public final boolean isClass() {
        return !this.isInterface;
    }

    /**
     * このクラスを宣言しているファイル情報を返す
     * 
     * @return このクラスを宣言しているファイル情報
     */
    public final FileInfo getOwnerFile() {
        return this.ownerFile;
    }

    /**
     * このクラスのスタティックイニシャライザ一覧を保存するための変数
     */
    private final SortedSet<StaticInitializerInfo> staticInitializers;

    /**
     * このクラスのインスタンスイニシャライザ一覧を保存するための変数
     */
    private final SortedSet<InstanceInitializerInfo> instanceInitializers;

    /**
     * このクラスの暗黙のインスタンスイニシャライザを保存するための変数
     */
    private final InstanceInitializerInfo implicitInstanceInitializer;

    /**
     * このクラスの暗黙のスタティックイニシャライザを保存するための変数
     */
    private final StaticInitializerInfo implicitStaticInitializer;

    /**
     * このクラス内からアクセス可能なクラス
     */
    private final Set<ClassInfo<?, ?, ?, ?>> accessibleClasses;

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

    /**
     * インターフェースであるかどうかを保存するための変数
     */
    private final boolean isInterface;

    /**
     * このクラスを宣言しているファイル情報を保存するための変数
     */
    private final FileInfo ownerFile;
}
