package jp.ac.osaka_u.ist.sdl.scdetector.gui.data;

/**
 * GUI�ŃR�[�h�N���[�����\������v�f��\���N���X
 * 
 * @author higo
 * 
 */
public class ElementInfo implements Comparable<ElementInfo> {

	/**
	 * �R���X�g���N�^
	 * 
	 * @param fileID
	 *            �t�@�C��ID
	 * @param fromLine
	 *            �J�n�s
	 * @param fromColumn
	 *            �J�n��
	 * @param toLine
	 *            �I���s
	 * @param toColumn
	 *            �I����
	 */
	public ElementInfo(final int fileID, final int fromLine,
			final int fromColumn, final int toLine, final int toColumn) {

		this.fileID = fileID;
		this.fromLine = fromLine;
		this.fromColumn = fromColumn;
		this.toLine = toLine;
		this.toColumn = toColumn;
	}

	public ElementInfo() {
	}

	/**
	 * �t�@�C����ID��Ԃ�
	 * 
	 * @return �t�@�C����ID
	 */
	public int getFileID() {
		return this.fileID;
	}

	public void setFileID(final int fileID) {
		this.fileID = fileID;
	}

	/**
	 * �J�n�s��Ԃ�
	 * 
	 * @return�@�J�n�s
	 */
	public int getFromLine() {
		return this.fromLine;
	}

	/**
	 * �J�n�s��ݒ肷��
	 * 
	 * @param fromLine
	 *            �@�ݒ肷��J�n�s
	 */
	public void setFromLine(final int fromLine) {
		this.fromLine = fromLine;
	}

	/**
	 * �J�n���Ԃ�
	 * 
	 * @return�@�J�n��
	 */
	public int getFromColumn() {
		return this.fromColumn;
	}

	/**
	 * �J�n���ݒ肷��
	 * 
	 * @param fromColumn
	 *            �@�ݒ肷��J�n��
	 */
	public void setFromColumn(final int fromColumn) {
		this.fromColumn = fromColumn;
	}

	/**
	 * �I���s��Ԃ�
	 * 
	 * @return�@�I���s
	 */
	public int getToLine() {
		return this.toLine;
	}

	/**
	 * �I���s��ݒ肷��
	 * 
	 * @param toLine
	 *            �@�ݒ肷��I���s
	 */
	public void setToLine(final int toLine) {
		this.toLine = toLine;
	}

	/**
	 * �I�����Ԃ�
	 * 
	 * @return�@�I����
	 */
	public int getToColumn() {
		return this.toColumn;
	}

	/**
	 * �I�����ݒ肷��
	 * 
	 * @param toColumn
	 *            �@�ݒ肷��I����
	 */
	public void setToColumn(final int toColumn) {
		this.toColumn = toColumn;
	}

	/**
	 * ��r�֐�
	 */
	@Override
	public int compareTo(ElementInfo o) {

		if (this.getFileID() < o.getFileID()) {
			return -1;
		} else if (this.getFileID() > o.getFileID()) {
			return 1;
		} else if (this.getFromLine() < o.getFromLine()) {
			return -1;
		} else if (this.getFromLine() > o.getFromLine()) {
			return 1;
		} else if (this.getFromColumn() < o.getFromColumn()) {
			return -1;
		} else if (this.getFromColumn() > o.getFromColumn()) {
			return 1;
		} else if (this.getToLine() < o.getToLine()) {
			return -1;
		} else if (this.getToLine() > o.getToLine()) {
			return 1;
		} else if (this.getToColumn() < o.getToColumn()) {
			return -1;
		} else if (this.getToColumn() > o.getToColumn()) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public int hashCode() {
		return this.fileID + this.getFromLine() + this.getFromColumn()
				+ this.getToLine() + this.getToColumn();
	}

	@Override
	public boolean equals(Object o) {

		if (null == o) {
			return false;
		}

		if (!(o instanceof ElementInfo)) {
			return false;
		}

		final ElementInfo target = (ElementInfo) o;
		return (this.fileID == target.fileID)
				&& (this.fromLine == target.fromLine)
				&& (this.fromColumn == target.fromColumn)
				&& (this.toLine == target.toLine)
				&& (this.toColumn == target.toColumn);
	}

	private int fileID;

	private int fromLine;

	private int fromColumn;

	private int toLine;

	private int toColumn;
}
