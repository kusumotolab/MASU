package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 変数の使用やメソッドの呼び出しなど，プログラム要素の使用を表すインターフェース
 * 
 * @author higo
 *
 */
public abstract class EntityUsageInfo implements Position{

    /**
     * オブジェクトを初期化 
     */
    EntityUsageInfo(final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        this.fromLine = fromLine;
        this.fromColumn = fromColumn;
        this.toLine = toLine;
        this.toColumn = toColumn;
    }

    /**
     * エンティティ使用の型を返す．
     * 
     * @return エンティティ使用の型
     */
    public abstract TypeInfo getType();

    /**
     * 開始行を返す
     * 
     * @return 開始行
     */
    @Override
    public final int getFromLine() {
        return this.fromLine;
    }

    /**
     * 開始列を返す
     * 
     * @return 開始列
     */
    @Override
    public final int getFromColumn() {
        return this.fromColumn;
    }

    /**
     * 終了行を返す
     * 
     * @return 終了行
     */
    @Override
    public final int getToLine() {
        return this.toLine;
    }

    /**
     * 終了列を返す
     * 
     * @return 終了列
     */
    @Override
    public final int getToColumn() {
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
