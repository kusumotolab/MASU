package jp.ac.osaka_u.ist.sel.metricstool.pdg.edge;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;

/**
 * ���\�b�h���܂�����ˑ��ӂł��邱�Ƃ�\���C���^�[�t�F�[�X
 * 
 * @author higo
 * 
 */
public interface PDGAcrossEdge {

	/**
	 * ���̈ˑ��ӂ̐������ɂȂ������\�b�hor�R���X�g���N�^�Ăяo����Ԃ�
	 * 
	 * @return
	 */
	CallInfo<?> getHolder();
}
