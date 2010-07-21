package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGUtility;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalClauseInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
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
		final int toLine = conditionalExpression.getToLine();

		// 古いノードを削除
		nodeFactory.removeNode(condition);
		this.remove();

		final VariableDeclarationStatementInfo newStatement = this
				.makeVariableDeclarationStatement(outerUnit,
						conditionalExpression);
		final ExpressionInfo newCondition = LocalVariableUsageInfo.getInstance(
				newStatement.getDeclaredLocalVariable(), true, false,
				ownerBlock.getOuterCallableUnit(), fromLine, CFGUtility
						.getRandomNaturalValue(), toLine, CFGUtility
						.getRandomNaturalValue());
		final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
		dissolvedNodeList.add(nodeFactory.makeNormalNode(newStatement));
		dissolvedNodeList.add(nodeFactory.makeControlNode(newCondition));

		// 分解したノードをエッジでつなぐ
		this.makeEdges(dissolvedNodeList);

		// ownerSpaceとの調整
		outerUnit.addStatement(newStatement);
		final ConditionalClauseInfo newConditionalClause = new ConditionalClauseInfo(
				ownerBlock, newCondition, fromLine, CFGUtility
						.getRandomNaturalValue(), toLine, CFGUtility
						.getRandomNaturalValue());
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
			final ExpressionInfo... requiredExpression) {

		if ((null == ownerSpace) || (1 != requiredExpression.length)) {
			throw new IllegalArgumentException();
		}

		final ConditionInfo condition = this.getCore();
		final int fromLine = condition.getFromLine();
		final int toLine = condition.getToLine();

		if (condition instanceof VariableDeclarationStatementInfo) {

			final VariableDeclarationStatementInfo statement = (VariableDeclarationStatementInfo) condition;
			final LocalVariableUsageInfo variableDeclaration = statement
					.getDeclaration();
			final VariableDeclarationStatementInfo newStatement = new VariableDeclarationStatementInfo(
					ownerSpace, variableDeclaration, requiredExpression[0],
					fromLine, CFGUtility.getRandomNaturalValue(), toLine,
					CFGUtility.getRandomNaturalValue());
			return newStatement;
		}

		else if (condition instanceof ExpressionInfo) {
			return requiredExpression[0];
		}

		else {
			throw new IllegalStateException();
		}
	}

	@Override
	void replace(
			List<CFGNode<? extends ExecutableElementInfo>> dissolvedNodeList) {
	}

	protected final VariableDeclarationStatementInfo makeVariableDeclarationStatement(
			final LocalSpaceInfo ownerSpace, final ExpressionInfo expression) {

		final int fromLine = expression.getFromLine();
		final int toLine = expression.getToLine();
		final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
				: ownerSpace.getOuterCallableUnit();

		final LocalVariableInfo dummyVariable = new LocalVariableInfo(
				Collections.<ModifierInfo> emptySet(), CFGNode
						.getDummyVariableName(), expression.getType(),
				ownerSpace, fromLine, CFGUtility.getRandomNaturalValue(),
				toLine, CFGUtility.getRandomNaturalValue());
		final LocalVariableUsageInfo variableUsage = LocalVariableUsageInfo
				.getInstance(dummyVariable, false, true, outerCallableUnit,
						fromLine, CFGUtility.getRandomNaturalValue(), toLine,
						CFGUtility.getRandomNaturalValue());
		final VariableDeclarationStatementInfo statement = new VariableDeclarationStatementInfo(
				ownerSpace, variableUsage, expression, fromLine, CFGUtility
						.getRandomNaturalValue(), toLine, CFGUtility
						.getRandomNaturalValue());
		return statement;
	}
}
