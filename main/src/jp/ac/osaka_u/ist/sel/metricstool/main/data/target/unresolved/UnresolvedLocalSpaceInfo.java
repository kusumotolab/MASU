package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ローカル領域(メソッドとメソッド内のブロック)を表すクラス
 * 
 * @author higo
 *
 * @param <T>
 */
public abstract class UnresolvedLocalSpaceInfo<T extends LocalSpaceInfo> extends
        UnresolvedUnitInfo<T> {

    /**
     * 位置情報を与えて初期化
     */
    public UnresolvedLocalSpaceInfo() {
        
        MetricsToolSecurityManager.getInstance().checkAccess();

        this.calls = new HashSet<UnresolvedCallInfo<?>>();
        this.variableUsages = new HashSet<UnresolvedVariableUsageInfo<VariableUsageInfo<?>>>();
        this.localVariables = new HashSet<UnresolvedLocalVariableInfo>();
        this.statements = new HashSet<UnresolvedStatementInfo<?>>();
    }

    /**
     * メソッドまたはコンストラクタ呼び出しを追加する
     * 
     * @param call メソッドまたはコンストラクタ呼び出し
     */
    public final void addCall(final UnresolvedCallInfo<?> call) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == call) {
            throw new NullPointerException();
        }

        this.calls.add(call);
    }

    /**
     * 変数使用を追加する
     * 
     * @param variableUsage 変数使用
     */
    public final void addVariableUsage(final UnresolvedVariableUsageInfo<VariableUsageInfo<?>> variableUsage) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == variableUsage) {
            throw new NullPointerException();
        }

        this.variableUsages.add(variableUsage);
    }

    /**
     * ローカル変数を追加する
     * 
     * @param localVariable ローカル変数
     */
    public final void addLocalVariable(final UnresolvedLocalVariableInfo localVariable) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == localVariable) {
            throw new NullPointerException();
        }

        this.localVariables.add(localVariable);
    }

    /**
     * 未解決文を追加する
     * 
     * @param statement 未解決文
     */
    public void addStatement(final UnresolvedStatementInfo<?> statement) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == statement) {
            throw new NullPointerException();
        }

        this.statements.add(statement);
    }

    public void addChildSpaceInfo(final UnresolvedLocalSpaceInfo<?> childLocalInfo) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == childLocalInfo) {
            throw new NullPointerException();
        }

        this.variableUsages.addAll(childLocalInfo.variableUsages);
        this.localVariables.addAll(childLocalInfo.localVariables);
        this.calls.addAll(childLocalInfo.calls);
    }

    /**
     * このブロック内で行われている未解決メソッド呼び出しおよびコンストラクタ呼び出しの Set を返す
     * 
     * @return このブロック内で行われている未解決メソッド呼び出しおよびコンストラクタ呼び出しの Set
     */
    public final Set<UnresolvedCallInfo<?>> getCalls() {
        return Collections.unmodifiableSet(this.calls);
    }

    /**
     * このブロック内で行われている未解決変数使用の Set を返す
     * 
     * @return このブロック内で行われている未解決変数使用の Set
     */
    public final Set<UnresolvedVariableUsageInfo<VariableUsageInfo<?>>> getVariableUsages() {
        return Collections.unmodifiableSet(this.variableUsages);
    }

    /**
     * このブロック内で定義されている未解決ローカル変数の Set を返す
     * 
     * @return このブロック内で定義されている未解決ローカル変数の Set
     */
    public final Set<UnresolvedLocalVariableInfo> getLocalVariables() {
        return Collections.unmodifiableSet(this.localVariables);
    }

    /**
     * このブロック内の未解決内部ブロックの Set を返す
     * 
     * @return このブロック内の未解決内部ブロックの Set
     */
    public final Set<UnresolvedStatementInfo<?>> getStatements() {
        return Collections.unmodifiableSet(this.statements);
    }
    
    protected final void resolveVariableUsages(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {
        
        if(!alreadyResolved()) {
            throw new NotResolvedException();
        }
        
        for (final UnresolvedVariableUsageInfo<VariableUsageInfo<?>> unresolvedVariableUsage : this.getVariableUsages()) {

            final VariableUsageInfo<?> variableUsage = unresolvedVariableUsage.resolve(usingClass,
                    usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
            this.resolvedInfo.addVariableUsage(variableUsage);
        }
    }

    /**
     * メソッドまたはコンストラクタ呼び出しを保存する変数
     */
    private final Set<UnresolvedCallInfo<?>> calls;

    /**
     * フィールド使用を保存する変数
     */
    private final Set<UnresolvedVariableUsageInfo<VariableUsageInfo<?>>> variableUsages;

    /**
     * このメソッド内で定義されているローカル変数を保存する変数
     */
    private final Set<UnresolvedLocalVariableInfo> localVariables;

    /**
     * このブロックの内側で定義された未解決文を保存する変数
     */
    private final Set<UnresolvedStatementInfo<?>> statements;

}
