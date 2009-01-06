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
    PLUS(ARITHMETIC, "+", null),

    /**
     * 算術演算子"-"
     */
    MINUS(ARITHMETIC, "-", null),

    /**
     * 算術演算子"*"
     */
    STAR(ARITHMETIC, "*", null),

    /**
     * 算術演算子"/"
     */
    DIV(ARITHMETIC, "/", null),

    /**
     * 算術演算子"%"
     */
    MOD(ARITHMETIC, "%", null),

    /**
     * 比較演算子"=="
     */
    EQUAL(COMPARATIVE, "==", PrimitiveTypeInfo.BOOLEAN),

    /**
     * 比較演算子"!="
     */
    NOT_EQUAL(COMPARATIVE, "!=", PrimitiveTypeInfo.BOOLEAN),

    /**
     * 比較演算子"<"
     */
    LT(COMPARATIVE, "<", PrimitiveTypeInfo.BOOLEAN),

    /**
     * 比較演算子">"
     */
    GT(COMPARATIVE, ">", PrimitiveTypeInfo.BOOLEAN),

    /**
     * 比較演算子"<="
     */
    LE(COMPARATIVE, "<=", PrimitiveTypeInfo.BOOLEAN),

    /**
     * 比較演算子">="
     */
    GE(COMPARATIVE, ">=", PrimitiveTypeInfo.BOOLEAN),
    
    /**
     * instanceof演算子
     */
    INSTANCEOF(COMPARATIVE, "instanceof", PrimitiveTypeInfo.BOOLEAN),

    /**
     * 論理演算子"&&"
     */
    LAND(LOGICAL, "&&", PrimitiveTypeInfo.BOOLEAN),

    /**
     * 論理演算子"||"
     */
    LOR(LOGICAL, "||", PrimitiveTypeInfo.BOOLEAN),

    /**
     * 論理演算子"!"
     */
    LNOT(LOGICAL, "!", PrimitiveTypeInfo.BOOLEAN),

    /**
     * ビット演算子"&"
     */
    BAND(BITS, "&", null),

    /**
     * ビット演算子"|"
     */
    BOR(BITS, "|", null),

    /**
     * ビット演算子"^"
     */
    BXOR(BITS, "^", null),

    /**
     * ビット演算子"~"
     */
    BNOT(BITS, "~", null),

    /**
     * シフト演算子"<<"
     */
    SL(SHIFT, "<<", null),

    /**
     * シフト演算子">>"
     */
    SR(SHIFT, ">>", null),

    /**
     * シフト演算子">>>"
     */
    BSR(SHIFT, ">>>", null),

    /**
     * 代入演算子"="
     */
    ASSIGN(ASSIGNMENT, "=", null),

    /**
     * 代入演算子"+="
     */
    PLUS_ASSIGN(ASSIGNMENT, "+=", null),

    /**
     * 代入演算子"-="
     */
    MINUS_ASSIGN(ASSIGNMENT, "-=", null),

    /**
     * 代入演算子"*="
     */
    STAR_ASSIGN(ASSIGNMENT, "*=", null),

    /**
     * 代入演算子"/="
     */
    DIV_ASSIGN(ASSIGNMENT, "/=", null),

    /**
     * 代入演算子"%="
     */
    MOD_ASSIGN(ASSIGNMENT, "%=", null),

    /**
     * 代入演算子"&="
     */
    BAND_ASSIGN(ASSIGNMENT, "&=", null),

    /**
     * 代入演算子"|="
     */
    BOR_ASSIGN(ASSIGNMENT, "|=", null),

    /**
     * 代入演算子"^="
     */
    BXOR_ASSIGN(ASSIGNMENT, "^=", null),

    /**
     * 代入演算子"<<="
     */
    SL_ASSIGN(ASSIGNMENT, "<<=", null),

    /**
     * 代入演算子">>="
     */
    SR_ASSIGN(ASSIGNMENT, ">>=", null),

    /**
     * 代入演算子">>>="
     */
    BSR_ASSIGN(ASSIGNMENT, ">>>=", null),
    
    /**
     * 算術一項演算子"++"
     */
    INC(ARITHMETIC, "++", PrimitiveTypeInfo.INT),
    
    /**
     * 算術一項演算子"--"
     */
    DEC(ARITHMETIC, "--", PrimitiveTypeInfo.INT),
    ;

    /**
     * 演算子のタイプとトークンを与えて初期化
     * 
     * @param operatorType 演算子のタイプ
     * @param token 演算子のトークン
     */
    private OPERATOR(final OPERATOR_TYPE operatorType, final String token, final PrimitiveTypeInfo specifiedResultType) {
        this.operatorType = operatorType;
        this.token = token;
        this.specifiedResultType = specifiedResultType;
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
     * 演算結果の型が決まっている場合はその型を返す.
     * 決まっていない場合はnullを返す.
     * @return 演算結果の型が決まっている場合はその型，決まっていない場合はnull
     */
    public PrimitiveTypeInfo getSpecifiedResultType() {
        return this.specifiedResultType;
    }
    
    final private PrimitiveTypeInfo specifiedResultType;

    /**
     * 演算子のタイプを表す変数
     */
    final private OPERATOR_TYPE operatorType;

    /**
     * 演算子のトークンを表す変数
     */
    final private String token;
}
