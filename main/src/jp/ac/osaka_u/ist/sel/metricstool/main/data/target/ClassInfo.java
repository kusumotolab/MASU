package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricMeasurable;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * クラス情報を格納するための抽象クラス
 * 
 * @author y-higo
 * 
 */
public abstract class ClassInfo implements UnitInfo, TypeInfo, Comparable<ClassInfo>,
        MetricMeasurable {

    /**
     * 名前空間名とクラス名からオブジェクトを生成する
     * 
     * @param namespace 名前空間名
     * @param className クラス名
     * 
     */
    public ClassInfo(final NamespaceInfo namespace, final String className) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == namespace) || (null == className)) {
            throw new NullPointerException();
        }

        this.namespace = namespace;
        this.className = className;
        this.superClasses = new TreeSet<ClassInfo>();
        this.subClasses = new TreeSet<ClassInfo>();
    }

    /**
     * 完全限定名からクラス情報オブジェクトを生成する
     * 
     * @param fullQualifiedName 完全限定名
     */
    public ClassInfo(final String[] fullQualifiedName) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fullQualifiedName) {
            throw new NullPointerException();
        }
        if (0 == fullQualifiedName.length) {
            throw new IllegalArgumentException("Full Qualified Name must has at least 1 word!");
        }

        String[] namespace = new String[fullQualifiedName.length - 1];
        System.arraycopy(fullQualifiedName, 0, namespace, 0, fullQualifiedName.length - 1);
        this.namespace = new NamespaceInfo(namespace);
        this.className = fullQualifiedName[fullQualifiedName.length - 1];
        this.superClasses = new TreeSet<ClassInfo>();
        this.subClasses = new TreeSet<ClassInfo>();
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
     * クラスオブジェクトの順序関係を定義するメソッド． 現在は，名前空間名順序を用いている．名前空間名が同じ場合は，クラス名（String）の順序になる．
     */
    public final int compareTo(final ClassInfo classInfo) {

        if (null == classInfo) {
            throw new NullPointerException();
        }

        final NamespaceInfo namespace = this.getNamespace();
        final NamespaceInfo correspondNamespace = classInfo.getNamespace();
        final int namespaceOrder = namespace.compareTo(correspondNamespace);
        if (namespaceOrder != 0) {
            return namespaceOrder;
        }

        final String name = this.getClassName();
        final String correspondName = classInfo.getClassName();
        return name.compareTo(correspondName);

    }

    /**
     * メトリクス計測対象としての名前を返す
     * 
     * @return メトリクス計測対象としての名前
     */
    public final String getMeasuredUnitName() {
        return this.getFullQualifiedName(Settings.getLanguage().getNamespaceDelimiter());
    }

    /**
     * このクラスに親クラスを追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param superClassReference 追加する親クラスの参照
     */
    public void addSuperClass(final ClassReferenceInfo superClassReference) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == superClassReference) {
            throw new NullPointerException();
        }

        this.superClasses.add((ClassInfo) superClassReference.getType());
    }

    /**
     * このクラスに子クラスを追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param subClass 追加する子クラス
     */
    public void addSubClass(final ClassInfo subClass) {

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
    public SortedSet<ClassInfo> getSuperClasses() {
        return Collections.unmodifiableSortedSet(this.superClasses);
    }

    /**
     * このクラスのサブクラスの SortedSet を返す．
     * 
     * @return サブクラスの SortedSet
     */
    public SortedSet<ClassInfo> getSubClasses() {
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
     * このクラスの型名を返す
     * 
     * @return このクラスの型名を返す
     */
    public final String getTypeName() {

        final String delimiter = Settings.getLanguage().getNamespaceDelimiter();
        final StringBuffer buffer = new StringBuffer();
        final String[] namespace = this.getNamespace().getName();
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
    public final boolean equals(final TypeInfo typeInfo) {

        if (null == typeInfo) {
            throw new NullPointerException();
        }

        if (!(typeInfo instanceof ClassInfo)) {
            return false;
        }

        final NamespaceInfo namespace = this.getNamespace();
        final NamespaceInfo correspondNamespace = ((ClassInfo) typeInfo).getNamespace();
        if (!namespace.equals(correspondNamespace)) {
            return false;
        }

        final String className = this.getClassName();
        final String correspondClassName = ((ClassInfo) typeInfo).getClassName();
        return className.equals(correspondClassName);
    }

    /**
     * このクラスが引数で与えられたクラスの親クラスであるかを判定する
     * 
     * @param classInfo 対象クラス
     * @return このクラスが引数で与えられたクラスの親クラスである場合は true，そうでない場合は false
     */
    public final boolean isSuperClass(final ClassInfo classInfo) {

        // 引数の直接の親クラスに対して
        for (final ClassInfo superClassInfo : classInfo.getSuperClasses()) {

            // 対象クラスの直接の親クラスがこのクラスと等しい場合は true を返す
            if (this.equals(superClassInfo)) {
                return true;
            }

            // 対象クラスの親クラスに対して再帰的に処理，true が返された場合は，このメソッドも true を返す
            if (this.isSuperClass(superClassInfo)) {
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
    public final boolean isSubClass(final ClassInfo classInfo) {

        // 引数の直接の子クラスに対して
        for (final ClassInfo subClassInfo : classInfo.getSubClasses()) {

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
    public final boolean isInnerClass(final ClassInfo classInfo) {

        // 引数で与えられたクラスが TargetClassInfo 出ない場合は false
        if (!(classInfo instanceof TargetClassInfo)) {
            return false;
        }

        for (final ClassInfo innerClassInfo : ((TargetClassInfo) classInfo).getInnerClasses()) {

            // このクラスが引数の直接の子クラスと等しい場合は true を返す
            if (innerClassInfo.equals(this)) {
                return true;
            }

            // このクラスが引数の間接的な子クラスである場合も true を返す
            if (this.isInnerClass(innerClassInfo)) {
                return true;
            }
        }

        // 子クラスを再帰的に調べた結果，このクラスと一致するクラスが見つからなかったので false を返す
        return false;
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
     * このクラスが継承しているクラス一覧を保存するための変数． 直接の親クラスのみを保有するが，多重継承を考えて Set にしている．
     */
    private final SortedSet<ClassInfo> superClasses;

    /**
     * このクラスを継承しているクラス一覧を保存するための変数．直接の子クラスのみを保有する．
     */
    private final SortedSet<ClassInfo> subClasses;
}
