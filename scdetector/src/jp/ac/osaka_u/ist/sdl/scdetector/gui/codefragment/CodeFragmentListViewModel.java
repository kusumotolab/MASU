package jp.ac.osaka_u.ist.sdl.scdetector.gui.codefragment;


import java.util.SortedSet;

import javax.swing.table.AbstractTableModel;

import jp.ac.osaka_u.ist.sdl.scdetector.data.CloneSetInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.data.CodeFragmentInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;


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
        case COL_CLASS:
            return this.getOwnerClass(this.codeFragments[row]).getClassName();
        case COL_POSITION:
            return this.getPositionText(this.codeFragments[row]);
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

    private ClassInfo getOwnerClass(final CodeFragmentInfo codeFragment) {

        final SortedSet<ExecutableElementInfo> elements = codeFragment.getElements();
        if (null == elements) {
            System.out.println("0");
        }
        final ExecutableElementInfo firstElement = elements.first();
        if (null == firstElement) {
            System.out.println("0");
        }
        final CallableUnitInfo ownerMethod = firstElement.getOwnerMethod();
        if (null == ownerMethod) {
            System.out.println("0");
        }
        final ClassInfo ownerClass = ownerMethod.getOwnerClass();
        if (null == ownerClass) {
            System.out.println("0");
        }

        return codeFragment.getElements().first().getOwnerMethod().getOwnerClass();
    }

    private String getPositionText(final CodeFragmentInfo codeFragment) {
        final StringBuilder text = new StringBuilder();
        text.append(codeFragment.getElements().first().getFromLine());
        text.append(".");
        text.append(codeFragment.getElements().first().getFromColumn());
        text.append(" - ");
        text.append(codeFragment.getElements().last().getToLine());
        text.append(".");
        text.append(codeFragment.getElements().last().getToColumn());
        return text.toString();
    }

    static final int COL_CLASS = 0;

    static final int COL_POSITION = 1;

    static final String[] TITLES = new String[] { "CLASS", "POSITION" };

    final private CodeFragmentInfo[] codeFragments;

}
