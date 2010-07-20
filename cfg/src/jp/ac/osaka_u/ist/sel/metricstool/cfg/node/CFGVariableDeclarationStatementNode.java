package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGUtility;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;


public class CFGVariableDeclarationStatementNode extends
        CFGStatementNode<VariableDeclarationStatementInfo> {

    CFGVariableDeclarationStatementNode(final VariableDeclarationStatementInfo statement) {
        super(statement);
    }

    @Override
    ExpressionInfo getDissolvingTarget() {
        final VariableDeclarationStatementInfo statement = this.getCore();
        if (statement.isInitialized()) {
            return statement.getInitializationExpression();
        } else {
            return null;
        }
    }

    @Override
    VariableDeclarationStatementInfo makeNewElement(final LocalSpaceInfo ownerSpace,
            final ExpressionInfo... requiredExpression) {

        if (1 != requiredExpression.length) {
            throw new IllegalArgumentException();
        }

        final VariableDeclarationStatementInfo statement = this.getCore();
        final LocalVariableUsageInfo variableDeclaration = statement.getDeclaration();
        final int fromLine = statement.getFromLine();
        final int toLine = statement.getToLine();

        final VariableDeclarationStatementInfo newStatement = new VariableDeclarationStatementInfo(
                ownerSpace, variableDeclaration, requiredExpression[0], fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        return newStatement;
    }
}
