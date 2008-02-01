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

    LocalSpaceInfo(final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        this.localVariables = new TreeSet<LocalVariableInfo>();
        this.fieldUsages = new HashSet<FieldUsageInfo>();
        this.innerBlocks = new TreeSet<BlockInfo>();
        this.callees = new HashSet<MemberCallInfo>();
    }

    /**
     * メソッドおよびコンストラクタ呼び出しを追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param callee 追加する呼び出されるメソッド
     */
    public final void addCallee(final MemberCallInfo memberCall) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == memberCall) {
            throw new NullPointerException();
        }

        this.callees.add(memberCall);
    }

    /**
     * このメソッドで定義されているローカル変数を追加する． public 宣言してあるが， プラグインからの呼び出しははじく．
     * 
     * @param localVariable 追加する引数
     */
    public void addLocalVariable(final LocalVariableInfo localVariable) {

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
    public void addFieldUsage(final FieldUsageInfo fieldUsage) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fieldUsage) {
            throw new NullPointerException();
        }

        this.fieldUsages.add(fieldUsage);
    }

    /**
     * このメソッドの直内ブロックを追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param innerBlock 追加する直内ブロック
     */
    public void addInnerBlock(final BlockInfo innerBlock) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == innerBlock) {
            throw new NullPointerException();
        }

        this.innerBlocks.add(innerBlock);
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
     * このメソッドの直内ブロックの SortedSet を返す．
     * 
     * @return このメソッドの直内ブロックの SortedSet を返す．
     */
    public SortedSet<BlockInfo> getInnerBlocks() {
        return Collections.unmodifiableSortedSet(this.innerBlocks);
    }

    /**
     * メソッド呼び出し一覧を保存するための変数
     */
    protected final Set<MemberCallInfo> callees;

    /**
     * このメソッドの内部で定義されているローカル変数
     */
    private final SortedSet<LocalVariableInfo> localVariables;

    /**
     * 利用しているフィールド一覧を保存するための変数
     */
    private final Set<FieldUsageInfo> fieldUsages;

    /**
     * このメソッド直内のブロック一覧を保存するための変数
     */
    private final SortedSet<BlockInfo> innerBlocks;
}
