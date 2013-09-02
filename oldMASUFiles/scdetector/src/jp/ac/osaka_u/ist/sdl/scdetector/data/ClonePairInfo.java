package jp.ac.osaka_u.ist.sdl.scdetector.data;

import java.util.Iterator;

import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

/**
 * �N���[���y�A��\���N���X
 * 
 * @author higo
 * 
 */
public class ClonePairInfo implements Cloneable, Comparable<ClonePairInfo> {

	/**
	 * �R���X�g���N�^
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
	 *�@�N���[���y�A�̒�����Ԃ�
	 * 
	 * @return�@�N���[���y�A�̒���
	 */
	public int length() {
		return (this.codecloneA.length() + this.codecloneB.length()) / 2;
	}

	/**
	 * �����ŗ^����ꂽ�N���[���y�A�ɁC���̃N���[���y�A����܂���Ă��邩���肷��
	 * 
	 * @param clonePair
	 *            �ΏۃN���[���y�A
	 * @return ��܂����ꍇ��true, �����łȂ��ꍇ��false
	 */
	public boolean subsumedBy(final ClonePairInfo clonepair) {

		// this.codecloneA �� clonepair.codecloneA �����������Ƃ�
		if (this.codecloneA.length() < clonepair.codecloneA.length()) {

			// this.codecloneA�@�� clonepair.codecloneA�@�ɕ�܂���Ă��邩�𒲍�
			if (this.codecloneA.subsumedBy(clonepair.codecloneA)) {

				// this.codecloneB �� clonepair.codecloneB �����������Ƃ�
				if (this.codecloneB.length() < clonepair.codecloneB.length()) {

					// this.codecloneB �� clonepair.codecloneB�@�ɕ�܂���Ă��邩�𒲍�
					if (this.codecloneB.subsumedBy(clonepair.codecloneB)) {
						return true;
					}
				}

				// this.codecloneB �� clonepair.codecloneB �Ɠ����傫���̂Ƃ�
				else if (this.codecloneB.length() == clonepair.codecloneB
						.length()) {

					// this.codecloneB �� clonepair.codecloneB �Ɠ��������𒲍�
					if (this.codecloneB.equals(clonepair.codecloneB)) {
						return true;
					}
				}
			}
		}

		// this.codecloneA �� clonepair.codecloneA �Ɠ����傫���̂Ƃ�
		else if (this.codecloneA.length() == clonepair.codecloneA.length()) {

			// this.codecloneA �� clonepair.codecloneA �Ɠ��������𒲍�
			if (this.codecloneA.equals(clonepair.codecloneA)) {

				// this.codecloneB �� clonepair.codecloneB �����������Ƃ�
				if (this.codecloneB.length() < clonepair.codecloneB.length()) {

					// this.codecloneB �� clonepair.codecloneB �ɕ�܂���Ă������𒲍�
					if (this.codecloneB.subsumedBy(clonepair.codecloneB)) {
						return true;
					}
				}
			}
		}

		// this.codecloneA �� clonepair.codecloneB �����������Ƃ�
		if (this.codecloneA.length() < clonepair.codecloneB.length()) {

			// this.codecloneA�@�� clonepair.codecloneB�@�ɕ�܂���Ă��邩�𒲍�
			if (this.codecloneA.subsumedBy(clonepair.codecloneB)) {

				// this.codecloneB �� clonepair.codecloneA �����������Ƃ�
				if (this.codecloneB.length() < clonepair.codecloneA.length()) {

					// this.codecloneB �� clonepair.codecloneA�@�ɕ�܂���Ă��邩�𒲍�
					if (this.codecloneB.subsumedBy(clonepair.codecloneA)) {
						return true;
					}
				}

				// this.codecloneB �� clonepair.codecloneA �Ɠ����傫���̂Ƃ�
				else if (this.codecloneB.length() == clonepair.codecloneA
						.length()) {

					// this.codecloneB �� clonepair.codecloneA �Ɠ��������𒲍�
					if (this.codecloneB.equals(clonepair.codecloneA)) {
						return true;
					}
				}
			}
		}

		// this.codecloneA �� clonepair.codecloneB �Ɠ����傫���̂Ƃ�
		else if (this.codecloneA.length() == clonepair.codecloneB.length()) {

			// this.codecloneA �� clonepair.codecloneB �Ɠ��������𒲍�
			if (this.codecloneA.equals(clonepair.codecloneB)) {

				// this.codecloneB �� clonepair.codecloneA �����������Ƃ�
				if (this.codecloneB.length() < clonepair.codecloneA.length()) {

					// this.codecloneB �� clonepair.codecloneA �ɕ�܂���Ă������𒲍�
					if (this.codecloneB.subsumedBy(clonepair.codecloneA)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * �N���[���y�A��ID��Ԃ�
	 * 
	 * @return �N���[���y�A��ID
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
		 * // �R�[�h�N���[�����\������v�f���Ŕ�r if (thisCodeA.length() > targetCodeA.length())
		 * { return 1; } else if (thisCodeA.length() < targetCodeA.length()) {
		 * return -1; } else if (thisCodeB.length() > targetCodeB.length()) {
		 * return 1; } else if (thisCodeB.length() < targetCodeB.length()) {
		 * return -1; }
		 */

		// �R�[�h�N���[��A�̈ʒu���Ŕ�r
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

		// �R�[�h�N���[��B�̈ʒu���Ŕ�r
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
