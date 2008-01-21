package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * if ブロックや for ブロックなど メソッド内の構造的なまとまりの単位を表す抽象クラス
 * 
 * @author higo
 */
public abstract class BlockInfo implements UnitInfo, Position, Comparable<BlockInfo> {

    /**
     * 位置情報を与えて初期化
     * 
     * @param ownerClass このブロックを所有するクラス
     * @param ownerMethod このブロックを所有するメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    BlockInfo(final TargetClassInfo ownerClass, final TargetMethodInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == ownerClass) || (null == ownerMethod)) {
            throw new NullPointerException();
        }

        this.ownerClass = ownerClass;
        this.ownerMethod = ownerMethod;
        this.fromLine = fromLine;
        this.fromColumn = fromColumn;
        this.toLine = toLine;
        this.toColumn = toColumn;

        this.assignmentees = new TreeSet<FieldInfo>();
        this.referencees = new TreeSet<FieldInfo>();
        this.localVariables = new TreeSet<LocalVariableInfo>();
        this.callees = new TreeSet<MethodInfo>();
        this.innerBlocks = new TreeSet<BlockInfo>();
    }

    public final int compareTo(BlockInfo o) {

        if (null == o) {
            throw new NullPointerException();
        }

        if (this.getFromLine() < o.getFromLine()) {
            return 1;
        } else if (this.getFromLine() > o.getFromLine()) {
            return -1;
        } else if (this.getFromColumn() < o.getFromColumn()) {
            return 1;
        } else if (this.getFromColumn() > o.getFromColumn()) {
            return -1;
        } else if (this.getToLine() < o.getToLine()) {
            return 1;
        } else if (this.getToLine() > o.getToLine()) {
            return -1;
        } else if (this.getToColumn() < o.getToColumn()) {
            return 1;
        } else if (this.getToColumn() > o.getToColumn()) {
            return -1;
        }

        return 0;
    }

    @Override
    public final boolean equals(Object o) {

        if (null == o) {
            return false;
        }

        if (!(o instanceof BlockInfo)) {
            return false;
        }

        return 0 == this.compareTo((BlockInfo) o);
    }

    @Override
    public final int hashCode() {
        return this.getFromLine() + this.getFromColumn() + this.getToLine() + this.getToColumn();
    }

    /**
     * このブロックを所有するクラスを返す
     * 
     * @return このブロックを所有するクラス
     */
    public final TargetClassInfo getOwnerClass() {
        return this.ownerClass;
    }

    /**
     * このブロックを所有するを返す
     * 
     * @return このブロックを所有するメソッド
     */
    public final TargetMethodInfo getOwnerMethod() {
        return this.ownerMethod;
    }

    /**
     * 開始行を返す
     * 
     * @return 開始行
     */
    public final int getFromLine() {
        return this.fromLine;
    }

    /**
     * 開始列を返す
     * 
     * @return 開始列
     */
    public final int getFromColumn() {
        return this.fromColumn;
    }

    /**
     * 終了行を返す
     * 
     * @return 終了行
     */
    public final int getToLine() {
        return this.toLine;
    }

    /**
     * 終了列を返す
     * 
     * @return 終了列
     */
    public final int getToColumn() {
        return this.toColumn;
    }

    /**
     * このブロックで定義されているローカル変数を追加する． public 宣言してあるが， プラグインからの呼び出しははじく．
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
     * このブロックが参照している変数を追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param referencee 追加する参照されている変数
     */
    public final void addReferencee(final FieldInfo referencee) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == referencee) {
            throw new NullPointerException();
        }

        this.referencees.add(referencee);
    }

    /**
     * このブロックが代入を行っている変数を追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param assignmentee 追加する代入されている変数
     */
    public final void addAssignmentee(final FieldInfo assignmentee) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == assignmentee) {
            throw new NullPointerException();
        }

        this.assignmentees.add(assignmentee);
    }

    /**
     * このブロックが呼び出しているメソッドを追加する．プラグインから呼ぶとランタイムエラー．
     * 
     * @param callee 追加する呼び出されるメソッド
     */
    public final void addCallee(final MethodInfo callee) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == callee) {
            throw new NullPointerException();
        }

        this.callees.add(callee);
    }

    /**
     * このブロックの直内のブロックを追加する．プラグインから呼ぶとランタイムエラー．
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
     * このブロックが参照しているフィールドの SortedSet を返す．
     * 
     * @return このブロックが参照しているフィールドの SortedSet
     */
    public final SortedSet<FieldInfo> getReferencees() {
        return Collections.unmodifiableSortedSet(this.referencees);
    }

    /**
     * このブロックが代入しているフィールドの SortedSet を返す．
     * 
     * @return このブロックが代入しているフィールドの SortedSet
     */
    public final SortedSet<FieldInfo> getAssignmentees() {
        return Collections.unmodifiableSortedSet(this.assignmentees);
    }

    /**
     * このブロックで定義されているローカル変数の SortedSet を返す．
     * 
     * @return このブロックで定義されているローカル変数の SortedSet
     */
    public final SortedSet<LocalVariableInfo> getLocalVariables() {
        return Collections.unmodifiableSortedSet(this.localVariables);
    }

    /**
     * このブロック内で呼び出されているメソッドの SortedSet を返す．
     * 
     * @return このブロック内で呼び出されているメソッドの SortedSet
     */
    public final SortedSet<MethodInfo> getCallees() {
        return Collections.unmodifiableSortedSet(this.callees);
    }

    /**
     * このブロックの直内のブロックの SortedSet を返す．
     * 
     * @return このブロックの直内のブロックの SortedSet
     */
    public final SortedSet<BlockInfo> getInnerBlocks() {
        return Collections.unmodifiableSortedSet(this.innerBlocks);
    }

    /**
     * このブロックの内部で定義されているローカル変数
     */
    private final SortedSet<LocalVariableInfo> localVariables;

    /**
     * 参照しているフィールド一覧を保存するための変数
     */
    private final SortedSet<FieldInfo> referencees;

    /**
     * 代入しているフィールド一覧を保存するための変数
     */
    private final SortedSet<FieldInfo> assignmentees;

    /**
     * 呼び出しているメソッド一覧を保存するための変数
     */
    private final SortedSet<MethodInfo> callees;

    /**
     * このブロックの直下のブロックを保存するための変数
     */
    private final SortedSet<BlockInfo> innerBlocks;

    /**
     * このブロックを所有するクラスを保存するための変数
     */
    private final TargetClassInfo ownerClass;

    /**
     * このブロックを所有するメソッドを保存するための変数
     */
    private final TargetMethodInfo ownerMethod;

    /**
     * 開始行を保存するための変数
     */
    private final int fromLine;

    /**
     * 開始列を保存するための変数
     */
    private final int fromColumn;

    /**
     * 終了行を保存するための変数
     */
    private final int toLine;

    /**
     * 開始列を保存するための変数
     */
    private final int toColumn;
}
