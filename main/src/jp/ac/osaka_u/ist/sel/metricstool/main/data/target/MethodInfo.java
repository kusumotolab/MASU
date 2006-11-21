package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


/**
 * メソッドの情報を保有するクラス． 以下の情報を持つ．
 * <ul>
 * <li>メソッド名</li>
 * <li>修飾子</li>
 * <li>返り値の型</li>
 * <li>引数のリスト</li>
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
public final class MethodInfo implements Comparable<MethodInfo> {

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
    public MethodInfo(String name, TypeInfo returnType, ClassInfo ownerClass) {
        this.name = name;
        this.ownerClass = ownerClass;
        this.returnType = returnType;

        this.parameters = new LinkedList<ParameterInfo>();
        this.callees = new TreeSet<MethodInfo>();
        this.callers = new TreeSet<MethodInfo>();
        this.overridees = new TreeSet<MethodInfo>();
        this.overriders = new TreeSet<MethodInfo>();
        this.referencees = new TreeSet<FieldInfo>();
        this.assignmentees = new TreeSet<FieldInfo>();
    }

    /**
     * 引数を追加するメソッド． public 宣言してあるが， プラグインからの呼び出しははじく．
     * 
     * @param parameter 追加する引数
     */
    public void addParameter(ParameterInfo parameter) {
        // TODO プラグインからの呼び出しをはじく処理をする
        this.parameters.add(parameter);
    }

    /**
     * メソッド間の順序関係を定義するメソッド．以下の順序で順序を決める．
     * <ol>
     * <li>メソッドを定義しているクラスの名前空間名</li>
     * <li>メソッドを定義しているクラスのクラス名</li>
     * <li>メソッド名</li>
     * <li>メソッドの引数の個数</li>
     * <li>メソッドの引数の型（第一引数から順番に）</li>
     */
    public int compareTo(MethodInfo method) {
        // クラスオブジェクトの compareTo を用いる．
        // クラスの名前空間名，クラス名が比較に用いられている．
        ClassInfo ownerClass = this.getOwnerClass();
        ClassInfo correspondOwnerClass = method.getOwnerClass();
        final int classOrder = ownerClass.compareTo(correspondOwnerClass);
        if (classOrder != 0) {
            return classOrder;
        } else {

            // メソッド名で比較
            String name = this.getName();
            String correspondName = method.getName();
            final int methodNameOrder = name.compareTo(correspondName);
            if (methodNameOrder != 0) {
                return methodNameOrder;
            } else {

                // 引数の個数で比較
                final int parameterNumber = this.getParameterNumber();
                final int correspondParameterNumber = method.getParameterNumber();
                if (parameterNumber < correspondParameterNumber) {
                    return 1;
                } else if (parameterNumber > correspondParameterNumber) {
                    return -1;
                } else {

                    // 引数の型で比較．第一引数から順番に．
                    Iterator<ParameterInfo> parameterIterator = this.parameterIterator();
                    Iterator<ParameterInfo> correspondParameterIterator = method
                            .parameterIterator();
                    while (parameterIterator.hasNext() && correspondParameterIterator.hasNext()) {
                        ParameterInfo parameter = parameterIterator.next();
                        ParameterInfo correspondParameter = correspondParameterIterator.next();
                        String typeName = parameter.getName();
                        String correspondTypeName = correspondParameter.getName();
                        final int typeOrder = typeName.compareTo(correspondTypeName);
                        if (typeOrder != 0) {
                            return typeOrder;
                        }
                    }

                    return 0;
                }

            }
        }
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
     * このメソッドの引数の数を返す
     * 
     * @return このメソッドの引数の数
     */
    public int getParameterNumber() {
        return this.parameters.size();
    }

    /**
     * このメソッドの返り値の型を返す
     * 
     * @return 返り値の型
     */
    public TypeInfo getReturnType() {
        return this.returnType;
    }

    /**
     * このメソッドの引数の Iterator を返す．
     * 
     * @return このメソッドの引数の Iterator
     */
    public Iterator<ParameterInfo> parameterIterator() {
        return this.parameters.iterator();
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
     * 
     * @return このメソッドが呼び出しているメソッドの Iterator
     */
    public Iterator<MethodInfo> calleeIterator() {
        return this.callees.iterator();
    }

    /**
     * このメソッドを呼び出しているメソッドの Iterator を返す．
     * 
     * @return このメソッドを呼び出しているメソッドの Iterator
     */
    public Iterator<MethodInfo> callerIterator() {
        return this.callers.iterator();
    }

    /**
     * このメソッドがオーバーライドしているメソッドの Iterator を返す．
     * 
     * @return このメソッドがオーバーライドしているメソッドの Iterator
     */
    public Iterator<MethodInfo> overrideeIterator() {
        return this.overridees.iterator();
    }

    /**
     * このメソッドをオーバーライドしているメソッドの Iterator を返す．
     * 
     * @return このメソッドをオーバーライドしているメソッドの Iterator
     */
    public Iterator<MethodInfo> overriderIterator() {
        return this.overriders.iterator();
    }

    /**
     * このメソッドが参照しているフィールドの Iterator を返す．
     * 
     * @return このメソッドが参照しているフィールドの Iterator
     */
    public Iterator<FieldInfo> referenceIterator() {
        return this.referencees.iterator();
    }

    /**
     * このメソッドが代入しているフィールドの Iterator を返す．
     * 
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
     * 返り値の型を保存するための変数
     */
    private TypeInfo returnType;

    /**
     * 引数のリストの保存するための変数
     */
    private final List<ParameterInfo> parameters;

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
