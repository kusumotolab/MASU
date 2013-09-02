package jp.ac.osaka_u.ist.sel.metricstool.pdg.edge;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

public class PDGAcrossExecutionDependenceEdge extends
		PDGExecutionDependenceEdge implements PDGAcrossEdge {

	public PDGAcrossExecutionDependenceEdge(final PDGNode<?> fromNode,
			final PDGNode<?> toNode, final CallInfo<?> call) {
		super(fromNode, toNode);
		this.call = call;
	}

	/**
	 * ���̃��\�b�h�Ԉˑ��֌W�̌��ɂȂ������\�b�h�Ăяo����Ԃ�
	 * 
	 * @return
	 */
	@Override
	public CallInfo<?> getHolder() {
		return this.call;
	}

	final private CallInfo<?> call;
}
