package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricMeasurable;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public abstract class MethodInfo implements UnitInfo, Comparable<MethodInfo>, MetricMeasurable {

    /**
     * メソッドオブジェクトを初期化する
     * 
     * @param methodName メソッド名
     * @param ownerClass メソッドを定義しているクラス
     * @param constructor コンストラクタかどうか
     */
    public MethodInfo(final String methodName, final ClassInfo ownerClass, final boolean constructor) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == methodName) || (null == ownerClass)) {
            throw new NullPointerException();
        }

        this.methodName = methodName;
        this.ownerClass = ownerClass;
        this.constructor = constructor;
        this.returnType = null;

        this.parameters = new LinkedList<ParameterInfo>();
        this.callees = new HashSet<MemberCallInfo>();
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
        final ClassInfo ownerClass = this.getOwnerClass();
        final ClassInfo correspondOwnerClass = method.getOwnerClass();
        final int classOrder = ownerClass.compareTo(correspondOwnerClass);
        if (classOrder != 0) {
            return classOrder;
        }

        // メソッド名で比較
        final String name = this.getMethodName();
        final String correspondName = method.getMethodName();
        final int methodNameOrder = name.compareTo(correspondName);
        if (methodNameOrder != 0) {
            return methodNameOrder;
        }

        // 引数の個数で比較
        final int parameterNumber = this.getParameterNumber();
        final int correspondParameterNumber = method.getParameterNumber();
        if (parameterNumber < correspondParameterNumber) {
            return 1;
        } else if (parameterNumber > correspondParameterNumber) {
            return -1;
        } else {

            // 引数の型で比較．第一引数から順番に．
            final Iterator<ParameterInfo> parameterIterator = this.getParameters().iterator();
            final Iterator<ParameterInfo> correspondParameterIterator = method.getParameters()
                    .iterator();
            while (parameterIterator.hasNext() && correspondParameterIterator.hasNext()) {
                final ParameterInfo parameter = parameterIterator.next();
                final ParameterInfo correspondParameter = correspondParameterIterator.next();
                final String typeName = parameter.getName();
                final String correspondTypeName = correspondParameter.getName();
                final int typeOrder = typeName.compareTo(correspondTypeName);
                if (typeOrder != 0) {
                    return typeOrder;
                }
            }

            return 0;
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
            final List<EntityUsageInfo> actualParameters) {

        if ((null == methodName) || (null == actualParameters)) {
            throw new NullPointerException();
        }

        // メソッド名が等しくない場合は該当しない
        if (!methodName.equals(this.getMethodName())) {
            return false;
        }

        // 引数の数が等しくない場合は該当しない
        final List<ParameterInfo> dummyParameters = this.getParameters();
        if (dummyParameters.size() != actualParameters.size()) {
            return false;
        }

        // 引数の型を先頭からチェック等しくない場合は該当しない
        final Iterator<ParameterInfo> dummyParameterIterator = dummyParameters.iterator();
        final Iterator<EntityUsageInfo> actualParameterIterator = actualParameters.iterator();
        NEXT_PARAMETER: while (dummyParameterIterator.hasNext()
                && actualParameterIterator.hasNext()) {
            final ParameterInfo dummyParameter = dummyParameterIterator.next();
            final EntityUsageInfo actualParameter = actualParameterIterator.next();

            // 実引数が参照型の場合
            if (actualParameter.getType() instanceof ClassTypeInfo) {

                // 実引数の型のクラスを取得
                final ClassInfo actualParameterClass = ((ClassTypeInfo) actualParameter
                        .getType()).getReferencedClass();

                // 仮引数が参照型でない場合は該当しない
                if (!(dummyParameter.getType() instanceof ClassTypeInfo)) {
                    return false;
                }

                // 仮引数の型のクラスを取得
                final ClassInfo dummyParameterClass = ((ClassTypeInfo) dummyParameter.getType())
                        .getReferencedClass();

                // 仮引数，実引数共に対象クラスである場合は，その継承関係を考慮する．つまり，実引数が仮引数のサブクラスでない場合は，呼び出し可能ではない
                if ((actualParameterClass instanceof TargetClassInfo)
                        && (dummyParameterClass instanceof TargetClassInfo)) {

                    // 実引数が仮引数と同じ参照型（クラス）でもなく，仮引数のサブクラスでもない場合は該当しない
                    if (actualParameterClass.equals(dummyParameterClass)) {
                        continue NEXT_PARAMETER;

                    } else if (actualParameterClass.isSubClass(dummyParameterClass)) {
                        continue NEXT_PARAMETER;

                    } else {
                        return false;
                    }

                    // 仮引数，実引数共に外部クラスである場合は，等しい場合のみ呼び出し可能とする
                } else if ((actualParameterClass instanceof ExternalClassInfo)
                        && (dummyParameterClass instanceof ExternalClassInfo)) {

                    if (actualParameterClass.equals(dummyParameterClass)) {
                        continue NEXT_PARAMETER;
                    }

                    return false;

                    // 仮引数が外部クラス，実引数が対象クラスの場合は，実引数が仮引数のサブクラスである場合，呼び出し可能とする
                } else if ((actualParameterClass instanceof TargetClassInfo)
                        && (dummyParameterClass instanceof ExternalClassInfo)) {

                    if (actualParameterClass.isSubClass(dummyParameterClass)) {
                        continue NEXT_PARAMETER;
                    }

                    return false;

                    // 仮引数が対象クラス，実引数が外部クラスの場合は，呼び出し不可能とする
                } else {
                    return false;
                }

                // 実引数がプリミティブ型の場合
            } else if (actualParameter.getType() instanceof PrimitiveTypeInfo) {

                // PrimitiveTypeInfo#equals を使って等価性の判定．
                // 等しくない場合は該当しない
                if (actualParameter.getType().equals(dummyParameter.getType())) {
                    continue NEXT_PARAMETER;
                }

                return false;

                // 実引数が配列型の場合
            } else if (actualParameter.getType() instanceof ArrayTypeInfo) {

                if (!(dummyParameter.getType() instanceof ArrayTypeInfo)) {
                    return false;
                }

                if (!actualParameter.getType().equals(dummyParameter.getType())) {
                    return false;
                }

                continue NEXT_PARAMETER;
                // TODO Java言語の場合は，仮引数が java.lang.object でもOKな処理が必要

                // 実引数が null の場合
            } else if (actualParameter.getType() instanceof NullTypeInfo) {

                // 仮引数が参照型でない場合は該当しない
                if (!(dummyParameter.getType() instanceof ClassInfo)) {
                    return false;
                }

                continue NEXT_PARAMETER;
                // TODO Java言語の場合は，仮引数が配列型の場合でもOKな処理が必要

                // 実引数の型が解決できなかった場合
            } else if (actualParameter.getType() instanceof UnknownTypeInfo) {

                // 実引数の型が不明な場合は，仮引数の型が何であろうともOKにしている
                continue NEXT_PARAMETER;

            } else {
                assert false : "Here shouldn't be reached!";
            }
        }

        return true;
    }

    /**
     * このメソッドが引数で与えられたオブジェクト（メソッド）と等しいかどうかを判定する
     * 
     * @param o 比較対象オブジェクト（メソッド）
     * @return 等しい場合は true, 等しくない場合は false
     */
    @Override
    public final boolean equals(Object o) {

        if (null == o) {
            return false;
        }

        if (!(o instanceof MethodInfo)) {
            return false;
        }

        return 0 == this.compareTo((MethodInfo) o) ? true : false;
    }

    /**
     * このメソッドのハッシュコードを返す
     * 
     * @return このメソッドのハッシュコード
     */
    @Override
    public final int hashCode() {

        final StringBuilder sb = new StringBuilder();
        sb.append(this.ownerClass.getFullQualifiedName(Settings.getLanguage()
                .getNamespaceDelimiter()));
        sb.append(this.methodName);

        return sb.toString().hashCode();
    }

    /**
     * メトリクス計測対象としての名前を返す
     * 
     * @return メトリクス計測対象としての名前
     */
    public final String getMeasuredUnitName() {

        final StringBuilder sb = new StringBuilder(this.getMethodName());
        sb.append("#");
        sb.append(this.getOwnerClass().getFullQualifiedName(
                Settings.getLanguage().getNamespaceDelimiter()));
        return sb.toString();
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

        if (null == this.returnType) {
            throw new NullPointerException();
        }

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
     * このメソッドの返り値をセットする．
     * 
     * @param returnType このメソッドの返り値
     */
    public void setReturnType(final TypeInfo returnType) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == returnType) {
            throw new NullPointerException();
        }

        this.returnType = returnType;
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
     * メソッドおよびコンストラクタ呼び出しを追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param callee 追加する呼び出されるメソッド
     */
    public void addCallee(final MemberCallInfo memberCall) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == memberCall) {
            throw new NullPointerException();
        }

        this.callees.add(memberCall);
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
     * メソッドおよびコンストラクタ呼び出し一覧を返す
     */
    public Set<MemberCallInfo> getMemberCalls() {
        return Collections.unmodifiableSet(this.callees);

    }

    /**
     * このメソッドが呼び出しているメソッドおよびコンストラクタの SortedSet を返す．
     * 
     * @return このメソッドが呼び出しているメソッドの SortedSet
     */
    public SortedSet<MethodInfo> getCallees() {
        final SortedSet<MethodInfo> callees = new TreeSet<MethodInfo>();
        for (final MemberCallInfo memberCall : this.getMemberCalls()) {
            callees.add(memberCall.getCallee());
        }
        return Collections.unmodifiableSortedSet(callees);
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
    private TypeInfo returnType;

    /**
     * このメソッドがコンストラクタかどうかを保存するための変数
     */
    private final boolean constructor;

    /**
     * 引数のリストの保存するための変数
     */
    protected final List<ParameterInfo> parameters;

    /**
     * メソッド呼び出し一覧を保存するための変数
     */
    protected final Set<MemberCallInfo> callees;

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
