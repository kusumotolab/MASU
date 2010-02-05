package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricMeasurable;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * クラス情報を格納するための抽象クラス
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public abstract class ClassInfo<F extends FieldInfo, M extends MethodInfo, C extends ConstructorInfo, I extends InnerClassInfo<?>>
        extends UnitInfo implements MetricMeasurable, Modifier, TypeParameterizable {

    /**
     * 名前空間名とクラス名からオブジェクトを生成する
     * 
     * @param modifiers 修飾子のSet
     * @param namespace 名前空間名
     * @param className クラス名
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    ClassInfo(final Set<ModifierInfo> modifiers, final NamespaceInfo namespace,
            final String className, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == namespace) || (null == className)) {
            throw new NullPointerException();
        }

        this.definedMethods = new TreeSet<M>();
        this.definedConstructors = new TreeSet<C>();
        this.definedFields = new TreeSet<F>();
        this.innerClasses = new TreeSet<I>();

        this.namespace = namespace;
        this.className = className;
        this.superClasses = new LinkedList<ClassTypeInfo>();
        this.subClasses = new TreeSet<ClassInfo<?, ?, ?, ?>>();

        this.typeParameters = new LinkedList<TypeParameterInfo>();
        this.typeParameterUsages = new HashMap<TypeParameterInfo, TypeInfo>();

        this.modifiers = new HashSet<ModifierInfo>();
        this.modifiers.addAll(modifiers);
    }

    /**
     * 完全限定名からクラス情報オブジェクトを生成する
     * 
     * @param modifiers 修飾子のSet
     * @param fullQualifiedName 完全限定名
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public ClassInfo(final Set<ModifierInfo> modifiers, final String[] fullQualifiedName,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fullQualifiedName) {
            throw new NullPointerException();
        }
        if (0 == fullQualifiedName.length) {
            throw new IllegalArgumentException("Full Qualified Name must has at least 1 word!");
        }

        this.definedMethods = new TreeSet<M>();
        this.definedConstructors = new TreeSet<C>();
        this.definedFields = new TreeSet<F>();
        this.innerClasses = new TreeSet<I>();

        String[] namespace = new String[fullQualifiedName.length - 1];
        System.arraycopy(fullQualifiedName, 0, namespace, 0, fullQualifiedName.length - 1);
        this.namespace = new NamespaceInfo(namespace);
        this.className = fullQualifiedName[fullQualifiedName.length - 1];
        this.superClasses = new LinkedList<ClassTypeInfo>();
        this.subClasses = new TreeSet<ClassInfo<?, ?, ?, ?>>();

        this.typeParameters = new LinkedList<TypeParameterInfo>();
        this.typeParameterUsages = new HashMap<TypeParameterInfo, TypeInfo>();

        this.modifiers = new HashSet<ModifierInfo>();
        this.modifiers.addAll(modifiers);
    }

    /**
     * このクラスのクラス名を返す
     * 
     * @return クラス名
     */
    public String getClassName() {
        return this.className;
    }

    /**
     * このクラスの名前空間名を返す
     * 
     * @return 名前空間名
     */
    public NamespaceInfo getNamespace() {
        return this.namespace;
    }

    /**
     * クラスオブジェクトの比較の場合は，名前空間に基づいた順序にするために定義している．
     */
    @Override
    public final int compareTo(final Position o) {

        if (null == o) {
            throw new IllegalArgumentException();
        }

        if (o instanceof ClassInfo<?, ?, ?, ?>) {

            final NamespaceInfo namespace = this.getNamespace();
            final NamespaceInfo correspondNamespace = ((ClassInfo<?, ?, ?, ?>) o).getNamespace();
            final int namespaceOrder = namespace.compareTo(correspondNamespace);
            if (namespaceOrder != 0) {
                return namespaceOrder;
            }

            final String name = this.getClassName();
            final String correspondName = ((ClassInfo<?, ?, ?, ?>) o).getClassName();
            return name.compareTo(correspondName);

        } else {
            return super.compareTo(o);
        }
    }

    /**
     * メトリクス計測対象としての名前を返す
     * 
     * @return メトリクス計測対象としての名前
     */
    @Override
    public final String getMeasuredUnitName() {
        return this.getFullQualifiedName(Settings.getInstance().getLanguage()
                .getNamespaceDelimiter());
    }

    /**
     * このクラスの修飾子の Set を返す
     * 
     * @return このクラスの修飾子の Set
     */
    @Override
    public Set<ModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(this.modifiers);
    }

    /**
     * このクラスに親クラス（の型）を追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param referenceType 追加する親クラスの型
     */
    public void addSuperClass(final ClassTypeInfo referenceType) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == referenceType) {
            throw new NullPointerException();
        }

        this.superClasses.add(referenceType);
    }

    /**
     * このクラスに子クラスを追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param subClass 追加する子クラス
     */
    public void addSubClass(final ClassInfo<?, ?, ?, ?> subClass) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == subClass) {
            throw new NullPointerException();
        }

        this.subClasses.add(subClass);
    }

    /**
     * このクラスのスーパークラスの SortedSet を返す．
     * 
     * @return スーパークラスの SortedSet
     */
    public List<ClassTypeInfo> getSuperClasses() {
        return Collections.unmodifiableList(this.superClasses);
    }

    /**
     * このクラスのサブクラスの SortedSet を返す．
     * 
     * @return サブクラスの SortedSet
     */
    public SortedSet<ClassInfo<?, ?, ?, ?>> getSubClasses() {
        return Collections.unmodifiableSortedSet(this.subClasses);
    }

    /**
     * このクラスの完全限定名を返す
     * 
     * @return このクラスの完全限定名
     */
    public final String[] getFullQualifiedName() {

        final String[] namespace = this.getNamespace().getName();
        final String[] fullQualifiedName = new String[namespace.length + 1];
        System.arraycopy(namespace, 0, fullQualifiedName, 0, namespace.length);
        fullQualifiedName[fullQualifiedName.length - 1] = this.getClassName();

        return fullQualifiedName;
    }

    /**
     * このクラスの完全限定名を返す．完全限定名は引数で与えられた文字列により連結され，返される．
     * 
     * @param delimiter 区切り文字
     * @return このクラスの完全限定名
     */
    public final String getFullQualifiedName(final String delimiter) {

        if (null == delimiter) {
            throw new NullPointerException();
        }

        StringBuffer buffer = new StringBuffer();
        String[] namespace = this.getNamespace().getName();
        for (int i = 0; i < namespace.length; i++) {
            buffer.append(namespace[i]);
            buffer.append(delimiter);
        }
        buffer.append(this.getClassName());
        return buffer.toString();
    }

    /**
     * 等しいかどうかのチェック
     * 
     * @return 等しい場合は true, 等しくない場合は false
     */
    @Override
    public final boolean equals(final Object o) {

        if (!(o instanceof ClassInfo<?, ?, ?, ?>)) {
            return false;
        }

        if (this == o) {
            return true;
        }

        final NamespaceInfo namespace = this.getNamespace();
        final NamespaceInfo correspondNamespace = ((ClassInfo<?, ?, ?, ?>) o).getNamespace();
        if (!namespace.equals(correspondNamespace)) {
            return false;
        }

        final String className = this.getClassName();
        final String correspondClassName = ((ClassInfo<?, ?, ?, ?>) o).getClassName();
        return className.equals(correspondClassName);
    }

    /**
     * このクラスが引数で与えられたクラスの親クラスであるかを判定する
     * 
     * @param classInfo 対象クラス
     * @return このクラスが引数で与えられたクラスの親クラスである場合は true，そうでない場合は false
     */
    public final boolean isSuperClass(final ClassInfo<?, ?, ?, ?> classInfo) {

        // 引数の直接の親クラスに対して
        for (final ClassInfo<?, ?, ?, ?> superClass : ClassTypeInfo.convert(classInfo
                .getSuperClasses())) {

            // 対象クラスの直接の親クラスがこのクラスと等しい場合は true を返す
            if (this.equals(superClass)) {
                return true;
            }

            // 対象クラスの親クラスに対して再帰的に処理，true が返された場合は，このメソッドも true を返す
            if (this.isSuperClass(superClass)) {
                return true;
            }
        }

        return false;
    }

    /**
     * このクラスが引数で与えられたクラスの子クラスであるかを判定する
     * 
     * @param classInfo 対象クラス
     * @return このクラスが引数で与えられたクラスの子クラスである場合は true，そうでない場合は false
     */
    public final boolean isSubClass(final ClassInfo<?, ?, ?, ?> classInfo) {

        // 引数の直接の子クラスに対して
        for (final ClassInfo<?, ?, ?, ?> subClassInfo : classInfo.getSubClasses()) {

            // 対象クラスの直接の親クラスがこのクラスと等しい場合は true を返す
            if (this.equals(subClassInfo)) {
                return true;
            }

            // 対象クラスの親クラスに対して再帰的に処理，true が返された場合は，このメソッドも true を返す
            if (this.isSubClass(subClassInfo)) {
                return true;
            }
        }

        return false;
    }

    /**
     * このクラスが引数で与えられたクラスのインナークラスであるかを判定する
     * 
     * @param classInfo 対象クラス
     * @return このクラスが引数で与えられたクラスのインナークラスである場合は true，そうでない場合は false
     */
    public final boolean isInnerClass(final ClassInfo<?, ?, ?, ?> classInfo) {

        // 引数がnullのときはfalse
        if (null == classInfo) {
            return false;
        }

        for (final InnerClassInfo<?> innerClassInfo : classInfo.getInnerClasses()) {

            // このクラスが引数の直接の子クラスと等しい場合は true を返す
            if (innerClassInfo.equals(this)) {
                return true;
            }

            // このクラスが引数の間接的な子クラスである場合も true を返す
            if (this.isInnerClass((ClassInfo<?, ?, ?, ?>) innerClassInfo)) {
                return true;
            }
        }

        // 子クラスを再帰的に調べた結果，このクラスと一致するクラスが見つからなかったので false を返す
        return false;
    }

    /**
     * 引数で指定された型パラメータを追加する
     * 
     * @param typeParameter 追加する型パラメータ
     */
    @Override
    public void addTypeParameter(final TypeParameterInfo typeParameter) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameter) {
            throw new NullPointerException();
        }

        this.typeParameters.add(typeParameter);
    }

    /**
     * 指定されたインデックスの型パラメータを返す
     * 
     * @param index 型パラメータのインデックス
     * @return　指定されたインデックスの型パラメータ
     */
    @Override
    public TypeParameterInfo getTypeParameter(final int index) {
        return this.typeParameters.get(index);
    }

    /**
     * このクラスの型パラメータの List を返す．
     * 
     * @return このクラスの型パラメータの List
     */
    @Override
    public List<TypeParameterInfo> getTypeParameters() {
        return Collections.unmodifiableList(this.typeParameters);
    }

    /**
     * 型パラメータの使用を追加する
     * 
     * @param typeParameterInfo 型パラメータ 
     * @param usedType 型パラメータに代入されている型
     */
    @Override
    public void addTypeParameterUsage(final TypeParameterInfo typeParameterInfo,
            final TypeInfo usedType) {

        if ((null == typeParameterInfo) || (null == usedType)) {
            throw new IllegalArgumentException();
        }

        this.typeParameterUsages.put(typeParameterInfo, usedType);
    }

    /**
     * 型パラメータ使用のマップを返す
     * 
     * @return 型パラメータ使用のマップ
     */
    @Override
    public Map<TypeParameterInfo, TypeInfo> getTypeParameterUsages() {
        return Collections.unmodifiableMap(this.typeParameterUsages);
    }

    /**
     * このクラスに定義されたメソッド情報を追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param definedMethod 追加する定義されたメソッド
     */
    public final void addDefinedMethod(final M definedMethod) {

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
    public final void addDefinedConstructor(final C definedConstructor) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedConstructor) {
            throw new NullPointerException();
        }

        this.definedConstructors.add(definedConstructor);
    }

    /**
     * このクラスに定義されたフィールド情報を追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param definedField 追加する定義されたフィールド
     */
    public final void addDefinedField(final F definedField) {

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
    public final SortedSet<M> getDefinedMethods() {
        return Collections.unmodifiableSortedSet(this.definedMethods);
    }

    /**
     * このクラスに定義されているコンストラクタの SortedSet を返す．
     * 
     * @return 定義されているメソッドの SortedSet
     */
    public final SortedSet<C> getDefinedConstructors() {
        return Collections.unmodifiableSortedSet(this.definedConstructors);
    }

    /**
     * このクラスに定義されているフィールドの SortedSet を返す．
     * 
     * @return 定義されているフィールドの SortedSet
     */
    public final SortedSet<F> getDefinedFields() {
        return Collections.unmodifiableSortedSet(this.definedFields);
    }

    /**
     * このクラスにインナークラスを追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param innerClass 追加するインナークラス
     */
    public final void addInnerClass(final I innerClass) {

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
    public final SortedSet<I> getInnerClasses() {
        return Collections.unmodifiableSortedSet(this.innerClasses);
    }

    /**
     * クラス名を保存するための変数
     */
    private final String className;

    /**
     * 名前空間名を保存するための変数
     */
    private final NamespaceInfo namespace;

    /**
     * 修飾子を保存する変数
     */
    private final Set<ModifierInfo> modifiers;

    /**
     * このクラスが継承しているクラス一覧を保存するための変数． 直接の親クラスのみを保有するが，多重継承を考えて Set にしている．
     */
    private final List<ClassTypeInfo> superClasses;

    /**
     * このクラスを継承しているクラス一覧を保存するための変数．直接の子クラスのみを保有する．
     */
    private final SortedSet<ClassInfo<?, ?, ?, ?>> subClasses;

    /**
     * このクラスで使用されている型パラメータと実際に型パラメータに代入されている型のペア.
     * このクラスで定義されている型パラメータではない．
     * 
     * class A<T> extends B<String> な場合，String は含まれるが，Tは含まれない
     */
    private final Map<TypeParameterInfo, TypeInfo> typeParameterUsages;

    /**
     * 型パラメータを保存する変数
     */
    private final List<TypeParameterInfo> typeParameters;

    /**
     * このクラスで定義されているメソッド一覧を保存するための変数．
     */
    protected final SortedSet<M> definedMethods;

    /**
     * このクラスで定義されているコンストラクタ一覧を保存するための変数．
     */
    protected final SortedSet<C> definedConstructors;

    /**
     * このクラスで定義されているフィールド一覧を保存するための変数．
     */
    protected final SortedSet<F> definedFields;

    /**
     * このクラスの内部クラス一覧を保存するための変数．直接の内部クラスのみを保有する．
     */
    private final SortedSet<I> innerClasses;

}
