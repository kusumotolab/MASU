package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGJumpEdge;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.JumpStatementInfo;

abstract public class CFGJumpStatementNode extends
		CFGStatementNode<JumpStatementInfo> {

	CFGJumpStatementNode(final JumpStatementInfo jumpStatement) {
		super(jumpStatement);
	}

	@Override
	public final void optimize() {

		// このノードのバックワードノード群を取得
		final Set<CFGNode<?>> backwardNodes = new HashSet<CFGNode<?>>();
		for (final CFGEdge backwardEdge : this.getBackwardEdges()) {
			backwardNodes.add(backwardEdge.getFromNode());
		}

		// このノードのフォワードノード群を取得
		final Set<CFGNode<?>> forwardNodes = new HashSet<CFGNode<?>>();
		for (final CFGEdge forwardEdge : this.getForwardEdges()) {
			forwardNodes.add(forwardEdge.getToNode());
		}

		// バックワードノードから，このノードをフォワードノードとするエッジを削除
		for (final CFGNode<?> backwardNode : this.getBackwardNodes()) {
			backwardNode.removeForwardEdges(this.getBackwardEdges());
		}

		// フォワードノードから，このノードをバックワードノードとするエッジを削除
		for (final CFGNode<?> forwardNode : this.getForwardNodes()) {
			forwardNode.removeBackwardEdges(this.getForwardEdges());
		}

		// バックワードノード群とフォワードノード群をつなぐ
		for (final CFGNode<?> backwardNode : backwardNodes) {
			for (final CFGNode<?> forwardNode : forwardNodes) {
				final CFGJumpEdge edge = new CFGJumpEdge(backwardNode,
						forwardNode);
				backwardNode.addForwardEdge(edge);
			}
		}
	}
}
