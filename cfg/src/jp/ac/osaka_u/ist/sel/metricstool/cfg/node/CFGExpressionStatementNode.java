package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGUtility;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BinominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR;


/**
 * 
 * @author higo
 * 
 */
public class CFGExpressionStatementNode extends CFGStatementNode<ExpressionStatementInfo> {

    /**
     * ê∂ê¨Ç∑ÇÈÉmÅ[ÉhÇ…ëŒâûÇ∑ÇÈï∂Çó^Ç¶Çƒèâä˙âª
     * 
     * @param statement
     *            ê∂ê¨Ç∑ÇÈÉmÅ[ÉhÇ…ëŒâûÇ∑ÇÈï∂
     */
    CFGExpressionStatementNode(final ExpressionStatementInfo statement) {
        super(statement);
    }

    @Override
    ExpressionInfo getDissolvingTarget() {

        final ExpressionStatementInfo statement = this.getCore();
        final ExpressionInfo expression = statement.getExpression();

        if (expression instanceof BinominalOperationInfo) {

            final BinominalOperationInfo binominalOperation = (BinominalOperationInfo) expression;
            final OPERATOR operator = binominalOperation.getOperator();

            if (operator.equals(OPERATOR.ASSIGN)) {
                return binominalOperation.getSecondOperand();
            }
        }

        return statement.getExpression();
    }

    @Override
    ExpressionStatementInfo makeNewElement(final LocalSpaceInfo ownerSpace,
            final ExpressionInfo... requiredExpression) {

        if ((null == ownerSpace) || (1 != requiredExpression.length)) {
            throw new IllegalArgumentException();
        }

        final ExpressionStatementInfo statement = this.getCore();
        final ExpressionInfo expression = statement.getExpression();
        final int fromLine = statement.getFromLine();
        final int toLine = statement.getToLine();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        if (expression instanceof BinominalOperationInfo) {

            final BinominalOperationInfo binominalOperation = (BinominalOperationInfo) expression;
            final OPERATOR operator = binominalOperation.getOperator();
            final ExpressionInfo firstOperand = binominalOperation.getFirstOperand();

            if (operator.equals(OPERATOR.ASSIGN)) {
                final BinominalOperationInfo newBinominalOperation = new BinominalOperationInfo(
                        operator, firstOperand, requiredExpression[0], outerCallableUnit, fromLine,
                        CFGUtility.getRandomNaturalValue(), toLine, CFGUtility
                                .getRandomNaturalValue());
                final ExpressionStatementInfo newStatement = new ExpressionStatementInfo(
                        ownerSpace, newBinominalOperation, fromLine, CFGUtility
                                .getRandomNaturalValue(), toLine, CFGUtility
                                .getRandomNaturalValue());
                return newStatement;
            }
        }

        final ExpressionStatementInfo newStatement = new ExpressionStatementInfo(ownerSpace,
                requiredExpression[0], fromLine, CFGUtility.getRandomNaturalValue(), toLine,
                CFGUtility.getRandomNaturalValue());
        return newStatement;
    }
}
