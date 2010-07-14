package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;

/**
 * CFGの制御ノードを表すクラス
 * 
 * @author t-miyake, higo
 * 
 */
public class CFGControlNode extends CFGNode<ConditionInfo> {

	CFGControlNode(final ConditionInfo condition) {
		super(condition);
	}

	@Override
	public void optimize() {
	}
}
