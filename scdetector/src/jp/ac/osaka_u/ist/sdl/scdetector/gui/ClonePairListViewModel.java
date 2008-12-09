package jp.ac.osaka_u.ist.sdl.scdetector.gui;


import java.util.Set;

import javax.swing.table.AbstractTableModel;

import jp.ac.osaka_u.ist.sdl.scdetector.ClonePairInfo;


public class ClonePairListViewModel extends AbstractTableModel {

    public ClonePairListViewModel(final Set<ClonePairInfo> clonePairs) {
        this.clonePairs = clonePairs.toArray(new ClonePairInfo[] {});
    }

    public int getRowCount() {
        return this.clonePairs.length;
    }

    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int row, int col) {

        switch (col) {
        case COL_ID:
            return this.clonePairs[row].getID();
        case COL_SIZE:
            return this.clonePairs[row].size();
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

    public ClonePairInfo getClonePair(final int row) {
        return this.clonePairs[row];
    }

    public ClonePairInfo[] getClonePairs() {
        return this.clonePairs;
    }

    static final int COL_ID = 0;

    static final int COL_SIZE = 1;

    static final String[] TITLES = new String[] { "ID", "Size" };

    final private ClonePairInfo[] clonePairs;

}
