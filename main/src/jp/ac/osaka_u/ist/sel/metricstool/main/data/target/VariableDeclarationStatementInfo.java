package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


public class VariableDeclarationStatementInfo implements StatementInfo {

    /**
     * 宣言されている変数，位置情報を与えて初期化
     * 宣言されている変数が初期化されていない場合，このコンストラクタを使用する
     * 
     * @param declaredVariable 宣言されているローカル変数
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public VariableDeclarationStatementInfo(final LocalVariableInfo declaredVariable,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        if (null == declaredVariable) {
            throw new IllegalArgumentException("declaredVariable is null");
        }

        this.declaredLocalVarialbe = declaredVariable;
        this.initializationExpression = null;

        this.fromLine = fromLine;
        this.fromColumn = fromColumn;
        this.toLine = toLine;
        this.toColumn = toColumn;
    }

    /**
     * 宣言されている変数，初期化式，位置情報を与えて初期化
     * 宣言されている変数が初期化されている場合，このコンストラクタを使用する
     * 
     * @param declaredVariable 宣言されているローカル変数
     * @param initializationExpression 初期化式
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public VariableDeclarationStatementInfo(final LocalVariableInfo declaredVariable,
            final ExpressionInfo initializationExpression, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        if (null == declaredVariable) {
            throw new IllegalArgumentException("declaredVariable is null");
        }

        this.declaredLocalVarialbe = declaredVariable;
        this.initializationExpression = initializationExpression;

        this.fromLine = fromLine;
        this.fromColumn = fromColumn;
        this.toLine = toLine;
        this.toColumn = toColumn;

    }

    @Override
    public final int compareTo(StatementInfo o) {

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
    public final int getFromColumn() {
        return this.fromColumn;
    }

    @Override
    public final int getFromLine() {
        return this.fromLine;
    }

    @Override
    public final int getToColumn() {
        return this.toColumn;
    }

    @Override
    public final int getToLine() {
        return this.toLine;
    }

    public final LocalVariableInfo getDeclaredLocalVariable() {
        return this.declaredLocalVarialbe;
    }

    /**
     * 宣言されている変数の初期化式を返す
     * 
     * @return 宣言されている変数の初期化式．初期化されてい場合はnull
     */
    public final ExpressionInfo getInitializationExpression() {
        return this.initializationExpression;
    }

    /**
     * 宣言されている変数が初期化されているかどうかを返す
     * 
     * @return 宣言されている変数が初期化されていればtrue
     */
    public boolean isInitialized() {
        return null != this.initializationExpression;
    }

    /**
     * 宣言されている変数を表すフィールド
     */
    private final LocalVariableInfo declaredLocalVarialbe;

    /**
     * 宣言されている変数の初期化式を表すフィールド
     */
    private final ExpressionInfo initializationExpression;

    /**
     * 開始行を表す変数
     */
    private final int fromLine;

    /**
     * 開始列を表す変数
     */
    private final int fromColumn;

    /**
     * 終了行を表す変数
     */
    private final int toLine;

    /**
     * 終了列を表す変数
     */
    private final int toColumn;
}
