package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;
import sun.reflect.FieldInfo;


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
public final class ClassInfo implements TypeInfo, Comparable<ClassInfo> {

    /**
     * クラスオブジェクトを初期化する． 以下の情報が引数として与えられなければならない．
     * <ul>
     * <li>クラス名</li>
     * <li>修飾子</li>
     * </ul>
     * 
     * @param className クラス名
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
        this.innerClasses = new TreeSet<ClassInfo>();
        this.definedMethods = new TreeSet<MethodInfo>();
        this.definedFields = new TreeSet<FieldInfo>();
    }

    /**
     * このクラスに親クラスを追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param superClass 追加する親クラス
     */
    public void addSuperClass(final ClassInfo superClass) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == superClass) {
            throw new NullPointerException();
        }

        this.superClasses.add(superClass);
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
     * このクラスにインナークラスを追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param innerClass 追加するインナークラス
     */
    public void addInnerClass(final ClassInfo innerClass) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == innerClass) {
            throw new NullPointerException();
        }

        this.innerClasses.add(innerClass);
    }

    /**
     * このクラスに定義されたメソッド情報を追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param definedMethod 追加する定義されたメソッド
     */
    public void addDefinedMethod(final MethodInfo definedMethod) {

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
    public void addDefinedField(final FieldInfo definedField) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == definedField) {
            throw new NullPointerException();
        }

        this.definedFields.add(definedField);
    }

    /**
     * クラスオブジェクトの順序関係を定義するメソッド． 現在は，名前空間名順序を用いている．名前空間名が同じ場合は，クラス名（String）の順序になる．
     */
    public int compareTo(final ClassInfo classInfo) {

        if (null == classInfo) {
            throw new NullPointerException();
        }

        NamespaceInfo namespace = this.getNamespace();
        NamespaceInfo correspondNamespace = classInfo.getNamespace();
        int namespaceOrder = namespace.compareTo(correspondNamespace);
        if (namespaceOrder != 0) {
            return namespaceOrder;
        } else {
            String name = this.getClassName();
            String correspondName = classInfo.getClassName();
            return name.compareTo(correspondName);
        }
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
     * このクラスの名前を返す． ここの名前とは，名前空間名 + クラス名を表す．
     */
    public String getName() {
        NamespaceInfo namespace = this.getNamespace();
        StringBuffer buffer = new StringBuffer();
        buffer.append(namespace.getName());
        buffer.append('.');
        buffer.append(this.getClassName());
        return buffer.toString();
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
     * このクラスのスーパークラスの Iterator を返す．
     * 
     * @return スーパークラスの Iterator
     */
    public Iterator<ClassInfo> superClassIterator() {
        Set<ClassInfo> unmodifiableSuperClasses = Collections.unmodifiableSet(this.superClasses);
        return unmodifiableSuperClasses.iterator();
    }

    /**
     * このクラスのサブクラスの Iterator を返す．
     * 
     * @return サブクラスの Iterator
     */
    public Iterator<ClassInfo> subClassIterator() {
        Set<ClassInfo> unmodifiablesubClasses = Collections.unmodifiableSet(this.subClasses);
        return unmodifiablesubClasses.iterator();
    }

    /**
     * このクラスのインナークラスの Iterator を返す．
     * 
     * @return インナークラスの Iterator
     */
    public Iterator<ClassInfo> innerClassIterator() {
        Set<ClassInfo> unmodifiableInnerClasses = Collections.unmodifiableSet(this.innerClasses);
        return unmodifiableInnerClasses.iterator();
    }

    /**
     * このクラスに定義されているメソッドの Iterator を返す．
     * 
     * @return 定義されているメソッドの Iterator
     */
    public Iterator<MethodInfo> definedMethodIterator() {
        Set<MethodInfo> unmodifiableDefinedMethods = Collections
                .unmodifiableSet(this.definedMethods);
        return unmodifiableDefinedMethods.iterator();
    }

    /**
     * このクラスに定義されているフィールドの Iterator を返す．
     * 
     * @return 定義されているフィールドの Iterator
     */
    public Iterator<FieldInfo> definedFieldIterator() {
        Set<FieldInfo> unmodifiableDefinedFields = Collections.unmodifiableSet(this.definedFields);
        return unmodifiableDefinedFields.iterator();
    }

    /**
     * クラス名を保存する変数
     */
    private final String className;

    /**
     * 名前空間名を保存する変数
     */
    private final NamespaceInfo namespace;

    /**
     * 修飾子を保存する変数
     */
    // TODO 修飾子を保存するための変数
    /**
     * 行数を保存するための変数
     */
    private int loc;

    /**
     * このクラスが継承しているクラス一覧を保存するための変数． 直接の親クラスのみを保有するが，多重継承を考えて Set にしている．
     */
    private final Set<ClassInfo> superClasses;

    /**
     * このクラスを継承しているクラス一覧を保存するための変数．直接の子クラスのみを保有する．
     */
    private final Set<ClassInfo> subClasses;

    /**
     * このクラスの内部クラス一覧を保存するための変数．直接の内部クラスのみを保有する．
     */
    private final Set<ClassInfo> innerClasses;

    /**
     * このクラスで定義されているメソッド一覧を保存するための変数．
     */
    private final Set<MethodInfo> definedMethods;

    /**
     * このクラスで定義されているフィールド一覧を保存するための変数．
     */
    private final Set<FieldInfo> definedFields;

}
