package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public abstract class MethodInfo implements Comparable<MethodInfo>, Resolved {

    /**
     * メソッドオブジェクトを初期化する
     * 
     * @param methodName メソッド名
     * @param 返り値の型
     * @param メソッドを定義しているクラス
     * @param コンストラクタかどうか
     */
    public MethodInfo(final String methodName, final TypeInfo returnType,
            final ClassInfo ownerClass, final boolean constructor) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == methodName) || (null == returnType) || (null == ownerClass)) {
            throw new NullPointerException();
        }

        this.methodName = methodName;
        this.returnType = returnType;
        this.ownerClass = ownerClass;
        this.constructor = constructor;

        this.parameters = new LinkedList<ParameterInfo>();
        this.callees = new TreeSet<MethodInfo>();
        this.callers = new TreeSet<MethodInfo>();
        this.overridees = new TreeSet<MethodInfo>();
        this.overriders = new TreeSet<MethodInfo>();
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
            String name = this.getMethodName();
            String correspondName = method.getMethodName();
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
                    Iterator<ParameterInfo> parameterIterator = this.getParameters().iterator();
                    Iterator<ParameterInfo> correspondParameterIterator = method.getParameters()
                            .iterator();
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
     * このメソッドが，引数で与えられた情報を使って呼び出すことができるかどうかを判定する．
     * 
     * @param methodName メソッド名
     * @param actualParameterTypes 引数の型のリスト
     * @return 呼び出せる場合は true，そうでない場合は false
     */
    public final boolean canCalledWith(final String methodName,
            final List<TypeInfo> actualParameterTypes) {

        if ((null == methodName) || (null == actualParameterTypes)) {
            throw new NullPointerException();
        }

        // メソッド名が等しくない場合は該当しない
        if (!methodName.equals(this.getMethodName())) {
            return false;
        }

        // 引数の数が等しくない場合は該当しない
        final List<ParameterInfo> dummyParameters = this.getParameters();
        if (dummyParameters.size() != actualParameterTypes.size()) {
            return false;
        }

        // 引数の型を先頭からチェック等しくない場合は該当しない
        final Iterator<ParameterInfo> dummyParameterIterator = dummyParameters.iterator();
        final Iterator<TypeInfo> actualParameterTypeIterator = actualParameterTypes.iterator();
        NEXT_PARAMETER: while (dummyParameterIterator.hasNext()
                && actualParameterTypeIterator.hasNext()) {
            final ParameterInfo dummyParameter = dummyParameterIterator.next();
            final TypeInfo actualParameterType = actualParameterTypeIterator.next();

            // 実引数が参照型の場合
            if (actualParameterType instanceof ClassInfo) {

                // 仮引数が参照型でない場合は該当しない
                if (!(dummyParameter.getType() instanceof ClassInfo)) {
                    return false;
                }

                // 仮引数，実引数共に対象クラスである場合は，その継承関係を考慮する．つまり，実引数が駆り引数のサブクラスでない場合は，呼び出し可能ではない
                if ((actualParameterType instanceof TargetClassInfo)
                        && (dummyParameter.getType() instanceof TargetClassInfo)) {

                    // 実引数が仮引数と同じ参照型（クラス）でもなく，仮引数のサブクラスでもない場合は該当しない
                    if (actualParameterType.equals(dummyParameter.getType())) {
                        continue NEXT_PARAMETER;

                    } else if (((ClassInfo) actualParameterType)
                            .isSubClass((ClassInfo) dummyParameter.getType())) {
                        continue NEXT_PARAMETER;

                    } else {
                        return false;
                    }

                    // 仮引数，実引数のどちらか，あるいは両方が外部クラスである場合は，継承関係から呼び出し可能かどうかを判断することができない
                    // この場合は，全て呼び出し可能であるとする
                } else {
                    continue NEXT_PARAMETER;
                }

                // 実引数がプリミティブ型の場合
            } else if (actualParameterType instanceof PrimitiveTypeInfo) {

                // PrimitiveTypeInfo#equals を使って等価性の判定．
                // 等しくない場合は該当しない
                if (actualParameterType.equals(dummyParameter.getType())) {
                    continue NEXT_PARAMETER;
                }else{
                    return false;
                }

                // 実引数が配列型の場合
            } else if (actualParameterType instanceof ArrayTypeInfo) {

                if (!(dummyParameter.getType() instanceof ArrayTypeInfo)) {
                    return false;
                }

                final int actualArrayDimension = ((ArrayTypeInfo) actualParameterType)
                        .getDimension();
                final int dummyArrayDimension = ((ArrayTypeInfo) dummyParameter.getType())
                        .getDimension();
                final TypeInfo actualArrayElementType = ((ArrayTypeInfo) actualParameterType)
                        .getElementType();
                final TypeInfo dummyArrayElementType = ((ArrayTypeInfo) dummyParameter.getType())
                        .getElementType();
                if (actualArrayDimension != dummyArrayDimension) {
                    return false;
                } else if (!actualArrayElementType.equals(dummyArrayElementType)) {
                    return false;
                }

                continue NEXT_PARAMETER;
                // TODO Java言語の場合は，仮引数が java.lang.object でもOKな処理が必要

                // 実引数が null の場合
            } else if (actualParameterType instanceof NullTypeInfo) {

                // 仮引数が参照型でない場合は該当しない
                if (!(dummyParameter.getType() instanceof ClassInfo)) {
                    return false;
                }

                continue NEXT_PARAMETER;
                // TODO Java言語の場合は，仮引数が配列型の場合でもOKな処理が必要

                // 実引数の型が解決できなかった場合
            } else if (actualParameterType instanceof UnknownTypeInfo) {

                // 実引数の型が不明な場合は，仮引数の型が何であろうともOKにしている
                continue NEXT_PARAMETER;
                
            } else {
                assert false : "Here shouldn't be reached!";
            }
        }

        return true;
    }

    /**
     * このメソッドの名前を返す
     * 
     * @return メソッド名
     */
    public final String getMethodName() {
        return this.methodName;
    }

    /**
     * このメソッドを定義しているクラスを返す．
     * 
     * @return このメソッドを定義しているクラス
     */
    public final ClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * このメソッドの返り値の型を返す
     * 
     * @return 返り値の型
     */
    public final TypeInfo getReturnType() {
        return this.returnType;
    }

    /**
     * このメソッドがコンストラクタかどうかを返す．
     * 
     * @return コンストラクタである場合は true，そうでない場合は false
     */
    public boolean isConstuructor() {
        return this.constructor;
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
     * このメソッドの引数を追加する． public 宣言してあるが， プラグインからの呼び出しははじく．
     * 
     * @param parameters 追加する引数群
     */
    public void addParameters(final List<ParameterInfo> parameters) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == parameters) {
            throw new NullPointerException();
        }

        this.parameters.addAll(parameters);
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
     * このメソッドの引数の List を返す．
     * 
     * @return このメソッドの引数の List
     */
    public List<ParameterInfo> getParameters() {
        return Collections.unmodifiableList(this.parameters);
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
     * このメソッドが呼び出しているメソッドの SortedSet を返す．
     * 
     * @return このメソッドが呼び出しているメソッドの SortedSet
     */
    public SortedSet<MethodInfo> getCallees() {
        return Collections.unmodifiableSortedSet(this.callees);
    }

    /**
     * このメソッドを呼び出しているメソッドの SortedSet を返す．
     * 
     * @return このメソッドを呼び出しているメソッドの SortedSet
     */
    public SortedSet<MethodInfo> getCallers() {
        return Collections.unmodifiableSortedSet(this.callers);
    }

    /**
     * このメソッドがオーバーライドしているメソッドの SortedSet を返す．
     * 
     * @return このメソッドがオーバーライドしているメソッドの SortedSet
     */
    public SortedSet<MethodInfo> getOverridees() {
        return Collections.unmodifiableSortedSet(this.overridees);
    }

    /**
     * このメソッドをオーバーライドしているメソッドの SortedSet を返す．
     * 
     * @return このメソッドをオーバーライドしているメソッドの SortedSet
     */
    public SortedSet<MethodInfo> getOverriders() {
        return Collections.unmodifiableSortedSet(this.overriders);
    }

    /**
     * メソッド名を保存するための変数
     */
    private final String methodName;

    /**
     * 所属しているクラスを保存するための変数
     */
    private final ClassInfo ownerClass;

    /**
     * 返り値の型を保存するための変数
     */
    private final TypeInfo returnType;

    /**
     * このメソッドがコンストラクタかどうかを保存するための変数
     */
    private final boolean constructor;

    /**
     * 引数のリストの保存するための変数
     */
    protected final List<ParameterInfo> parameters;

    /**
     * このメソッドが呼び出しているメソッド一覧を保存するための変数
     */
    protected final SortedSet<MethodInfo> callees;

    /**
     * このメソッドを呼び出しているメソッド一覧を保存するための変数
     */
    protected final SortedSet<MethodInfo> callers;

    /**
     * このメソッドがオーバーライドしているメソッド一覧を保存するための変数
     */
    protected final SortedSet<MethodInfo> overridees;

    /**
     * オーバーライドされているメソッドを保存するための変数
     */
    protected final SortedSet<MethodInfo> overriders;
}
