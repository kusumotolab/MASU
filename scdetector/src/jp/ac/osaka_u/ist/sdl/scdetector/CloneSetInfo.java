package jp.ac.osaka_u.ist.sdl.scdetector;


import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class CloneSetInfo implements Entity, Serializable, Comparable<CloneSetInfo> {

    public CloneSetInfo() {
        this.codeFragments = new HashSet<CodeFragmentInfo>();
        this.id = number++;
    }

    public void add(final CodeFragmentInfo codeFragment) {
        this.codeFragments.add(codeFragment);
    }

    public void addAll(final Collection<CodeFragmentInfo> codeFragments) {
        this.codeFragments.addAll(codeFragments);
    }

    public Set<CodeFragmentInfo> getCodeFragments() {
        return Collections.unmodifiableSet(this.codeFragments);
    }

    public int getID() {
        return this.id;
    }

    public int size() {
        return this.codeFragments.size();
    }

    @Override
    public int compareTo(CloneSetInfo o) {

        if (this.getID() < o.getID()) {
            return -1;
        } else if (this.getID() > o.getID()) {
            return 1;
        } else {
            return 0;
        }
    }

    final private Set<CodeFragmentInfo> codeFragments;

    final private int id;

    private static int number = 0;

    public static String CLONESET = new String("CLONESET");
}
