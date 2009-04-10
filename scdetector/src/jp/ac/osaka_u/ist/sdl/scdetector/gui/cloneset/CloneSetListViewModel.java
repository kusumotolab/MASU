package jp.ac.osaka_u.ist.sdl.scdetector.gui.cloneset;


import java.util.Set;

import javax.swing.table.AbstractTableModel;

import jp.ac.osaka_u.ist.sdl.scdetector.data.CloneSetInfo;


public class CloneSetListViewModel extends AbstractTableModel {

    public CloneSetListViewModel(final Set<CloneSetInfo> cloneSets) {
        this.cloneSets = cloneSets.toArray(new CloneSetInfo[] {});
    }

    public int getRowCount() {
        return this.cloneSets.length;
    }

    public int getColumnCount() {
        return TITLES.length;
    }

    @Override
    public Object getValueAt(int row, int col) {

        switch (col) {
        case COL_ID:
            return this.cloneSets[row].getID();
        case COL_SIZE:
            return this.cloneSets[row].size();
        case COL_LENGTH:
            return this.cloneSets[row].length();
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

    static final int COL_LENGTH = 2;

    static final String[] TITLES = new String[] { "ID", "# of Instances", "size" };

    final private CloneSetInfo[] cloneSets;

}
