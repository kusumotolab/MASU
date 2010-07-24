package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


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
            return (ExpressionInfo) statement.getInitializationExpression().copy();
        } else {
            return null;
        }
    }

    @Override
    VariableDeclarationStatementInfo makeNewElement(final LocalSpaceInfo ownerSpace,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn,
            final ExpressionInfo... requiredExpression) {

        if (1 != requiredExpression.length) {
            throw new IllegalArgumentException();
        }

        final VariableDeclarationStatementInfo statement = this.getCore();
        final LocalVariableUsageInfo variableDeclaration = (LocalVariableUsageInfo) statement
                .getDeclaration().copy();

        final VariableDeclarationStatementInfo newStatement = new VariableDeclarationStatementInfo(
                ownerSpace, variableDeclaration, requiredExpression[0], fromLine, fromColumn,
                toLine, toColumn);
        return newStatement;
    }

    @Override
    VariableDeclarationStatementInfo makeNewElement(final LocalSpaceInfo ownerSpace,
            final ExpressionInfo... requiredExpression) {

        if (1 != requiredExpression.length) {
            throw new IllegalArgumentException();
        }

        final VariableDeclarationStatementInfo statement = this.getCore();
        final int fromLine = statement.getFromLine();
        final int fromColumn = statement.getFromColumn();
        final int toLine = statement.getToLine();
        final int toColumn = statement.getToColumn();

        return this.makeNewElement(ownerSpace, fromLine, fromColumn, toLine, toColumn,
                requiredExpression);
    }
}
