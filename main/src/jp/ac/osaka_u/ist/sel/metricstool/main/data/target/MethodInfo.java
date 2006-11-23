package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


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
    public MethodInfo(final String name, final TypeInfo returnType, final ClassInfo ownerClass) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == name) || (null == returnType) || (null == ownerClass)) {
            throw new NullPointerException();
        }

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
     * このメソッドの引数を追加する． public 宣言してあるが， プラグインからの呼び出しははじく．
     * 
     * @param parameter 追加する引数
     */
    public void addParameter(final ParameterInfo parameter) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == parameter) {
            throw new NullPointerException();
        }

        this.parameters.add(parameter);
    }

    /**
     * このメソッドが呼び出しているメソッドを追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param callee 追加する呼び出されるメソッド
     */
    public void addCallee(final MethodInfo callee) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == callee) {
            throw new NullPointerException();
        }

        this.callees.add(callee);
    }

    /**
     * このメソッドを呼び出しているメソッドを追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param caller 追加する呼び出すメソッド
     */
    public void addCaller(final MethodInfo caller) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == caller) {
            throw new NullPointerException();
        }

        this.callers.add(caller);
    }

    /**
     * このメソッドがオーバーライドしているメソッドを追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param overridee 追加するオーバーライドされているメソッド
     */
    public void addOverridee(final MethodInfo overridee) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == overridee) {
            throw new NullPointerException();
        }

        this.overridees.add(overridee);
    }

    /**
     * このメソッドをオーバーライドしているメソッドを追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param overrider 追加するオーバーライドしているメソッド
     * 
     */
    public void addOverrider(final MethodInfo overrider) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == overrider) {
            throw new NullPointerException();
        }

        this.overriders.add(overrider);
    }

    /**
     * このメソッドが参照している変数を追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param referencee 追加する参照されている変数
     */
    public void addReferencee(final FieldInfo referencee) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == referencee) {
            throw new NullPointerException();
        }

        this.referencees.add(referencee);
    }

    /**
     * このメソッドが代入を行っている変数を追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param assignmentee 追加する代入されている変数
     */
    public void addAssignmentee(final FieldInfo assignmentee) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == assignmentee) {
            throw new NullPointerException();
        }

        this.assignmentees.add(assignmentee);
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
    public int compareTo(final MethodInfo method) {
        
        if (null == method) {
            throw new NullPointerException();
        }
        
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
        List<ParameterInfo> unmodifiableParameters = Collections.unmodifiableList(this.parameters);
        return unmodifiableParameters.iterator();
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
        Set<MethodInfo> unmodifiableCallees = Collections.unmodifiableSet(this.callees);
        return unmodifiableCallees.iterator();
    }

    /**
     * このメソッドを呼び出しているメソッドの Iterator を返す．
     * 
     * @return このメソッドを呼び出しているメソッドの Iterator
     */
    public Iterator<MethodInfo> callerIterator() {
        Set<MethodInfo> unmodifiableCallers = Collections.unmodifiableSet(this.callers);
        return unmodifiableCallers.iterator();
    }

    /**
     * このメソッドがオーバーライドしているメソッドの Iterator を返す．
     * 
     * @return このメソッドがオーバーライドしているメソッドの Iterator
     */
    public Iterator<MethodInfo> overrideeIterator() {
        Set<MethodInfo> unmodifiableOverridees = Collections.unmodifiableSet(this.overridees);
        return unmodifiableOverridees.iterator();
    }

    /**
     * このメソッドをオーバーライドしているメソッドの Iterator を返す．
     * 
     * @return このメソッドをオーバーライドしているメソッドの Iterator
     */
    public Iterator<MethodInfo> overriderIterator() {
        Set<MethodInfo> unmodifiableOverriders = Collections.unmodifiableSet(this.overriders);
        return unmodifiableOverriders.iterator();
    }

    /**
     * このメソッドが参照しているフィールドの Iterator を返す．
     * 
     * @return このメソッドが参照しているフィールドの Iterator
     */
    public Iterator<FieldInfo> referenceIterator() {
        Set<FieldInfo> unmodifiableReferencees = Collections.unmodifiableSet(this.referencees);
        return unmodifiableReferencees.iterator();
    }

    /**
     * このメソッドが代入しているフィールドの Iterator を返す．
     * 
     * @return このメソッドが代入しているフィールドの Iterator
     */
    public Iterator<FieldInfo> assignmenteeIterator() {
        Set<FieldInfo> unmodifiableAssignmentees = Collections.unmodifiableSet(this.assignmentees);
        return unmodifiableAssignmentees.iterator();
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

    /**
     * 行数を保存するための変数
     */
    private int loc;

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
