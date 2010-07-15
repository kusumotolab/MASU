package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import java.util.Collections;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGUtility;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGNormalEdge;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ThrowStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;

/**
 * throw文を表すノード
 * 
 * @author higo
 * 
 */
public class CFGThrowStatementNode extends CFGStatementNode<ThrowStatementInfo> {

	/**
	 * ノードを生成するthrow文を与えて初期化
	 * 
	 * @param throwStatement
	 */
	CFGThrowStatementNode(final ThrowStatementInfo throwStatement) {
		super(throwStatement);
	}

	@Override
	public CFGNode<? extends ExecutableElementInfo> dissolve(
			final ICFGNodeFactory nodeFactory) {

		final ThrowStatementInfo statement = this.getCore();
		final ExpressionInfo expression = statement.getThrownExpression();

		// assertの判定部分が変数使用の式でない場合は分解を行う
		if (CFGUtility.isDissolved(expression)) {

			// 分解前の文から必要な情報を取得
			final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();
			final int fromLine = statement.getFromLine();
			final int fromColumn = statement.getFromColumn();
			final int toLine = statement.getToLine();
			final int toColumn = statement.getToColumn();
			final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
					: ownerSpace.getOuterCallableUnit();

			// ダミー変数の宣言を生成
			final LocalVariableInfo dummyVariable = new LocalVariableInfo(
					Collections.<ModifierInfo> emptySet(),
					getDummyVariableName(), expression.getType(), ownerSpace,
					fromLine, fromColumn - 1, toLine, toColumn - 1);
			final LocalVariableUsageInfo dummyVariableInitialization = LocalVariableUsageInfo
					.getInstance(dummyVariable, false, true, outerCallableUnit,
							fromLine, fromColumn - 1, toLine, toColumn - 1);
			final VariableDeclarationStatementInfo dummyVariableDeclaration = new VariableDeclarationStatementInfo(
					dummyVariableInitialization, expression, fromLine,
					fromColumn - 1, toLine, toColumn - 1);

			// ダミー変数を利用したassert文を生成
			final LocalVariableUsageInfo dummyVariableUsage = LocalVariableUsageInfo
					.getInstance(dummyVariable, true, false, outerCallableUnit,
							fromLine, fromColumn, toLine, toColumn);
			final ThrowStatementInfo newThrowStatement = new ThrowStatementInfo(
					ownerSpace, dummyVariableUsage, fromLine, fromColumn,
					toLine, toColumn);

			// 古いノードを削除
			nodeFactory.removeNode(statement);
			this.remove();

			// 新しい文のCFGノードを作成し，フローを生成
			final CFGNode<?> definitionNode = nodeFactory
					.makeNormalNode(dummyVariableDeclaration);
			final CFGNode<?> referenceNode = nodeFactory
					.makeNormalNode(newThrowStatement);
			final CFGEdge newEdge = new CFGNormalEdge(definitionNode,
					referenceNode);
			definitionNode.addForwardEdge(newEdge);
			referenceNode.addBackwardEdge(newEdge);
			for (final CFGEdge backwardEdge : this.getBackwardEdges()) {
				final CFGNode<?> backwardNode = backwardEdge.getFromNode();
				final CFGEdge newBackwardEdge = backwardEdge
						.replaceToNode(definitionNode);
				backwardNode.addForwardEdge(newBackwardEdge);
			}
			for (final CFGEdge forwardEdge : this.getForwardEdges()) {
				final CFGNode<?> forwardNode = forwardEdge.getToNode();
				final CFGEdge newForwardEdge = forwardEdge
						.replaceFromNode(referenceNode);
				forwardNode.addBackwardEdge(newForwardEdge);
			}

			// 抽出したExpressionに対しては再帰的にdissolveを実行
			definitionNode.dissolve(nodeFactory);

			// 抽出が行われたassert文のノードを返す
			return referenceNode;
		}

		// 分解しない場合は nullを返す
		else {
			return null;
		}
	}
}
