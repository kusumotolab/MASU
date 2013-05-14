package jp.ac.osaka_u.ist.sel.metricstool.pdg.edge;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

abstract class PDGAcrossDataDependenceEdge extends PDGDataDependenceEdge
		implements PDGAcrossEdge {

	PDGAcrossDataDependenceEdge(final PDGNode<?> fromNode,
			final PDGNode<?> toNode, final VariableInfo<?> data,
			final CallInfo<?> call) {
		super(fromNode, toNode, data);
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
