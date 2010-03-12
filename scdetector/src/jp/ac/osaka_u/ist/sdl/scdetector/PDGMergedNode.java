package jp.ac.osaka_u.ist.sdl.scdetector;

import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNormalNode;

public class PDGMergedNode<T extends ExecutableElementInfo> extends
		PDGNormalNode<T> {

	public PDGMergedNode() {
		this.originalNodes = new LinkedList<PDGNormalNode<?>>();
	}

	@Override
	public SortedSet<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SortedSet<VariableInfo<? extends UnitInfo>> getReferencedVariables() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addNode(final PDGNormalNode<?> node) {
		this.originalNodes.add(node);
	}

	private final List<PDGNormalNode<?>> originalNodes;
}
