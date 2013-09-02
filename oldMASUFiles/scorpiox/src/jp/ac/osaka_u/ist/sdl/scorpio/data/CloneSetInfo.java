package jp.ac.osaka_u.ist.sdl.scorpio.data;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * �N���[���Z�b�g��\���N���X
 * 
 * @author higo
 * 
 */
public class CloneSetInfo implements Comparable<CloneSetInfo> {

	/**
	 * �R���X�g���N�^
	 */
	public CloneSetInfo() {
		this.codeclones = new TreeSet<CodeCloneInfo>();
		this.id = number++;
	}

	/**
	 * �R�[�h�N���[����ǉ�����
	 * 
	 * @param codeclone
	 *            �ǉ�����R�[�h�N���[��
	 * @return �ǉ������ꍇ��true,�@���łɊ܂܂�Ă���ǉ����Ȃ������ꍇ��false
	 */
	public boolean add(final CodeCloneInfo codeclone) {
		return this.codeclones.add(codeclone);
	}

	/**
	 * �R�[�h�N���[���Q��ǉ�����
	 * 
	 * @param codeclones
	 *            �ǉ�����R�[�h�N���[���Q
	 */
	public void addAll(final Collection<CodeCloneInfo> codeclones) {

		for (final CodeCloneInfo codeFragment : codeclones) {
			this.add(codeFragment);
		}
	}

	/**
	 * �N���[���Z�b�g���\������R�[�h�N���[���Q��Ԃ�
	 * 
	 * @return�@�N���[���Z�b�g���\������R�[�h�N���[���Q
	 */
	public SortedSet<CodeCloneInfo> getCodeClones() {
		return Collections.unmodifiableSortedSet(this.codeclones);
	}

	/**
	 * �N���[���Z�b�g��ID��Ԃ�
	 * 
	 * @return�@�N���[���Z�b�g��ID
	 */
	public int getID() {
		return this.id;
	}
	
	public int getHash(){
		return 0 == this.codeclones.size()?0:this.codeclones.first().hashCode();
	}

	/**
	 * �N���[���Z�b�g�Ɋ܂܂��R�[�h�N���[���̐���Ԃ�
	 * 
	 * @return�@�N���[���Z�b�g�Ɋ܂܂��R�[�h�N���[���̐�
	 */
	public int getNumberOfCodeclones() {
		return this.codeclones.size();
	}

	/**
	 * �N���[���Z�b�g�Ɋ܂܂��M���b�v�̐���Ԃ�
	 * 
	 * @return�@�N���[���Z�b�g�Ɋ܂܂��M���b�v�̐�
	 */
	public int getGapsNumber() {

		int gap = 0;

		for (final CodeCloneInfo codeFragment : this.getCodeClones()) {
			gap += codeFragment.getGapsNumber();
		}

		return gap;
	}

	/**
	 * �N���[���Z�b�g�̒����i�܂܂��R�[�h�N���[���̑傫���j��Ԃ�
	 * 
	 * @return�@�N���[���Z�b�g�̒����i�܂܂��R�[�h�N���[���̑傫���j
	 */
	public int getLength() {
		int total = 0;
		for (final CodeCloneInfo codeFragment : this.getCodeClones()) {
			total += codeFragment.length();
		}

		return total / this.getNumberOfCodeclones();
	}

	@Override
	public int compareTo(CloneSetInfo o) {

		final Iterator<CodeCloneInfo> thisIterator = this.getCodeClones()
				.iterator();
		final Iterator<CodeCloneInfo> targetIterator = o.getCodeClones()
				.iterator();

		// �����̗v�f���������
		while (thisIterator.hasNext() && targetIterator.hasNext()) {

			final int elementOrder = thisIterator.next().compareTo(
					targetIterator.next());
			if (0 != elementOrder) {
				return elementOrder;
			}
		}

		if (!thisIterator.hasNext() && !targetIterator.hasNext()) {
			return 0;
		}

		if (!thisIterator.hasNext()) {
			return -1;
		}

		if (!targetIterator.hasNext()) {
			return 1;
		}

		assert false : "Here shouldn't be reached!";
		return 0;
	}

	final private SortedSet<CodeCloneInfo> codeclones;

	final private int id;

	private static int number = 0;
}
