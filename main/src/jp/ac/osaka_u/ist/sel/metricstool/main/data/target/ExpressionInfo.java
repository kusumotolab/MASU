package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 変数の使用やメソッドの呼び出しなど，プログラム要素の使用を表すクラス
 * 
 * @author higo
 *
 */
public abstract class ExpressionInfo implements ConditionInfo {

    /**
     * 
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    ExpressionInfo(final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        this.ownerExecutableElement = null;
        this.fromLine = fromLine;
        this.fromColumn = fromColumn;
        this.toLine = toLine;
        this.toColumn = toColumn;
    }

    abstract public String getText();

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

    @Override
    public final int compareTo(ExecutableElementInfo o) {

        if (null == o) {
            throw new NullPointerException();
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

    public final ExecutableElementInfo getOwnerExecutableElement() {

        if (null == this.ownerExecutableElement) {
            throw new IllegalStateException();
        }

        return this.ownerExecutableElement;
    }

    public final StatementInfo getOwnerStatement() {

        final ExecutableElementInfo ownerExecutableElement = this.getOwnerExecutableElement();
        if (ownerExecutableElement instanceof StatementInfo) {
            return (StatementInfo) ownerExecutableElement;
        }

        if (ownerExecutableElement instanceof ExpressionInfo) {
            return ((ExpressionInfo) ownerExecutableElement).getOwnerStatement();
        }

        // ownerExecutableElement が StatementInfo でも ExpressionInfo　でもないときはIllegalStateException
        throw new IllegalStateException(
                "ownerExecutableElement must be StatementInfo or ExpressionInfo.");
    }

    /**
     * 直接のオーナーであるExecutableElementをセットする
     * 
     * @param ownerExecutableElement 直接のオーナーであるExecutableElement
     */
    public final void setOwnerExecutableElement(final ExecutableElementInfo ownerExecutableElement) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == ownerExecutableElement) {
            throw new IllegalArgumentException();
        }

        this.ownerExecutableElement = ownerExecutableElement;
    }

    private ExecutableElementInfo ownerExecutableElement;

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
