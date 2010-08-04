package jp.ac.osaka_u.ist.sel.metricstool.pdg.edge;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

public class PDGAcrossDataDependenceEdge extends PDGDataDependenceEdge
		implements PDGAcrossEdge {

	public PDGAcrossDataDependenceEdge(final PDGNode<?> fromNode,
			final PDGNode<?> toNode, final VariableInfo<?> data) {
		super(fromNode, toNode, data);
	}
}
