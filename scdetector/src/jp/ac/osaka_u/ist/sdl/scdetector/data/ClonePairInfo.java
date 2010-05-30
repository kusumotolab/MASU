package jp.ac.osaka_u.ist.sdl.scdetector.data;

import java.util.Collection;
import java.util.Iterator;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;

/**
 * クローンペアを表すクラス
 * 
 * @author higo
 * 
 */
public class ClonePairInfo implements Cloneable, Comparable<ClonePairInfo> {

	/**
	 * コンストラクタ
	 */
	public ClonePairInfo() {

		this.codecloneA = new CodeCloneInfo();
		this.codecloneB = new CodeCloneInfo();

		this.id = number++;
	}

	/**
	 * コンストラクタ
	 * 
	 * @param elementA
	 *            初期要素A
	 * @param elementB
	 *            初期要素B
	 */
	public ClonePairInfo(final ExecutableElementInfo elementA,
			final ExecutableElementInfo elementB) {

		this();

		this.add(elementA, elementB);
	}

	/**
	 * クローンペアに要素を追加する
	 * 
	 * @param elementA
	 *            追加要素A
	 * @param elementB
	 *            追加要素B
	 */
	public void add(final ExecutableElementInfo elementA,
			final ExecutableElementInfo elementB) {
		this.codecloneA.add(elementA);
		this.codecloneB.add(elementB);
	}

	/**
	 * クローンペアに要素の集合を追加する
	 * 
	 * @param elementsA
	 *            要素群A
	 * @param elementsB
	 *            要素群B
	 */
	public void addAll(final Collection<ExecutableElementInfo> elementsA,
			final Collection<ExecutableElementInfo> elementsB) {
		this.codecloneA.addAll(elementsA);
		this.codecloneB.addAll(elementsB);
	}

	/**
	 *　クローンペアの長さを返す
	 * 
	 * @return　クローンペアの長さ
	 */
	public int length() {
		return (this.codecloneA.length() + this.codecloneB.length()) / 2;
	}

	/**
	 * 引数で与えられたクローンペアに，このクローンペアが包含されているか判定する
	 * 
	 * @param clonePair
	 *            対象クローンペア
	 * @return 包含される場合はtrue, そうでない場合はfalse
	 */
	public boolean subsumedBy(final ClonePairInfo clonePair) {

		return (this.codecloneA.subsumedBy(clonePair.codecloneA) && this.codecloneB
				.subsumedBy(clonePair.codecloneB))
				|| (this.codecloneB.subsumedBy(clonePair.codecloneA) && this.codecloneA
						.subsumedBy(clonePair.codecloneB));
	}

	/**
	 * クローンペアのIDを返す
	 * 
	 * @return クローンペアのID
	 */
	public int getID() {
		return this.id;
	}

	@Override
	public int hashCode() {
		return this.codecloneA.hashCode() + this.codecloneB.hashCode();
	}

	@Override
	public boolean equals(Object o) {

		if (null == o) {
			return false;
		}

		if (!(o instanceof ClonePairInfo)) {
			return false;
		}

		final ClonePairInfo target = (ClonePairInfo) o;
		return (this.codecloneA.equals(target.codecloneA) && this.codecloneB
				.equals(target.codecloneB))
				|| (this.codecloneA.equals(target.codecloneB) && this.codecloneB
						.equals(target.codecloneA));
	}

	@Override
	public ClonePairInfo clone() {

		final ClonePairInfo clonePair = new ClonePairInfo();
		final CodeCloneInfo cloneA = this.codecloneA;
		final CodeCloneInfo cloneB = this.codecloneB;
		clonePair.addAll(cloneA.getElements(), cloneB.getElements());

		return clonePair;
	}

	@Override
	public int compareTo(final ClonePairInfo clonePair) {

		if (null == clonePair) {
			throw new IllegalArgumentException();
		}

		final CodeCloneInfo thisCodeA = this.codecloneA;
		final CodeCloneInfo thisCodeB = this.codecloneB;
		final CodeCloneInfo targetCodeA = clonePair.codecloneA;
		final CodeCloneInfo targetCodeB = clonePair.codecloneB;

		/*
		 * // コードクローンを構成する要素数で比較 if (thisCodeA.length() > targetCodeA.length())
		 * { return 1; } else if (thisCodeA.length() < targetCodeA.length()) {
		 * return -1; } else if (thisCodeB.length() > targetCodeB.length()) {
		 * return 1; } else if (thisCodeB.length() < targetCodeB.length()) {
		 * return -1; }
		 */

		// コードクローンAの位置情報で比較
		{
			Iterator<ExecutableElementInfo> thisIterator = thisCodeA
					.getElements().iterator();
			Iterator<ExecutableElementInfo> targetIterator = targetCodeA
					.getElements().iterator();
			while (thisIterator.hasNext() && targetIterator.hasNext()) {
				int order = thisIterator.next()
						.compareTo(targetIterator.next());
				if (0 != order) {
					return order;
				}
			}
		}

		// コードクローンBの位置情報で比較
		{
			Iterator<ExecutableElementInfo> thisIterator = thisCodeB
					.getElements().iterator();
			Iterator<ExecutableElementInfo> targetIterator = targetCodeB
					.getElements().iterator();
			while (thisIterator.hasNext() && targetIterator.hasNext()) {
				int order = thisIterator.next()
						.compareTo(targetIterator.next());
				if (0 != order) {
					return order;
				}
			}
		}

		return 0;
	}

	final public CodeCloneInfo codecloneA;

	final public CodeCloneInfo codecloneB;

	final private int id;

	private static int number = 0;
}
