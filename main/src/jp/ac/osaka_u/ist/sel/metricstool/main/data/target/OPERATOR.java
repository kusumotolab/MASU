package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import static jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR_TYPE.*;


/**
 * 演算子を表す列挙型
 * 
 * @author t-miyake
 *
 */
public enum OPERATOR {

    /**
     * 算術演算子"+"
     */
    PLUS(ARITHMETIC, "+"),

    /**
     * 算術演算子"-"
     */
    MINUS(ARITHMETIC, "-"),

    /**
     * 算術演算子"*"
     */
    STAR(ARITHMETIC, "*"),

    /**
     * 算術演算子"/"
     */
    DIV(ARITHMETIC, "/"),

    /**
     * 算術演算子"%"
     */
    MOD(ARITHMETIC, "%"),

    /**
     * 比較演算子"=="
     */
    EQUAL(COMPARATIVE, "=="),

    /**
     * 比較演算子"!="
     */
    NOT_EQUAL(COMPARATIVE, "!="),

    /**
     * 比較演算子"<"
     */
    LT(COMPARATIVE, "<"),

    /**
     * 比較演算子">"
     */
    GT(COMPARATIVE, ">"),

    /**
     * 比較演算子"<="
     */
    LE(COMPARATIVE, "<="),

    /**
     * 比較演算子">="
     */
    GE(COMPARATIVE, ">="),
    
    /**
     * instanceof演算子
     */
    INSTANCEOF(COMPARATIVE, "instanceof"),

    /**
     * 論理演算子"&&"
     */
    LAND(LOGICAL, "&&"),

    /**
     * 論理演算子"||"
     */
    LOR(LOGICAL, "||"),

    /**
     * 論理演算子"!"
     */
    LNOT(LOGICAL, "!"),

    /**
     * ビット演算子"&"
     */
    BAND(BITS, "&"),

    /**
     * ビット演算子"|"
     */
    BOR(BITS, "|"),

    /**
     * ビット演算子"^"
     */
    BXOR(BITS, "^"),

    /**
     * ビット演算子"~"
     */
    BNOT(BITS, "~"),

    /**
     * シフト演算子"<<"
     */
    SL(SHIFT, "<<"),

    /**
     * シフト演算子">>"
     */
    SR(SHIFT, ">>"),

    /**
     * シフト演算子">>>"
     */
    BSR(SHIFT, ">>>"),

    /**
     * 代入演算子"="
     */
    ASSIGN(ASSIGNMENT, "="),

    /**
     * 代入演算子"+="
     */
    PLUS_ASSIGN(ASSIGNMENT, "+="),

    /**
     * 代入演算子"-="
     */
    MINUS_ASSIGN(ASSIGNMENT, "-="),

    /**
     * 代入演算子"*="
     */
    STAR_ASSIGN(ASSIGNMENT, "*="),

    /**
     * 代入演算子"/="
     */
    DIV_ASSIGN(ASSIGNMENT, "/="),

    /**
     * 代入演算子"%="
     */
    MOD_ASSIGN(ASSIGNMENT, "%="),

    /**
     * 代入演算子"&="
     */
    BAND_ASSIGN(ASSIGNMENT, "&="),

    /**
     * 代入演算子"|="
     */
    BOR_ASSIGN(ASSIGNMENT, "|="),

    /**
     * 代入演算子"^="
     */
    BXOR_ASSIGN(ASSIGNMENT, "^="),

    /**
     * 代入演算子"<<="
     */
    SL_ASSIGN(ASSIGNMENT, "<<="),

    /**
     * 代入演算子">>="
     */
    SR_ASSIGN(ASSIGNMENT, ">>="),

    /**
     * 代入演算子">>>="
     */
    BSR_ASSIGN(ASSIGNMENT, ">>>="),
    
    ;

    //    INC             :   "++"    ;

    //    DEC             :   "--"    ;

    /**
     * 演算子のタイプとトークンを与えて初期化
     * 
     * @param operatorType 演算子のタイプ
     * @param token 演算子のトークン
     */
    private OPERATOR(final OPERATOR_TYPE operatorType, final String token) {
        this.operatorType = operatorType;
        this.token = token;
    }

    /**
     * 演算子のタイプを返す
     * 
     * @return 演算子のタイプ
     */
    public OPERATOR_TYPE getOperatorType() {
        return this.operatorType;
    }

    /**
     * 演算子のトークンを返す
     * 
     * @return 演算子のトークン
     */
    public String getToken() {
        return this.token;
    }
    
    public static OPERATOR getOperator(final String token) {
        for(final OPERATOR operator : OPERATOR.values()) {
            if(operator.getToken().equals(token)) {
                return operator;
            }
        }
        return null;
    }

    /**
     * 演算子のタイプを表す変数
     */
    final private OPERATOR_TYPE operatorType;

    /**
     * 演算子のトークンを表す変数
     */
    final private String token;
}
