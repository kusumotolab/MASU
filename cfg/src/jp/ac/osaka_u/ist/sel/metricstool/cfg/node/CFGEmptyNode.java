package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;

final public class CFGEmptyNode extends CFGNormalNode<ExecutableElementInfo> {

	CFGEmptyNode(final ExecutableElementInfo statement) {
		super(statement);
	}

	@Override
	final ExpressionInfo getDissolvingTarget() {
		return null;
	}

	@Override
	ExecutableElementInfo makeNewElement(final LocalSpaceInfo ownerSpace,
			final int fromLine, final int fromColumn, final int toLine,
			final int toColumn, final ExpressionInfo... requiredExpressions) {
		return null;
	}

	@Override
	ExecutableElementInfo makeNewElement(final LocalSpaceInfo ownerSpace,
			final ExpressionInfo... requiredExpressions) {
		return null;
	}

	@Override
	void replace(
			List<CFGNode<? extends ExecutableElementInfo>> dissolvedNodeList) {
	}
}
