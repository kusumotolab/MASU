package jp.ac.osaka_u.ist.sdl.scdetector;


import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Position;


public class CodeFragmentInfo extends TreeSet<Position> {

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

        for (final Position element : this) {
            hash += element.getFromLine();
            hash += element.getFromColumn();
            hash += element.getToLine();
            hash += element.getToColumn();
        }

        return hash;
    }
}
