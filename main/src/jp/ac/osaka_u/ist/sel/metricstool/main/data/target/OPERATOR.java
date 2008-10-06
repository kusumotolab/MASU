package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import static jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR_TYPE.*;


/**
 * Zqπ\·ρ^
 * 
 * @author t-miyake
 *
 */
public enum OPERATOR {

    /**
     * ZpZq"+"
     */
    PLUS(ARITHMETIC, "+"),

    /**
     * ZpZq"-"
     */
    MINUS(ARITHMETIC, "-"),

    /**
     * ZpZq"*"
     */
    STAR(ARITHMETIC, "*"),

    /**
     * ZpZq"/"
     */
    DIV(ARITHMETIC, "/"),

    /**
     * ZpZq"%"
     */
    MOD(ARITHMETIC, "%"),

    /**
     * δrZq"=="
     */
    EQUAL(COMPARATIVE, "=="),

    /**
     * δrZq"!="
     */
    NOT_EQUAL(COMPARATIVE, "!="),

    /**
     * δrZq"<"
     */
    LT(COMPARATIVE, "<"),

    /**
     * δrZq">"
     */
    GT(COMPARATIVE, ">"),

    /**
     * δrZq"<="
     */
    LE(COMPARATIVE, "<="),

    /**
     * δrZq">="
     */
    GE(COMPARATIVE, ">="),
    
    /**
     * instanceofZq
     */
    INSTANCEOF(COMPARATIVE, "instanceof"),

    /**
     * _Zq"&&"
     */
    LAND(LOGICAL, "&&"),

    /**
     * _Zq"||"
     */
    LOR(LOGICAL, "||"),

    /**
     * _Zq"!"
     */
    LNOT(LOGICAL, "!"),

    /**
     * rbgZq"&"
     */
    BAND(BITS, "&"),

    /**
     * rbgZq"|"
     */
    BOR(BITS, "|"),

    /**
     * rbgZq"^"
     */
    BXOR(BITS, "^"),

    /**
     * rbgZq"~"
     */
    BNOT(BITS, "~"),

    /**
     * VtgZq"<<"
     */
    SL(SHIFT, "<<"),

    /**
     * VtgZq">>"
     */
    SR(SHIFT, ">>"),

    /**
     * VtgZq">>>"
     */
    BSR(SHIFT, ">>>"),

    /**
     * γόZq"="
     */
    ASSIGN(ASSIGNMENT, "="),

    /**
     * γόZq"+="
     */
    PLUS_ASSIGN(ASSIGNMENT, "+="),

    /**
     * γόZq"-="
     */
    MINUS_ASSIGN(ASSIGNMENT, "-="),

    /**
     * γόZq"*="
     */
    STAR_ASSIGN(ASSIGNMENT, "*="),

    /**
     * γόZq"/="
     */
    DIV_ASSIGN(ASSIGNMENT, "/="),

    /**
     * γόZq"%="
     */
    MOD_ASSIGN(ASSIGNMENT, "%="),

    /**
     * γόZq"&="
     */
    BAND_ASSIGN(ASSIGNMENT, "&="),

    /**
     * γόZq"|="
     */
    BOR_ASSIGN(ASSIGNMENT, "|="),

    /**
     * γόZq"^="
     */
    BXOR_ASSIGN(ASSIGNMENT, "^="),

    /**
     * γόZq"<<="
     */
    SL_ASSIGN(ASSIGNMENT, "<<="),

    /**
     * γόZq">>="
     */
    SR_ASSIGN(ASSIGNMENT, ">>="),

    /**
     * γόZq">>>="
     */
    BSR_ASSIGN(ASSIGNMENT, ">>>="),
    
    ;

    //    INC             :   "++"    ;

    //    DEC             :   "--"    ;

    /**
     * ZqΜ^CvΖg[Nπ^¦Δϊ»
     * 
     * @param operatorType ZqΜ^Cv
     * @param token ZqΜg[N
     */
    private OPERATOR(final OPERATOR_TYPE operatorType, final String token) {
        this.operatorType = operatorType;
        this.token = token;
    }

    /**
     * ZqΜ^CvπΤ·
     * 
     * @return ZqΜ^Cv
     */
    public OPERATOR_TYPE getOperatorType() {
        return this.operatorType;
    }

    /**
     * ZqΜg[NπΤ·
     * 
     * @return ZqΜg[N
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
     * ZqΜ^Cvπ\·Ο
     */
    final private OPERATOR_TYPE operatorType;

    /**
     * ZqΜg[Nπ\·Ο
     */
    final private String token;
}
