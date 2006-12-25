package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;

public class OperatorToken extends AstTokenAdapter{
    
    /**
     * キャストは 型定義 と 変数 の2つの項目を扱う2項演算子として扱う
     */
    public static final OperatorToken CAST = new OperatorToken("CAST",2,false,false,null);
    public static final OperatorToken INCL_AND_DECL = new OperatorToken("INCLEMENT",1,true,true,null);
    public static final OperatorToken ASSIGN = new OperatorToken("ASSIGN",2,true,false,null);
    public static final OperatorToken TWO_TERM = new OperatorToken("TWO_TERM",2,false,true,null);
    public static final OperatorToken SINGLE_TERM = new OperatorToken("SINGLE_TERM",1,false,true,null);
    public static final OperatorToken THREE_TERM = new OperatorToken("THREE_TERM",3,true,false,null);
    public static final OperatorToken COMPARE = new OperatorToken("COMPARE",2,false,true,PrimitiveTypeInfo.BOOLEAN);
    public static final OperatorToken NOT = new OperatorToken("NOT",1,false,true,PrimitiveTypeInfo.BOOLEAN);
    public static final OperatorToken ARRAY = new OperatorToken("ARRAY",2,false,true,null);
    
    public OperatorToken(String text, int termCount, boolean leftIsAssignmentee, boolean leftIsReferencee, UnresolvedTypeInfo specifiedResultType) {
        super(text);
        this.leftIsAssignmentee = leftIsAssignmentee;
        this.leftIsReferencee = leftIsReferencee;
        this.termCount = termCount;
        this.specifiedResultType = specifiedResultType;
    }
    
    public int getTermCount(){
        return termCount;
    }
    
    public boolean isAssignmentOperator(){
        return this.leftIsAssignmentee;
    }
    
    public boolean isOperator(){
        return true;
    }
    
    public boolean isLeftTermIsReferencee(){
        return leftIsReferencee;
    }
    
    public UnresolvedTypeInfo getSpecifiedResultType(){
        return this.specifiedResultType;
    }
    
    private final boolean leftIsAssignmentee;
    private final boolean leftIsReferencee;
    private final int termCount;
    
    private final UnresolvedTypeInfo specifiedResultType;
    
}
