package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;


/**
 * 未解決のクラス参照，メソッド呼び出し，フィールド使用などを現すクラスの共通の基底クラス
 * 
 * @author higo
 */
public abstract class UnresolvedEntityUsageInfo<T extends EntityUsageInfo> implements
        Resolvable<T>, PositionSetting {

    protected UnresolvedEntityUsageInfo() {
        this.fromLine = 0;
        this.fromColumn = 0;
        this.toLine = 0;
        this.toColumn = 0;
        
        this.resolvedInfo = null;
    }

    /**
     * 既に解決済みかどうかを返す．
     * 
     * @return 解決済みである場合は true，そうでない場合は false
     */
    @Override
    public final boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * 解決済みクラス参照を返す
     * 
     * @return 解決済みクラス参照
     * @throws NotResolvedException 解決されていない場合にスローされる
     */
    @Override
    public final T getResolved() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }
    
    /**
     * 開始行をセットする
     * 
     * @param fromLine 開始行
     */
    public final void setFromLine(final int fromLine) {

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
     * 解決済み情報を保存するための変数
     */
    protected T resolvedInfo;
    
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
}
