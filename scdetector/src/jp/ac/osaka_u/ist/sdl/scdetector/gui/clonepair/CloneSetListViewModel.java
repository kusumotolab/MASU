package jp.ac.osaka_u.ist.sdl.scdetector.gui.clonepair;


import java.util.Set;

import javax.swing.table.AbstractTableModel;

import jp.ac.osaka_u.ist.sdl.scdetector.CloneSetInfo;


public class CloneSetListViewModel extends AbstractTableModel {

    public CloneSetListViewModel(final Set<CloneSetInfo> cloneSets) {
        this.cloneSets = cloneSets.toArray(new CloneSetInfo[] {});
    }

    public int getRowCount() {
        return this.cloneSets.length;
    }

    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int row, int col) {

        switch (col) {
        case COL_ID:
            return this.cloneSets[row].getID();
        case COL_SIZE:
            return this.cloneSets[row].size();
        default:
            assert false : "Here shouldn't be reached!";
            return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int row) {
        return Integer.class;
    }

    @Override
    public String getColumnName(int col) {
        return TITLES[col];
    }

    public CloneSetInfo getClonePair(final int row) {
        return this.cloneSets[row];
    }

    public CloneSetInfo[] getCloneSets() {
        return this.cloneSets;
    }

    static final int COL_ID = 0;

    static final int COL_SIZE = 1;

    static final String[] TITLES = new String[] { "ID", "Size" };

    final private CloneSetInfo[] cloneSets;

}
