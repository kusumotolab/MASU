package jp.ac.osaka_u.ist.sel.metricstool.pdg.edge;

import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

public class PDGAcrossExecutionDependenceEdge extends
		PDGExecutionDependenceEdge implements PDGAcrossEdge {

	public PDGAcrossExecutionDependenceEdge(final PDGNode<?> fromNode,
			final PDGNode<?> toNode) {
		super(fromNode, toNode);
	}
}
