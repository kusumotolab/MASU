package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


/**
 * 演算子を表すトークンクラス
 * 
 * @author kou-tngt
 *
 */
public class OperatorToken extends AstTokenAdapter {

    /**
     * キャストは演算子を表す定数インスタンス
     */
    public static final OperatorToken CAST = new OperatorToken("CAST", 2, false, false, null);

    /**
     * インクリメント演算子とデクリメント演算子を表す定数インスタンス
     */
    public static final OperatorToken INCL_AND_DECL = new OperatorToken("INCLEMENT", 1, true, true,
            null);

    /**
     * 代入演算子を表す定数インスタンス
     */
    public static final OperatorToken ASSIGN = new OperatorToken("ASSIGN", 2, true, false, null);

    /**
     * 二項演算子を表す定数インスタンス
     */
    public static final OperatorToken TWO_TERM = new OperatorToken("TWO_TERM", 2, false, true, null);

    /**
     * 単項演算子を表す定数インスタンス
     */
    public static final OperatorToken SINGLE_TERM = new OperatorToken("SINGLE_TERM", 1, false,
            true, null);

    /**
     * 参考演算子を表す定数インスタンス
     */
    public static final OperatorToken THREE_TERM = new OperatorToken("THREE_TERM", 3, true, false,
            null);

    /**
     * 比較演算子を表す定数インスタンス
     */
    public static final OperatorToken COMPARE = new OperatorToken("COMPARE", 2, false, true,
            PrimitiveTypeInfo.BOOLEAN);

    /**
     * 否定演算子を表す定数インスタンス
     */
    public static final OperatorToken NOT = new OperatorToken("NOT", 1, false, true,
            PrimitiveTypeInfo.BOOLEAN);

    /**
     * 配列記述子を表す定数インスタンス
     */
    public static final OperatorToken ARRAY = new OperatorToken("ARRAY", 2, false, true, null);

    /**
     * 演算子の文字列，扱う項の数，左辺値への参照と代入を行うかどうか，演算結果の型を指定するコンストラクタ.
     * 
     * @param text 演算子の文字列
     * @param termCount 扱う項の数
     * @param leftIsAssignmentee 左辺値への代入がある場合はtrue
     * @param leftIsReferencee 左辺値へのがある場合はtrue
     * @param specifiedResultType 演算結果の型が決まっている場合はその型を，決まっていない場合はnullを指定する
     * @throws IllegalArgumentException termCountが0以下の場合
     */
    public OperatorToken(final String text, final int termCount, final boolean leftIsAssignmentee,
            final boolean leftIsReferencee, final UnresolvedTypeInfo specifiedResultType) {
        super(text);

        if (termCount <= 0) {
            throw new IllegalArgumentException("Operator must treat one or more terms.");
        }

        this.leftIsAssignmentee = leftIsAssignmentee;
        this.leftIsReferencee = leftIsReferencee;
        this.termCount = termCount;
        this.specifiedResultType = specifiedResultType;
    }

    /**
     * この演算子が取り扱う項の数を返す.
     * @return この演算子が取り扱う項の数
     */
    public int getTermCount() {
        return this.termCount;
    }

    /**
     * 左辺値への代入があるかどうかを返す.
     * @return　左辺値への代入がある場合はtrue
     */
    @Override
    public boolean isAssignmentOperator() {
        return this.leftIsAssignmentee;
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstTokenAdapter#isOperator()
     */
    @Override
    public boolean isOperator() {
        return true;
    }

    /**
     * 左辺値が参照として利用されるかどうかを返す.
     * @return　左辺値が参照として利用される場合はtrue
     */
    public boolean isLeftTermIsReferencee() {
        return this.leftIsReferencee;
    }

    /**
     * 演算結果の型が決まっている場合はその型を返す.
     * 決まっていない場合はnullを返す.
     * @return 演算結果の型が決まっている場合はその型，決まっていない場合はnull
     */
    public UnresolvedTypeInfo getSpecifiedResultType() {
        return this.specifiedResultType;
    }

    /**
     * 左辺値への代入があるかどうかを表す
     */
    private final boolean leftIsAssignmentee;

    /**
     * 左辺値が参照として利用されるかどうかを表す
     */
    private final boolean leftIsReferencee;

    /**
     * この演算子が取り扱う項の数
     */
    private final int termCount;

    /**
     * 演算結果の型を表す.
     * 決まっていない場合はnull.
     */
    private final UnresolvedTypeInfo specifiedResultType;

}
