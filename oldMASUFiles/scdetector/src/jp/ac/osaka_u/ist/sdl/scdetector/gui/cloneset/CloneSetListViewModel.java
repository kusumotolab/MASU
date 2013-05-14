package jp.ac.osaka_u.ist.sdl.scdetector.gui.cloneset;


import java.util.Set;

import javax.swing.table.AbstractTableModel;

import jp.ac.osaka_u.ist.sdl.scdetector.gui.data.CloneSetInfo;


/**
 * CloneSetListView�̃��f��
 * 
 * @author higo
 *
 */
class CloneSetListViewModel extends AbstractTableModel {

    public CloneSetListViewModel(final Set<CloneSetInfo> cloneSets) {
        this.cloneSets = cloneSets.toArray(new CloneSetInfo[] {});
    }

    /**
     * �s����Ԃ�
     * 
     * @return �s��
     */
    @Override
    public int getRowCount() {
        return this.cloneSets.length;
    }

    /**
     * �񐔂�Ԃ�
     * 
     * @return ��
     */
    @Override
    public int getColumnCount() {
        return TITLES.length;
    }

    /**
     * �w�肳�ꂽ�Z���̃I�u�W�F�N�g��Ԃ�
     * 
     * @param row �s
     * @param col ��
     * @return �w�肳�ꂽ�Z���̃I�u�W�F�N�g
     */
    @Override
    public Object getValueAt(int row, int col) {

        switch (col) {
        case COL_SIZE:
            return this.cloneSets[row].getNumberOfElements();
        case COL_LENGTH:
            return this.cloneSets[row].getLength();
        case COL_GAPS:
            return this.cloneSets[row].getNumberOfGapps();
        default:
            assert false : "Here shouldn't be reached!";
            return null;
        }
    }

    /**
     * �Z���̌^��Ԃ�
     */
    @Override
    public Class<?> getColumnClass(int row) {
        return Integer.class;
    }

    /**
     * �^�C�g����Ԃ�
     */
    @Override
    public String getColumnName(int col) {
        return TITLES[col];
    }

    /**
     * �w�肳�ꂽ�s�̃N���[���Z�b�g��Ԃ�
     * 
     * @param row�@�s
     * @return�@�w�肳�ꂽ�s�̃N���[���Z�b�g
     */
    public CloneSetInfo getCloneset(final int row) {
        return this.cloneSets[row];
    }

    /**
     * �N���[���Z�b�g�Q��Ԃ�
     * 
     * @return �N���[���Z�b�g�Q
     */
    public CloneSetInfo[] getClonesets() {
        return this.cloneSets;
    }

    static final int COL_SIZE = 0;

    static final int COL_LENGTH = 1;

    static final int COL_GAPS = 2;

    static final String[] TITLES = new String[] {"# of Instances", "size", "gaps" };

    final private CloneSetInfo[] cloneSets;

}
