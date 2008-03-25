package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ローカル領域(メソッドやメソッド内ブロック)を表すクラス
 * 
 * @author higo
 *
 */
public abstract class LocalSpaceInfo extends UnitInfo {

    LocalSpaceInfo(final ClassInfo ownerClass, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        this.ownerClass = ownerClass;
        this.localVariables = new TreeSet<LocalVariableInfo>();
        this.fieldUsages = new HashSet<FieldUsageInfo>();
        this.localVariableUsages = new HashSet<LocalVariableUsageInfo>();
        this.parameterUsages = new HashSet<ParameterUsageInfo>();
        this.statements = new TreeSet<StatementInfo>();
        this.calls = new HashSet<CallInfo>();
    }

    /**
     * メソッドおよびコンストラクタ呼び出しを追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param memberCall 追加するメソッドおよびコンストラクタ呼び出し
     */
    public final void addCall(final CallInfo memberCall) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == memberCall) {
            throw new NullPointerException();
        }

        this.calls.add(memberCall);
    }

    /**
     * このメソッドで定義されているローカル変数を追加する． public 宣言してあるが， プラグインからの呼び出しははじく．
     * 
     * @param localVariable 追加する引数
     */
    public final void addLocalVariable(final LocalVariableInfo localVariable) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == localVariable) {
            throw new NullPointerException();
        }

        this.localVariables.add(localVariable);
    }

    /**
     * このメソッドが参照している変数を追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param fieldUsage 追加するフィールド利用
     */
    public final void addVariableUsage(final VariableUsageInfo<?> variableUsage) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == variableUsage) {
            throw new NullPointerException();
        }

        if (variableUsage instanceof FieldUsageInfo) {
            this.fieldUsages.add((FieldUsageInfo) variableUsage);
        } else if (variableUsage instanceof LocalVariableUsageInfo) {
            this.localVariableUsages.add((LocalVariableUsageInfo) variableUsage);
        } else if (variableUsage instanceof ParameterUsageInfo) {
            this.parameterUsages.add((ParameterUsageInfo) variableUsage);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * このローカルコープの直内の式を追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param innerBlock 追加する直内ブロック
     */
    public void addStatement(final StatementInfo statement) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == statement) {
            throw new NullPointerException();
        }

        this.statements.add(statement);
    }

    /**
     * メソッドおよびコンストラクタ呼び出し一覧を返す
     */
    public Set<CallInfo> getCalls() {
        return Collections.unmodifiableSet(this.calls);

    }

    /**
     * このメソッドが呼び出しているメソッドの一覧を返す
     * 
     * @return このメソッドが呼び出しているメソッドの SortedSet
     */
    public SortedSet<MethodInfo> getCallees() {
        final SortedSet<MethodInfo> callees = new TreeSet<MethodInfo>();
        for (final CallInfo call : this.getCalls()) {
            if (call instanceof MethodCallInfo) {
                callees.add(((MethodCallInfo) call).getCallee());
            }
        }
        return Collections.unmodifiableSortedSet(callees);
    }

    /**
     * このメソッドで定義されているローカル変数の SortedSet を返す．
     * 
     * @return このメソッドで定義されているローカル変数の SortedSet
     */
    public SortedSet<LocalVariableInfo> getLocalVariables() {
        return Collections.unmodifiableSortedSet(this.localVariables);
    }

    /**
     * このメソッドのフィールド利用のSetを返す
     */
    public Set<FieldUsageInfo> getFieldUsages() {
        return Collections.unmodifiableSet(this.fieldUsages);
    }

    /**
     * このメソッドのローカル変数利用のSetを返す
     */
    public Set<LocalVariableUsageInfo> getLocalVariableUsages() {
        return Collections.unmodifiableSet(this.localVariableUsages);
    }
    
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        final SortedSet<VariableUsageInfo<?>> variableUsages = new TreeSet<VariableUsageInfo<?>>();
        variableUsages.addAll(this.getLocalVariableUsages());
        variableUsages.addAll(this.getFieldUsages());
        variableUsages.addAll(this.getParameterUsages());
        return Collections.unmodifiableSortedSet(variableUsages);
    }

    /**
     * このメソッドのフィールド利用のSetを返す
     */
    public Set<ParameterUsageInfo> getParameterUsages() {
        return Collections.unmodifiableSet(this.parameterUsages);
    }

    /**
     * このローカルスペースの直内の文情報の SortedSet を返す．
     * 
     * @return このローカルスペースの直内の文情報の SortedSet
     */
    public SortedSet<StatementInfo> getStatements() {
        return Collections.unmodifiableSortedSet(this.statements);
    }

    /**
     * 所属しているクラスを返す
     * 
     * @return 所属しているクラス
     */
    public final ClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * メソッド呼び出し一覧を保存するための変数
     */
    protected final Set<CallInfo> calls;

    /**
     * このメソッドの内部で定義されているローカル変数
     */
    private final SortedSet<LocalVariableInfo> localVariables;

    /**
     * 利用しているフィールド一覧を保存するための変数
     */
    private final Set<FieldUsageInfo> fieldUsages;

    /**
     * 利用しているローカル変数の一覧を保存するための変数
     */
    private final Set<LocalVariableUsageInfo> localVariableUsages;

    /**
     * 利用している引数の一覧を保存するための変数
     */
    private final Set<ParameterUsageInfo> parameterUsages;

    /**
     * このローカルスコープの直内の文情報一覧を保存するための変数
     */
    private final SortedSet<StatementInfo> statements;

    /**
     * 所属しているクラスを保存するための変数
     */
    private final ClassInfo ownerClass;
}
