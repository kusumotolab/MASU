package jp.ac.osaka_u.ist.sdl.scdetector.gui.codefragment;


import javax.swing.table.AbstractTableModel;

import jp.ac.osaka_u.ist.sdl.scdetector.CloneSetInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.CodeFragmentInfo;


public class CodeFragmentListViewModel extends AbstractTableModel {

    public CodeFragmentListViewModel(final CloneSetInfo cloneSet) {
        this.codeFragments = cloneSet.getCodeFragments().toArray(new CodeFragmentInfo[] {});
    }

    public int getRowCount() {
        return this.codeFragments.length;
    }

    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int row, int col) {

        switch (col) {
        case COL_FILE:
            return "aaa";//this.codeFragments[row].getID();
        case COL_POSITION:
            return "bbb";//this.codeFragments[row].size();
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

    public CodeFragmentInfo getCodeFragment(final int row) {
        return this.codeFragments[row];
    }

    public CodeFragmentInfo[] getCodeFragments() {
        return this.codeFragments;
    }

    static final int COL_FILE = 0;

    static final int COL_POSITION = 1;

    static final String[] TITLES = new String[] { "FILE", "POSITION" };

    final private CodeFragmentInfo[] codeFragments;

}
