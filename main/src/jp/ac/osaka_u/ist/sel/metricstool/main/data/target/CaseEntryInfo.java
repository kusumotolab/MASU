package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * switch 文の case エントリを表すクラス
 * 
 * @author higo
 */
public class CaseEntryInfo extends UnitInfo implements StatementInfo {

    /**
     * 対応する switch ブロック情報を与えて case エントリを初期化
     * 
     * @param ownerSwitchBlock この case エントリが属する switch ブロック
     * @param label この case エントリのラベル
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public CaseEntryInfo(final SwitchBlockInfo ownerSwitchBlock, final EntityUsageInfo label,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == ownerSwitchBlock) || (null == label)) {
            throw new IllegalArgumentException();
        }

        this.ownerSwitchBlock = ownerSwitchBlock;
        this.label = label;
    }

    /**
     * 対応する switch ブロック情報を与えて case エントリを初期化
     * 
     * @param ownerSwitchBlock この case エントリが属する switch ブロック
     * @param breakStatement この case エントリが break 文を持つかどうか
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    protected CaseEntryInfo(final SwitchBlockInfo ownerSwitchBlock, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == ownerSwitchBlock) {
            throw new IllegalArgumentException();
        }

        this.ownerSwitchBlock = ownerSwitchBlock;
        this.label = null;
    }

    /**
     * この文（case エントリ）で用いられている変数利用の一覧を返す．
     * どの変数も用いられていないので，空のsetが返される
     * 
     * @return 変数利用のSet
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return new HashSet<VariableUsageInfo<?>>();
    }

    /**
     * 比較する
     */
    @Override
    public int compareTo(StatementInfo o) {

        if (null == o) {
            throw new IllegalArgumentException();
        }

        if (this.getFromLine() < o.getFromLine()) {
            return -1;
        } else if (this.getFromLine() > o.getFromLine()) {
            return 1;
        } else if (this.getFromColumn() < o.getFromColumn()) {
            return -1;
        } else if (this.getFromColumn() > o.getFromColumn()) {
            return 1;
        } else if (this.getToLine() < o.getToLine()) {
            return -1;
        } else if (this.getToLine() > o.getToLine()) {
            return 1;
        } else if (this.getToColumn() < o.getToColumn()) {
            return -1;
        } else if (this.getToColumn() > o.getToColumn()) {
            return 1;
        }

        return 0;
    }

    /**
     * この case エントリが属する switch ブロックを返す
     * 
     * @return この case エントリが属する switch ブロック
     */
    public final SwitchBlockInfo getOwnerSwitchBlock() {
        return this.ownerSwitchBlock;
    }

    /**
     * この case エントリののラベルを返す
     * 
     * @return この case エントリのラベル
     */
    public final EntityUsageInfo getLabel() {
        return this.label;
    }

    /**
     * この case エントリが属する switch ブロックを保存するための変数
     */
    private final SwitchBlockInfo ownerSwitchBlock;

    /**
     * この case エントリのラベルを保存する変数
     */
    private EntityUsageInfo label;
}
