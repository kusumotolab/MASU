package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachConditionInfo;

public class CFGForeachControlNode extends CFGControlNode {

	CFGForeachControlNode(final ForeachConditionInfo condition) {
		super(condition);
	}
}
