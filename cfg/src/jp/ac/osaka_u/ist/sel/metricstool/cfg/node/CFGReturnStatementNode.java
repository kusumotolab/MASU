package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;

/**
 * return����\���m�[�h
 * 
 * @author higo
 * 
 */
public class CFGReturnStatementNode extends CFGStatementNode {

	/**
	 * ��������m�[�h�ɑΉ�����return����^���ď�����
	 * 
	 * @param returnStatement
	 */
	CFGReturnStatementNode(final ReturnStatementInfo returnStatement) {
		super(returnStatement);
	}
}