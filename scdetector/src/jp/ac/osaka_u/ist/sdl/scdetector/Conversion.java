package jp.ac.osaka_u.ist.sdl.scdetector;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.AssertStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ThrowStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;


public class Conversion {

    public static String getNormalizedString(final SingleStatementInfo statement) {

        final StringBuilder sb = new StringBuilder();

        if (statement instanceof AssertStatementInfo) {
            
            sb.append("assert ");
            
            final ExpressionInfo expression = ((AssertStatementInfo)statement).getAssertedExpression();
            final String expressionString = Conversion.getNormalizedString(expression);
            sb.append(expressionString);
            
        } else if (statement instanceof ExpressionStatementInfo) {

            final ExpressionInfo expression = ((ExpressionStatementInfo)statement).getExpression();
            final String expressionString = Conversion.getNormalizedString(expression);
            sb.append(expressionString);
            
        } else if (statement instanceof ReturnStatementInfo) {

            sb.append("return ");
            
            final ExpressionInfo expression = ((ReturnStatementInfo)statement).getReturnedExpression();
            final String expressionString = Conversion.getNormalizedString(expression);
            sb.append(expressionString);
            
        } else if (statement instanceof ThrowStatementInfo) {

            sb.append("throw ");
            
            final ExpressionInfo expression = ((ThrowStatementInfo)statement).getThrownExpression();
            final String expressionString = Conversion.getNormalizedString(expression);
            sb.append(expressionString);
            
        } else if (statement instanceof VariableDeclarationStatementInfo) {

            ((VariableDeclarationStatementInfo)statement).getDeclaredLocalVariable();
            
        }

        return sb.toString();
    }
    
    public static String getNormalizedString(final ExpressionInfo expression){
        
        final StringBuilder sb = new StringBuilder();
        
        if (expression instanceof ArrayElementUsageInfo){
            
        }else if(expression instanceof ArrayTypeReferenceInfo){
            
        }else if(expression instanceof BinominalOperationInfo){
            
        }else if(expression instanceof CallInfo){
            
        }else if(expression instanceof CastUsageInfo){
            
        }else if(expression instanceof ClassReferenceInfo){
            
        }else if(expression instanceof LiteralUsageInfo){
            
        }else if(expression instanceof MonominalOperationInfo){
            
        }else if(expression instanceof NullUsageInfo){
            
        }else if(expression instanceof TernaryOperationInfo){
            
        }else if (expression instanceof TypeParameterUsageInfo){
            
        }else if(expression instanceof UnknownEntityUsageInfo){
            
        }else if(expression instanceof VariableUsageInfo){
            
        }
        
        return sb.toString();
    }
}
