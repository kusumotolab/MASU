package jp.ac.osaka_u.ist.sdl.scorpio.gui.data;

import java.util.Collections;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sdl.scorpio.Entity;

/**
 * GUI�ŃN���[���Z�b�g��\���N���X
 * 
 * @author higo
 * 
 */
public class CloneSetInfo implements Entity, Comparable<CloneSetInfo> {

	/**
	 * �R���X�g���N�^
	 */
	public CloneSetInfo() {
		this.codeclones = new TreeSet<CodeCloneInfo>();
	}

	/**
	 * �R�[�h�N���[����ǉ�����
	 * 
	 * @param codeclone
	 *            �ǉ������R�[�h�N���[��
	 */
	public void add(final CodeCloneInfo codeclone) {

		if (null == codeclone) {
			throw new IllegalArgumentException();
		}

		this.codeclones.add(codeclone);
	}

	/**
	 * ���̃N���[���Z�b�g�Ɋ܂܂��R�[�h�N���[����SortedSet��Ԃ�
	 * 
	 * @return�@���̃N���[���Z�b�g�Ɋ܂܂��R�[�h�N���[����SortedSet
	 */
	public SortedSet<CodeCloneInfo> getCodeclones() {
		return Collections.unmodifiableSortedSet(this.codeclones);
	}

	/**
	 * �N���[���Z�b�g�Ɋ܂܂��R�[�h�N���[���̑傫���i�̕��ϒl�j��Ԃ�
	 * 
	 * @return�@�N���[���Z�b�g�Ɋ܂܂��R�[�h�N���[���̑傫���i�̕��ϒl�j
	 */
	public int getLength() {

		int totalLength = 0;
		for (final CodeCloneInfo codeclone : this.getCodeclones()) {
			totalLength += codeclone.getLength();
		}

		return totalLength / this.getNumberOfElements();
	}

	/**
	 * �N���[���Z�b�g�̗v�f���i�R�[�h�N���[�����j��Ԃ�
	 * 
	 * @return�@�N���[���Z�b�g�̗v�f���i�R�[�h�N���[�����j
	 */
	public int getNumberOfElements() {
		return this.codeclones.size();
	}

	/**
	 * �N���[���Z�b�g���̃M���b�v�̐���Ԃ�
	 * 
	 * @return �N���[���Z�b�g���̃M���b�v�̐�
	 */
	public int getNumberOfGapps() {

		int gap = 0;

		for (final CodeCloneInfo codeclone : this.getCodeclones()) {
			gap += codeclone.getNumberOfGapps();
		}

		return gap;
	}

	/**
	 * �擪�̃R�[�h�N���[����Ԃ�
	 * 
	 * @return�@�擪�̃R�[�h�N���[��
	 */
	public CodeCloneInfo getFirstCodeclone() {
		return this.codeclones.first();
	}

	/**
	 * ��r�֐�
	 */
	@Override
	public int compareTo(CloneSetInfo o) {
		final Iterator<CodeCloneInfo> thisIterator = this.codeclones.iterator();
		final Iterator<CodeCloneInfo> targetIterator = o.codeclones.iterator();

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

	public static String CLONESET = new String("CLONESET");

	private final SortedSet<CodeCloneInfo> codeclones;
}
