package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGUtility;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalClauseInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;

/**
 * CFGの制御ノードを表すクラス
 * 
 * @author t-miyake, higo
 * 
 */
public class CFGControlNode extends CFGNode<ConditionInfo> {

	CFGControlNode(final ConditionInfo condition) {
		super(condition);
	}

	@Override
	public CFG dissolve(final ICFGNodeFactory nodeFactory) {

		if (null == nodeFactory) {
			throw new IllegalArgumentException();
		}

		final ConditionInfo condition = this.getCore();
		final ExpressionInfo conditionalExpression = (ExpressionInfo) this
				.getCore();

		// conditionが分解の必要がない場合は何もせずに抜ける
		if (!CFGUtility.isDissolved(conditionalExpression)) {
			return null;
		}

		// 分解前の文から必要な情報を取得
		final ConditionalBlockInfo ownerBlock = (ConditionalBlockInfo) condition
				.getOwnerExecutableElement();
		final LocalSpaceInfo outerUnit = ownerBlock.getOwnerSpace();
		final int fromLine = conditionalExpression.getFromLine();
		final int fromColumn = conditionalExpression.getFromColumn();
		final int toLine = conditionalExpression.getToLine();
		final int toColumn = conditionalExpression.getToColumn();

		// 古いノードを削除
		nodeFactory.removeNode(condition);
		this.remove();

		final VariableDeclarationStatementInfo newStatement = this
				.makeVariableDeclarationStatement(outerUnit,
						conditionalExpression);
		final ExpressionInfo newCondition = LocalVariableUsageInfo.getInstance(
				newStatement.getDeclaredLocalVariable(), true, false,
				ownerBlock.getOuterCallableUnit(), fromLine, fromColumn,
				toLine, toColumn);
		final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
		dissolvedNodeList.add(nodeFactory.makeNormalNode(newStatement));
		dissolvedNodeList.add(nodeFactory.makeControlNode(newCondition));

		// 分解したノードをエッジでつなぐ
		this.makeEdges(dissolvedNodeList);

		// ownerSpaceとの調整
		outerUnit.addStatement(newStatement);
		final ConditionalClauseInfo newConditionalClause = new ConditionalClauseInfo(
				ownerBlock, newCondition, fromLine, fromColumn, toLine,
				toColumn);
		ownerBlock.setConditionalClause(newConditionalClause);

		// 分解したノード群からCFGを構築
		final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

		return newCFG;
	}

	@Override
	final ExpressionInfo getDissolvingTarget() {
		final ConditionInfo condition = this.getCore();
		if (condition instanceof VariableDeclarationStatementInfo) {
			final VariableDeclarationStatementInfo statement = (VariableDeclarationStatementInfo) condition;
			if (statement.isInitialized()) {
				return statement.getInitializationExpression();
			}
		} else if (condition instanceof ExpressionInfo) {
			return (ExpressionInfo) condition;
		} else {
			throw new IllegalStateException();
		}
		return null;
	}

	/**
	 * 与えられた引数の情報を用いて，ノードの核となるプログラム要素を生成する
	 */
	@Override
	ConditionInfo makeNewElement(final LocalSpaceInfo ownerSpace,
			final int fromLine, final int fromColumn, final int toLine,
			final int toColumn, final ExpressionInfo... requiredExpressions) {

		if ((null == ownerSpace) || (1 != requiredExpressions.length)) {
			throw new IllegalArgumentException();
		}

		final ConditionInfo condition = this.getCore();

		if (condition instanceof VariableDeclarationStatementInfo) {

			final VariableDeclarationStatementInfo statement = (VariableDeclarationStatementInfo) condition;
			final LocalVariableUsageInfo variableDeclaration = statement
					.getDeclaration();
			final VariableDeclarationStatementInfo newStatement = new VariableDeclarationStatementInfo(
					ownerSpace, variableDeclaration, requiredExpressions[0],
					fromLine, fromColumn, toLine, toColumn);
			return newStatement;
		}

		else if (condition instanceof ExpressionInfo) {
			return requiredExpressions[0];
		}

		else {
			throw new IllegalStateException();
		}
	}

	/**
	 * 与えられた引数の情報を用いて，ノードの核となるプログラム要素を生成する
	 */
	@Override
	ConditionInfo makeNewElement(final LocalSpaceInfo ownerSpace,
			final ExpressionInfo... requiredExpressions) {

		if ((null == ownerSpace) || (1 != requiredExpressions.length)) {
			throw new IllegalArgumentException();
		}

		final ConditionInfo condition = this.getCore();

		if (condition instanceof VariableDeclarationStatementInfo) {

			final VariableDeclarationStatementInfo statement = (VariableDeclarationStatementInfo) condition;

			final int fromLine = statement.getFromLine();
			final int fromColumn = statement.getFromLine();
			final int toLine = statement.getToLine();
			final int toColumn = statement.getToColumn();

			return this.makeNewElement(ownerSpace, fromLine, fromColumn,
					toLine, toColumn, requiredExpressions);
		}

		else if (condition instanceof ExpressionInfo) {
			return requiredExpressions[0];
		}

		else {
			throw new IllegalStateException();
		}
	}

	@Override
	void replace(
			List<CFGNode<? extends ExecutableElementInfo>> dissolvedNodeList) {
	}

}
