package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.io.Serializable;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * プログラムユニットを表すクラス
 * 
 * @author higo
 */
public abstract class UnitInfo implements Position, Serializable {

    /**
     * 必要な情報を与えてオブジェクトを初期化
     * 
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    UnitInfo(final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        this.fromLine = fromLine;
        this.fromColumn = fromColumn;
        this.toLine = toLine;
        this.toColumn = toColumn;
    }

    /**
     * 開始行を返す
     * 
     * @return 開始行
     */
    public int getFromLine() {
        return this.fromLine;
    }

    /**
     * 開始列を返す
     * 
     * @return 開始列
     */
    public int getFromColumn() {
        return this.fromColumn;
    }

    /**
     * 終了行を返す
     * 
     * @return 終了行
     */
    public int getToLine() {
        return this.toLine;
    }

    /**
     * このユニットの行数を返す
     * 
     * @return　このユニットの行数
     */
    public final int getLOC() {
        return this.getToLine() - this.getFromLine() + 1;
    }
    
    /**
     * このユニットのシグネチャを返す
     * 
     * @return このユニットのシグネチャ
     */
    //public abstract String getSignature();

    /**
     * 終了列を返す
     * 
     * @return 終了列
     */
    public int getToColumn() {
        return this.toColumn;
    }

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
