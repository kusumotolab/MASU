package jp.ac.osaka_u.ist.sdl.scorpio.gui.inter.codeclone;

import javax.swing.table.AbstractTableModel;

import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CloneSetInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.gui.data.CodeCloneInfo;

/**
 * 
 * @author higo
 * 
 */
class CodeCloneListViewModel extends AbstractTableModel {

	/**
	 * �R���X�g���N�^
	 * 
	 * @param cloneSet
	 *            �@���f�������������邽�߂̃N���[���Z�b�g
	 */
	CodeCloneListViewModel(final CloneSetInfo cloneSet) {
		this.codeclones = cloneSet.getCodeclones().toArray(
				new CodeCloneInfo[] {});
		for (int index = 0; index < this.codeclones.length; index++) {
			this.codeclones[index].setID(index);
		}
	}

	/**
	 * �s����Ԃ�
	 * 
	 * @Return �s��
	 */
	@Override
	public int getRowCount() {
		return this.codeclones.length;
	}

	/**
	 * �񐔂�Ԃ�
	 * 
	 * @Return ��
	 */
	@Override
	public int getColumnCount() {
		return TITLES.length;
	}

	/**
	 * �w�肵���ꏊ�̃I�u�W�F�N�g��Ԃ�
	 * 
	 * @param row
	 *            �s
	 * @param col
	 *            ��
	 */
	@Override
	public Object getValueAt(int row, int col) {

		switch (col) {
		case COL_ID:
			return this.codeclones[row].getID();
		case COL_ELEMENTS:
			return this.codeclones[row].getElements().size();
		case COL_GAPS:
			return this.codeclones[row].getNumberOfGapps();
		default:
			assert false : "Here shouldn't be reached!";
			return null;
		}
	}

	/**
	 * ��̌^���w��
	 */
	@Override
	public Class<?> getColumnClass(int row) {
		return Integer.class;
	}

	/**
	 * �^�C�g����Ԃ�
	 */
	@Override
	public String getColumnName(int col) {
		return TITLES[col];
	}

	/**
	 * �w�肵����̃R�[�h�N���[����Ԃ�
	 * 
	 * @param row
	 *            �w�߂�����
	 * @return�@�w�肵����̃R�[�h�N���[��
	 */
	public CodeCloneInfo getCodeClone(final int row) {
		return this.codeclones[row];
	}

	/**
	 * ���̃��f�����̃R�[�h�N���[���ꗗ��Ԃ�
	 * 
	 * @return ���̃��f�����̃R�[�h�N���[���ꗗ
	 */
	public CodeCloneInfo[] getCodeClones() {
		return this.codeclones;
	}

	private String getPositionText(final CodeCloneInfo codeFragment) {
		final StringBuilder text = new StringBuilder();
		text.append(codeFragment.getFirstElement().getFromLine());
		text.append(".");
		text.append(codeFragment.getFirstElement().getFromColumn());
		text.append(" - ");
		text.append(codeFragment.getLastElement().getToLine());
		text.append(".");
		text.append(codeFragment.getLastElement().getToColumn());
		return text.toString();
	}

	static final int COL_ID = 0;

	static final int COL_ELEMENTS = 1;

	static final int COL_GAPS = 2;

	static final String[] TITLES = new String[] { "ID", "# of Elements", "GAPS" };

	final private CodeCloneInfo[] codeclones;

}
