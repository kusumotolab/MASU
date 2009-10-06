package jp.ac.osaka_u.ist.sdl.scdetector.data;

import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

public class NodePairInfo {

	private final PDGNode<?> nodeA;
	private final PDGNode<?> nodeB;

	public NodePairInfo(final PDGNode<?> nodeA, final PDGNode<?> nodeB) {
		this.nodeA = nodeA;
		this.nodeB = nodeB;
	}

	public PDGNode<?> getNodeA() {
		return this.nodeA;
	}

	public PDGNode<?> getNodeB() {
		return this.nodeB;
	}
}
