package jp.ac.osaka_u.ist.sdl.scdetector.data;

import java.util.Iterator;

import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

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

	public ClonePairInfo(final PDGNode<?> nodeA, final PDGNode<?> nodeB) {

		this();

		this.add(nodeA, nodeB);
	}

	public void add(final PDGNode<?> nodeA, final PDGNode<?> nodeB) {
		this.codecloneA.add(nodeA);
		this.codecloneB.add(nodeB);
	}
	
	public void add(final ClonePairInfo clonepair){
		this.codecloneA.addElements(clonepair.codecloneA);
		this.codecloneB.addElements(clonepair.codecloneB);
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
	public boolean subsumedBy(final ClonePairInfo clonepair) {

		// this.codecloneA が clonepair.codecloneA よりも小さいとき
		if (this.codecloneA.length() < clonepair.codecloneA.length()) {

			// this.codecloneA　が clonepair.codecloneA　に包含されているかを調査
			if (this.codecloneA.subsumedBy(clonepair.codecloneA)) {

				// this.codecloneB が clonepair.codecloneB よりも小さいとき
				if (this.codecloneB.length() < clonepair.codecloneB.length()) {

					// this.codecloneB が clonepair.codecloneB　に包含されているかを調査
					if (this.codecloneB.subsumedBy(clonepair.codecloneB)) {
						return true;
					}
				}

				// this.codecloneB が clonepair.codecloneB と同じ大きさのとき
				else if (this.codecloneB.length() == clonepair.codecloneB
						.length()) {

					// this.codecloneB が clonepair.codecloneB と等しいかを調査
					if (this.codecloneB.equals(clonepair.codecloneB)) {
						return true;
					}
				}
			}
		}

		// this.codecloneA が clonepair.codecloneA と同じ大きさのとき
		else if (this.codecloneA.length() == clonepair.codecloneA.length()) {

			// this.codecloneA が clonepair.codecloneA と等しいかを調査
			if (this.codecloneA.equals(clonepair.codecloneA)) {

				// this.codecloneB が clonepair.codecloneB よりも小さいとき
				if (this.codecloneB.length() < clonepair.codecloneB.length()) {

					// this.codecloneB が clonepair.codecloneB に包含されていうかを調査
					if (this.codecloneB.subsumedBy(clonepair.codecloneB)) {
						return true;
					}
				}
			}
		}

		// this.codecloneA が clonepair.codecloneB よりも小さいとき
		if (this.codecloneA.length() < clonepair.codecloneB.length()) {

			// this.codecloneA　が clonepair.codecloneB　に包含されているかを調査
			if (this.codecloneA.subsumedBy(clonepair.codecloneB)) {

				// this.codecloneB が clonepair.codecloneA よりも小さいとき
				if (this.codecloneB.length() < clonepair.codecloneA.length()) {

					// this.codecloneB が clonepair.codecloneA　に包含されているかを調査
					if (this.codecloneB.subsumedBy(clonepair.codecloneA)) {
						return true;
					}
				}

				// this.codecloneB が clonepair.codecloneA と同じ大きさのとき
				else if (this.codecloneB.length() == clonepair.codecloneA
						.length()) {

					// this.codecloneB が clonepair.codecloneA と等しいかを調査
					if (this.codecloneB.equals(clonepair.codecloneA)) {
						return true;
					}
				}
			}
		}

		// this.codecloneA が clonepair.codecloneB と同じ大きさのとき
		else if (this.codecloneA.length() == clonepair.codecloneB.length()) {

			// this.codecloneA が clonepair.codecloneB と等しいかを調査
			if (this.codecloneA.equals(clonepair.codecloneB)) {

				// this.codecloneB が clonepair.codecloneA よりも小さいとき
				if (this.codecloneB.length() < clonepair.codecloneA.length()) {

					// this.codecloneB が clonepair.codecloneA に包含されていうかを調査
					if (this.codecloneB.subsumedBy(clonepair.codecloneA)) {
						return true;
					}
				}
			}
		}

		return false;
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

		if (((this.codecloneA.hashCode() != target.codecloneA.hashCode()) || (this.codecloneB
				.hashCode() != target.codecloneB.hashCode()))
				&& ((this.codecloneA.hashCode() != target.codecloneB.hashCode()) || (this.codecloneB
						.hashCode() != target.codecloneB.hashCode()))) {
			return false;
		}

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
		clonePair.codecloneA.addElements(cloneA);
		clonePair.codecloneB.addElements(cloneB);

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
			Iterator<PDGNode<?>> thisIterator = thisCodeA.getElements()
					.iterator();
			Iterator<PDGNode<?>> targetIterator = targetCodeA.getElements()
					.iterator();
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
			Iterator<PDGNode<?>> thisIterator = thisCodeB.getElements()
					.iterator();
			Iterator<PDGNode<?>> targetIterator = targetCodeB.getElements()
					.iterator();
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
