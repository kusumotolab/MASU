package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * 変数宣言文の情報を保有するクラス
 * 
 * @author t-miyake
 *
 */
public class VariableDeclarationStatementInfo extends SingleStatementInfo {

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
        super(fromLine, fromColumn, toLine, toColumn);
        
        if (null == declaredVariable) {
            throw new IllegalArgumentException("declaredVariable is null");
        }

        this.declaredLocalVarialbe = declaredVariable;
        this.initializationExpression = initializationExpression;

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

}
