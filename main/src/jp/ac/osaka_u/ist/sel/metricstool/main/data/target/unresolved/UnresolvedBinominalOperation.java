package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR;


/**
 * 未解決二項演算を格納するためのクラス
 * 
 * @author y-higo
 * 
 */
public class UnresolvedBinominalOperation implements UnresolvedTypeInfo{

    /**
     * 引数なしコンストラクタ
     */
    public UnresolvedBinominalOperation() {
    }

    /**
     * 演算子と2つのオペランドを与えて初期化する
     * 
     * @param operator 演算子
     * @param firstOperand 第一（未解決）オペランド
     * @param secondOperand 第二（未解決）オペランド
     */
    public UnresolvedBinominalOperation(final OPERATOR operator,
            final UnresolvedTypeInfo firstOperand, final UnresolvedTypeInfo secondOperand) {

        if ((null == operator) || (null == firstOperand) || (null == secondOperand)) {
            throw new NullPointerException();
        }

        this.operator = operator;
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
    }
    
    /**
     * このクラスの型名を返す
     * 
     * @return このクラスの型名
     */
    public String getTypeName() {
        return "UnresolvedBinominalOperation";
    }

    /**
     * 演算子を取得する
     * 
     * @return 演算子
     */
    public OPERATOR getOperator() {
        return this.operator;
    }

    /**
     * 第一（未解決）オペランドを取得する
     * 
     * @return 第一（未解決）オペランド
     */
    public UnresolvedTypeInfo getFirstOperand() {
        return this.firstOperand;
    }

    /**
     * 第二（未解決）オペランドを取得する
     * 
     * @return 第二（未解決）オペランド
     */
    public UnresolvedTypeInfo getSecondOperand() {
        return this.secondOperand;
    }

    /**
     * 演算子をセットする
     * 
     * @param operator 演算子
     */
    public void setOperator(final OPERATOR operator) {

        if (null == operator) {
            throw new NullPointerException();
        }

        this.operator = operator;
    }

    /**
     * 第一（未解決）オペランドをセットする
     * 
     * @param firstOperand 第一（未解決）オペランド
     */
    public void setFirstOperand(final UnresolvedTypeInfo firstOperand) {

        if (null == firstOperand) {
            throw new NullPointerException();
        }

        this.firstOperand = firstOperand;
    }

    /**
     * 第二（未解決）オペランドをセットする
     * 
     * @param secondOperand 第二（未解決）オペランド
     */
    public void setSecondOperand(final UnresolvedTypeInfo secondOperand) {

        if (null == secondOperand) {
            throw new NullPointerException();
        }

        this.secondOperand = secondOperand;
    }

    private OPERATOR operator;

    private UnresolvedTypeInfo firstOperand;

    private UnresolvedTypeInfo secondOperand;
}
