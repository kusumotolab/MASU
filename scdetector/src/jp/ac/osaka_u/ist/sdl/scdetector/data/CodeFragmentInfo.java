package jp.ac.osaka_u.ist.sdl.scdetector.data;


import java.util.TreeSet;

import jp.ac.osaka_u.ist.sdl.scdetector.Entity;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;


public class CodeFragmentInfo extends TreeSet<ExecutableElementInfo> implements Entity,
        Comparable<CodeFragmentInfo> {

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof CodeFragmentInfo)) {
            return false;
        }

        return this.containsAll((CodeFragmentInfo) o) && ((CodeFragmentInfo) o).containsAll(this);
    }

    @Override
    public int hashCode() {

        int hash = 0;

        for (final ExecutableElementInfo element : this) {
            hash += element.getFromLine();
            hash += element.getFromColumn();
            hash += element.getToLine();
            hash += element.getToColumn();
        }

        return hash;
    }

    @Override
    public int compareTo(CodeFragmentInfo o) {

        final ExecutableElementInfo firstElement1 = this.first();
        final ExecutableElementInfo firstElement2 = o.first();
        return firstElement1.compareTo(firstElement2);
    }

    public static String CODEFRAGMENT = new String("CODEFRAGMENT");
}
