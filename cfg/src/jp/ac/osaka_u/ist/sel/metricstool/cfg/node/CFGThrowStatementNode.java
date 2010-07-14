package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ThrowStatementInfo;

/**
 * throw文を表すノード
 * 
 * @author higo
 * 
 */
public class CFGThrowStatementNode extends CFGStatementNode {

	/**
	 * ノードを生成するthrow文を与えて初期化
	 * 
	 * @param throwStatement
	 */
	CFGThrowStatementNode(final ThrowStatementInfo throwStatement) {
		super(throwStatement);
	}
}
