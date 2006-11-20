package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;


/**
 * メソッドの情報を保有するクラス． 以下の情報を持つ．
 * <ul>
 * <li>メソッド名</li>
 * <li>修飾子</li>
 * <li>シグネチャ</li>
 * <li>行数</li>
 * <li>コントロールグラフ（しばらくは未実装）</li>
 * <li>ローカル変数</li>
 * <li>所属しているクラス</li>
 * <li>呼び出しているメソッド</li>
 * <li>呼び出されているメソッド</li>
 * <li>オーバーライドしているメソッド</li>
 * <li>オーバーライドされているメソッド</li>
 * <li>参照しているフィールド</li>
 * <li>代入しているフィールド</li>
 * </ul>
 * 
 * @author y-higo
 * 
 */
public class MethodInfo {

    /**
     * メソッドオブジェクトを初期化する． 以下の情報が引数として与えられなければならない．
     * <ul>
     * <li>メソッド名</li>
     * <li>修飾子</li>
     * <li>シグネチャ</li>
     * <li>所有しているクラス</li>
     * </ul>
     * 
     * @param name メソッド名
     * 
     */
    public MethodInfo(String name, ClassInfo ownerClass) {
        this.name = name;
        this.ownerClass = ownerClass;

        this.callees = new TreeSet<MethodInfo>();
        this.callers = new TreeSet<MethodInfo>();
        this.overridees = new TreeSet<MethodInfo>();
        this.overriders = new TreeSet<MethodInfo>();
        this.referencees = new TreeSet<FieldInfo>();
        this.assignmentees = new TreeSet<FieldInfo>();
    }

    /**
     * このメソッドの名前を返す
     * 
     * @return メソッド名
     */
    public String getName() {
        return this.name;
    }

    /**
     * このメソッドの行数を返す
     * 
     * @return このメソッドの行数
     */
    public int getLOC() {
        return this.loc;
    }

    /**
     * このメソッドを定義しているクラスを返す．
     * 
     * @return このメソッドを定義しているクラス
     */
    public ClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * このメソッドが呼び出しているメソッドの Iterator を返す．
     * @return このメソッドが呼び出しているメソッドの Iterator
     */
    public Iterator<MethodInfo> calleeIterator() {
        return this.callees.iterator();
    }

    /**
     * このメソッドを呼び出しているメソッドの Iterator を返す．
     * @return このメソッドを呼び出しているメソッドの Iterator
     */
    public Iterator<MethodInfo> callerIterator() {
        return this.callers.iterator();
    }

    /**
     * このメソッドがオーバーライドしているメソッドの Iterator を返す．
     * @return このメソッドがオーバーライドしているメソッドの Iterator
     */
    public Iterator<MethodInfo> overrideeIterator() {
        return this.overridees.iterator();
    }

    /**
     * このメソッドをオーバーライドしているメソッドの Iterator を返す．
     * @return このメソッドをオーバーライドしているメソッドの Iterator
     */
    public Iterator<MethodInfo> overriderIterator() {
        return this.overriders.iterator();
    }
    
    /**
     * このメソッドが参照しているフィールドの Iterator を返す．
     * @return このメソッドが参照しているフィールドの Iterator
     */
    public Iterator<FieldInfo> referenceIterator() {
        return this.referencees.iterator();
    }
    
    /**
     * このメソッドが代入しているフィールドの Iterator を返す．
     * @return このメソッドが代入しているフィールドの Iterator
     */
    public Iterator<FieldInfo> assignmenteeIterator() {
        return this.assignmentees.iterator();
    }
    
    /**
     * メソッド名を保存するための変数
     */
    private final String name;

    /**
     * 修飾子を保存するための変数
     */
    // TODO 修飾子を保存するための変数を定義する
    /**
     * シグネチャを保存するための変数
     */
    // TODO シグネチャを保存するための変数を定義する
    /**
     * 行数を保存するための変数
     */
    private int loc;

    /**
     * ローカル変数を保存するための変数
     */
    // TODO ローカル変数を保存するための変数を定義する
    /**
     * 所属しているクラスを保存するための変数
     */
    private final ClassInfo ownerClass;

    /**
     * このメソッドが呼び出しているメソッド一覧を保存するための変数
     */
    private final Set<MethodInfo> callees;

    /**
     * このメソッドを呼び出しているメソッド一覧を保存するための変数
     */
    private final Set<MethodInfo> callers;

    /**
     * このメソッドがオーバーライドしているメソッド一覧を保存するための変数
     */
    private final Set<MethodInfo> overridees;

    /**
     * オーバーライドされているメソッドを保存するための変数
     */
    private final Set<MethodInfo> overriders;

    /**
     * 参照しているフィールド一覧を保存するための変数
     */
    private final Set<FieldInfo> referencees;

    /**
     * 代入しているフィールド一覧を保存するための変数
     */
    private final Set<FieldInfo> assignmentees;
}
