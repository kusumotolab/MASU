package jp.ac.osaka_u.ist.sdl.scdetector.gui.data;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sdl.scdetector.Entity;


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
     * @param codeclone �ǉ������R�[�h�N���[��
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
        return this.getFirstCodeclone().compareTo(o.getFirstCodeclone());
    }

    public static String CLONESET = new String("CLONESET");

    private final SortedSet<CodeCloneInfo> codeclones;
}
