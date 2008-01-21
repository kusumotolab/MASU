package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * if文やwhile文などのメソッド内の構造（ブロック）を表すためのクラス
 * 
 * @author higo
 * 
 */
public abstract class UnresolvedBlockInfo<T extends BlockInfo> implements PositionSetting,
        UnresolvedUnitInfo<T> {

    /**
     * ブロック構造を表すオブジェクトを初期化する
     * 
     */
    public UnresolvedBlockInfo() {

        MetricsToolSecurityManager.getInstance().checkAccess();

        this.methodCalls = new HashSet<UnresolvedMethodCallInfo>();
        this.fieldUsages = new HashSet<UnresolvedFieldUsageInfo>();
        this.localVariables = new HashSet<UnresolvedLocalVariableInfo>();
        this.innerBlocks = new HashSet<UnresolvedBlockInfo<?>>();

        this.fromLine = 0;
        this.fromColumn = 0;
        this.toLine = 0;
        this.toColumn = 0;
    }

    /**
     * このブロックが既に解決されているかどうかをかえす
     * 
     * @return 既に解決されている場合は true, そうでない場合は false
     */
    public final boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    public final T getResolvedUnit() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    /**
     * メソッド呼び出しを追加する
     * 
     * @param methodCall メソッド呼び出し
     */
    public final void addMethodCall(final UnresolvedMethodCallInfo methodCall) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == methodCall) {
            throw new NullPointerException();
        }

        this.methodCalls.add(methodCall);
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

    /**
     * このブロック内で行われている未解決メソッド呼び出しの Set を返す
     * 
     * @return このブロック内で行われている未解決メソッド呼び出しの Set
     */
    public final Set<UnresolvedMethodCallInfo> getMethodCalls() {
        return Collections.unmodifiableSet(this.methodCalls);
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
     * 開始行をセットする
     * 
     * @param fromLine 開始行
     */
    public final void setFromLine(final int fromLine) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (fromLine < 0) {
            throw new IllegalArgumentException();
        }

        this.fromLine = fromLine;
    }

    /**
     * 開始列をセットする
     * 
     * @param fromColumn 開始列
     */
    public final void setFromColumn(final int fromColumn) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (fromColumn < 0) {
            throw new IllegalArgumentException();
        }

        this.fromColumn = fromColumn;
    }

    /**
     * 終了行をセットする
     * 
     * @param toLine 終了行
     */
    public final void setToLine(final int toLine) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (toLine < 0) {
            throw new IllegalArgumentException();
        }

        this.toLine = toLine;
    }

    /**
     * 終了列をセットする
     * 
     * @param toColumn 終了列
     */
    public final void setToColumn(final int toColumn) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (toColumn < 0) {
            throw new IllegalArgumentException();
        }

        this.toColumn = toColumn;
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
     * メソッド呼び出しを保存する変数
     */
    private final Set<UnresolvedMethodCallInfo> methodCalls;

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

    /**
     * 開始行を保存するための変数
     */
    private int fromLine;

    /**
     * 開始列を保存するための変数
     */
    private int fromColumn;

    /**
     * 終了行を保存するための変数
     */
    private int toLine;

    /**
     * 開始列を保存するための変数
     */
    private int toColumn;

    /**
     * 解決済みブロック情報を保存するための変数
     */
    protected T resolvedInfo;
}
