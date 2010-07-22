package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;

public class CFGVariableDeclarationStatementNode extends
		CFGStatementNode<VariableDeclarationStatementInfo> {

	CFGVariableDeclarationStatementNode(
			final VariableDeclarationStatementInfo statement) {
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
	VariableDeclarationStatementInfo makeNewElement(
			final LocalSpaceInfo ownerSpace, final int fromLine,
			final int fromColumn, final int toLine, final int toColumn,
			final ExpressionInfo... requiredExpression) {

		if (1 != requiredExpression.length) {
			throw new IllegalArgumentException();
		}

		final VariableDeclarationStatementInfo statement = this.getCore();
		final LocalVariableUsageInfo variableDeclaration = statement
				.getDeclaration();

		final VariableDeclarationStatementInfo newStatement = new VariableDeclarationStatementInfo(
				ownerSpace, variableDeclaration, requiredExpression[0],
				fromLine, fromColumn, toLine, toColumn);
		return newStatement;
	}

	@Override
	VariableDeclarationStatementInfo makeNewElement(
			final LocalSpaceInfo ownerSpace,
			final ExpressionInfo... requiredExpression) {

		if (1 != requiredExpression.length) {
			throw new IllegalArgumentException();
		}

		final VariableDeclarationStatementInfo statement = this.getCore();
		final int fromLine = statement.getFromLine();
		final int fromColumn = statement.getFromColumn();
		final int toLine = statement.getToLine();
		final int toColumn = statement.getToColumn();

		return this.makeNewElement(ownerSpace, fromLine, fromColumn, toLine,
				toColumn, requiredExpression);
	}

	@Override
	void replace(
			final List<CFGNode<? extends ExecutableElementInfo>> dissolvedNodeList) {

		if (null == dissolvedNodeList) {
			throw new IllegalArgumentException();
		}

		super.replace(dissolvedNodeList);

		final VariableDeclarationStatementInfo statement = this.getCore();
		final ExecutableElementInfo owner = statement
				.getOwnerExecutableElement();

		if (owner instanceof ForBlockInfo) {

			final ForBlockInfo ownerForBlock = (ForBlockInfo) owner;
			if (ownerForBlock.getInitializerExpressions().contains(statement)) {

				ownerForBlock.removeInitializerExpressions(statement);
				for (final CFGNode<? extends ExecutableElementInfo> node : dissolvedNodeList) {
					final ExecutableElementInfo core = node.getCore();
					ownerForBlock
							.addInitializerExpressions((ConditionInfo) core);
				}
			}
		}
	}
}
