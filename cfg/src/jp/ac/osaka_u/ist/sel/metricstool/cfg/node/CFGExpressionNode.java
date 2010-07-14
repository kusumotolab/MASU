package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;

/**
 * 
 * @author higo
 * 
 */
public class CFGExpressionNode extends CFGNormalNode<ConditionInfo> {

	/**
	 * 生成するノードに対応する文を与えて初期化
	 * 
	 * @param statement
	 *            生成するノードに対応する文
	 */
	CFGExpressionNode(final ConditionInfo expression) {
		super(expression);
	}
}
