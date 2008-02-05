package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
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

    public UnresolvedLocalSpaceInfo() {

        MetricsToolSecurityManager.getInstance().checkAccess();

        this.memberCalls = new HashSet<UnresolvedCallInfo>();
        this.fieldUsages = new HashSet<UnresolvedFieldUsageInfo>();
        this.localVariables = new HashSet<UnresolvedLocalVariableInfo>();
        this.innerBlocks = new HashSet<UnresolvedBlockInfo<?>>();
    }

    /**
     * メソッドまたはコンストラクタ呼び出しを追加する
     * 
     * @param memberCall メソッドまたはコンストラクタ呼び出し
     */
    public final void addMemberCall(final UnresolvedCallInfo memberCall) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == memberCall) {
            throw new NullPointerException();
        }

        this.memberCalls.add(memberCall);
    }

    /**
     * フィールド使用を追加する
     * 
     * @param fieldUsage フィールド使用
     */
    public final void addFieldUsage(final UnresolvedFieldUsageInfo fieldUsage) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fieldUsage) {
            throw new NullPointerException();
        }

        this.fieldUsages.add(fieldUsage);
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
     * インナーブロックを追加する
     * 
     * @param innerBlock ローカル変数
     */
    public void addInnerBlock(final UnresolvedBlockInfo<?> innerBlock) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == innerBlock) {
            throw new NullPointerException();
        }

        this.innerBlocks.add(innerBlock);
    }

    public void addChildSpaceInfo(final UnresolvedLocalSpaceInfo<?> childLocalInfo){
    	MetricsToolSecurityManager.getInstance().checkAccess();
    	if (null == childLocalInfo) {
    		throw new NullPointerException();
    	}
    	
    	this.fieldUsages.addAll(childLocalInfo.fieldUsages);
    	this.localVariables.addAll(childLocalInfo.localVariables);
    	this.memberCalls.addAll(childLocalInfo.memberCalls);
    }
    
    /**
     * このブロック内で行われている未解決メソッド呼び出しの Set を返す
     * 
     * @return このブロック内で行われている未解決メソッド呼び出しの Set
     */
    public final Set<UnresolvedCallInfo> getMethodCalls() {
        return Collections.unmodifiableSet(this.memberCalls);
    }

    /**
     * このブロック内で行われている未解決フィールド使用の Set を返す
     * 
     * @return このブロック内で行われている未解決フィールド使用の Set
     */
    public final Set<UnresolvedFieldUsageInfo> getFieldUsages() {
        return Collections.unmodifiableSet(this.fieldUsages);
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
    public final Set<UnresolvedBlockInfo<?>> getInnerBlocks() {
        return Collections.unmodifiableSet(this.innerBlocks);
    }

    /**
     * メソッドまたはコンストラクタ呼び出しを保存する変数
     */
    private final Set<UnresolvedCallInfo> memberCalls;

    /**
     * フィールド使用を保存する変数
     */
    private final Set<UnresolvedFieldUsageInfo> fieldUsages;

    /**
     * このメソッド内で定義されているローカル変数を保存する変数
     */
    private final Set<UnresolvedLocalVariableInfo> localVariables;

    /**
     * このブロックの内側で定義されたブロックを保存する変数
     */
    private final Set<UnresolvedBlockInfo<?>> innerBlocks;

}
