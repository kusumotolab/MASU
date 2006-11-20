package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

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
public class ClassInfo implements TypeInfo, Comparable<ClassInfo> {

    /**
     * クラスオブジェクトを初期化する． 以下の情報が引数として与えられなければならない．
     * <ul>
     * <li>クラス名</li>
     * <li>修飾子</li>
     * </ul>
     * 
     * @param className クラス名
     */
    public ClassInfo(NamespaceInfo namespace, String className) {
        this.namespace = namespace;
        this.className = className;

        this.superClasses = new TreeSet<ClassInfo>();
        this.subClasses = new TreeSet<ClassInfo>();
        this.innerClasses = new TreeSet<ClassInfo>();
        this.definedMethods = new TreeSet<MethodInfo>();
        this.definedFields = new TreeSet<FieldInfo>();
    }

    /**
     * クラスオブジェクトの順序関係を定義するメソッド． 現在は，名前空間名順序を用いている．名前空間名が同じ場合は，クラス名（String）の順序になる．
     */
    public int compareTo(ClassInfo classInfo) {
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
     * このクラスの名前空間名を返す
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
     * このクラスの名前を返す． ここの名前とは，名前空間名 + クラス名を表す．
     */
    /**
     * このクラスのスーパークラスの Iterator を返す．
     * 
     * @return スーパークラスの Iterator
     */
    public Iterator<ClassInfo> superClassIterator() {
        return this.superClasses.iterator();
    }

    /**
     * このクラスのサブクラスの Iterator を返す．
     * 
     * @return サブクラスの Iterator
     */
    public Iterator<ClassInfo> subClassIterator() {
        return this.subClasses.iterator();
    }

    /**
     * このクラスのインナークラスの Iterator を返す．
     * 
     * @return インナークラスの Iterator
     */
    public Iterator<ClassInfo> innerClassIterator() {
        return this.innerClasses.iterator();
    }

    /**
     * このクラスに定義されているメソッドの Iterator を返す．
     * 
     * @return 定義されているメソッドの Iterator
     */
    public Iterator<MethodInfo> definedMethodIterator() {
        return this.definedMethods.iterator();
    }

    /**
     * このクラスに定義されているフィールドの Iterator を返す．
     * 
     * @return 定義されているフィールドの Iterator
     */
    public Iterator<FieldInfo> definedFieldIterator() {
        return this.definedFields.iterator();
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
