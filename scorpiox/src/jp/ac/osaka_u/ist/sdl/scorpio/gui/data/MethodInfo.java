package jp.ac.osaka_u.ist.sdl.scorpio.gui.data;

public class MethodInfo implements Comparable<MethodInfo> {

	public MethodInfo(final String name, final int id, final int fromLine,
			final int toLine) {
		this.name = name;
		this.id = id;
		this.fromLine = fromLine;
		this.toLine = toLine;
	}

	public MethodInfo() {
	}

	/**
	 * メソッド名を返す
	 * 
	 * @return メソッド名
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * メソッド名を設定する
	 * 
	 * @param name
	 *            設定するメソッド名
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * メソッドのIDを返す
	 * 
	 * @return　メソッドのID
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * メソッドのIDを設定する
	 * 
	 * @param id
	 *            設定するID
	 */
	public void setID(final int id) {
		this.id = id;
	}

	/**
	 * メソッドの開始行を返す
	 * 
	 * @return　メソッドの開始行
	 */
	public int getFromLine() {
		return this.fromLine;
	}

	/**
	 * メソッドの開始行を設定する
	 * 
	 * @param fromLine
	 *            設定する行数
	 */
	public void setFromLine(final int fromLine) {
		this.fromLine = fromLine;
	}

	/**
	 * メソッドの開始行を返す
	 * 
	 * @return　メソッドの開始行
	 */
	public int getToLine() {
		return this.toLine;
	}

	/**
	 * メソッドの開始行を設定する
	 * 
	 * @param fromLine
	 *            設定する行数
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

	private String name;
	private int id;
	private int fromLine;
	private int toLine;
}
