package jp.ac.osaka_u.ist.sdl.scorpio.gui.data;

import jp.ac.osaka_u.ist.sdl.scorpio.Entity;

public class MethodInfo implements Entity, Comparable<MethodInfo> {

	public MethodInfo(final String name, final int id, final int fileID,
			final int fromLine, final int toLine) {
		this.name = name;
		this.id = id;
		this.fileID = fileID;
		this.fromLine = fromLine;
		this.toLine = toLine;
		this.codeclone = null;
	}

	public MethodInfo() {
		this(null, 0, 0, 0, 0);
	}

	/**
	 * ���\�b�h����Ԃ�
	 * 
	 * @return ���\�b�h��
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * ���\�b�h����ݒ肷��
	 * 
	 * @param name
	 *            �ݒ肷�郁�\�b�h��
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * ���\�b�h��ID��Ԃ�
	 * 
	 * @return�@���\�b�h��ID
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * ���\�b�h��ID��ݒ肷��
	 * 
	 * @param id
	 *            �ݒ肷��ID
	 */
	public void setID(final int id) {
		this.id = id;
	}

	/**
	 * �t�@�C����ID��Ԃ�
	 * 
	 * @return�@�t�@�C����ID
	 */
	public int getFileID() {
		return this.fileID;
	}

	/**
	 * �t�@�C����ID��ݒ肷��
	 * 
	 * @param id
	 *            �ݒ肷��t�@�C��ID
	 */
	public void setFileID(final int fileID) {
		this.fileID = fileID;
	}

	/**
	 * ���\�b�h�̊J�n�s��Ԃ�
	 * 
	 * @return�@���\�b�h�̊J�n�s
	 */
	public int getFromLine() {
		return this.fromLine;
	}

	/**
	 * ���\�b�h�̊J�n�s��ݒ肷��
	 * 
	 * @param fromLine
	 *            �ݒ肷��s��
	 */
	public void setFromLine(final int fromLine) {
		this.fromLine = fromLine;
	}

	/**
	 * ���\�b�h�̊J�n�s��Ԃ�
	 * 
	 * @return�@���\�b�h�̊J�n�s
	 */
	public int getToLine() {
		return this.toLine;
	}

	/**
	 * ���\�b�h�̊J�n�s��ݒ肷��
	 * 
	 * @param fromLine
	 *            �ݒ肷��s��
	 */
	public void setToLine(final int toLine) {
		this.toLine = toLine;
	}

	@Override
	public int compareTo(MethodInfo o) {
		if (this.id < o.id) {
			return -1;
		} else if (this.id > o.id) {
			return 1;
		} else {
			return 0;
		}
	}

	public void setCodeClone(final CodeCloneInfo codeclone) {
		this.codeclone = codeclone;
	}

	public void removeCodeClone() {
		this.codeclone = null;
	}

	public CodeCloneInfo getCodeClone() {
		return this.codeclone;
	}

	@Override
	public String toString() {
		final StringBuilder label = new StringBuilder();
		label.append(this.name);
		label.append("()");
		// label.append("#");
		// final FileInfo file = FileController.getInstance(ScorpioGUI.ID)
		// .getFile(this.fileID);
		// label.append(file.getName());
		return label.toString();
	}

	public static String METHOD = new String("METHOD");

	private String name;
	private int id;
	private int fileID;
	private int fromLine;
	private int toLine;

	private CodeCloneInfo codeclone;
}
