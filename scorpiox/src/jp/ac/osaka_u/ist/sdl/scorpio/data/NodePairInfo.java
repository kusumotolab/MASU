package jp.ac.osaka_u.ist.sdl.scorpio.data;

import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

public class NodePairInfo {

	public final PDGNode<?> nodeA;
	public final PDGNode<?> nodeB;

	public NodePairInfo(final PDGNode<?> nodeA, final PDGNode<?> nodeB) {
		this.nodeA = nodeA;
		this.nodeB = nodeB;
	}
}
