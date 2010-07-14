package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;

final class CFGEmptyNode extends CFGNormalNode<ExecutableElementInfo> {

	CFGEmptyNode(final ExecutableElementInfo statement) {
		super(statement);
	}
}
