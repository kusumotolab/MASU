package jp.ac.osaka_u.ist.sdl.scdetector.data;


import java.io.Serializable;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sdl.scdetector.Entity;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;


/**
 * コード片を表すクラス
 * 
 * @author higo
 *
 */
public class CodeFragmentInfo implements Entity, Comparable<CodeFragmentInfo>, Serializable {

    final private SortedSet<ExecutableElementInfo> elements;

    /**
     * 
     */
    public CodeFragmentInfo() {
        this.elements = new TreeSet<ExecutableElementInfo>();
    }

    public CodeFragmentInfo(final ExecutableElementInfo element) {
        this();
        this.elements.add(element);
    }

    public CodeFragmentInfo(final SortedSet<ExecutableElementInfo> elements) {
        this();
        this.elements.addAll(elements);
    }

    public void add(final ExecutableElementInfo element) {
        this.elements.add(element);
    }

    public void addAll(final SortedSet<ExecutableElementInfo> elements) {
        this.elements.addAll(elements);
    }

    public int length() {
        return this.elements.size();
    }

    public boolean includedBy(final CodeFragmentInfo codeFragment) {
        return codeFragment.getElements().containsAll(this.getElements());
    }

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof CodeFragmentInfo)) {
            return false;
        }

        final CodeFragmentInfo codeFragment = (CodeFragmentInfo) o;
        return this.getElements().containsAll(codeFragment.getElements())
                && codeFragment.getElements().containsAll(this.getElements());
    }

    /**
     * 
     * @return
     */
    public SortedSet<ExecutableElementInfo> getElements() {
        return Collections.unmodifiableSortedSet(this.elements);
    }

    @Override
    public int hashCode() {

        int hash = 0;

        for (final ExecutableElementInfo element : this.getElements()) {
            hash += element.getFromLine();
            hash += element.getFromColumn();
            hash += element.getToLine();
            hash += element.getToColumn();
        }

        return hash;
    }

    @Override
    public int compareTo(CodeFragmentInfo o) {

        final ExecutableElementInfo firstElement1 = this.getElements().first();
        final ExecutableElementInfo firstElement2 = o.getElements().first();
        return firstElement1.compareTo(firstElement2);
    }

    public static String CODEFRAGMENT = new String("CODEFRAGMENT");
}
