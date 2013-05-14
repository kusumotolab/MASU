package jp.ac.osaka_u.ist.sdl.scdetector.data;

import java.util.Collection;
import java.util.Collections;
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
		return this.codeclones.first().compareTo(o.getCodeClones().first());
	}

	final private SortedSet<CodeCloneInfo> codeclones;

	final private int id;

	private static int number = 0;
}
