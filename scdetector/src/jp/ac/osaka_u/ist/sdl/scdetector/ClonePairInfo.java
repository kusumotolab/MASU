package jp.ac.osaka_u.ist.sdl.scdetector;


import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;


public class ClonePairInfo implements Serializable, Entity {

    public ClonePairInfo(final ExecutableElementInfo statementA,
            final ExecutableElementInfo statementB) {

        this.cloneA = new TreeSet<ExecutableElementInfo>();
        this.cloneB = new TreeSet<ExecutableElementInfo>();

        this.cloneA.add(statementA);
        this.cloneB.add(statementB);
        this.id = number++;
    }

    public void add(final ExecutableElementInfo statementA, final ExecutableElementInfo statementB) {
        this.cloneA.add(statementA);
        this.cloneB.add(statementB);
    }

    public int size() {
        return (this.cloneA.size() + this.cloneB.size()) / 2;
    }

    public SortedSet<ExecutableElementInfo> getCloneA() {
        return this.cloneA;
    }

    public SortedSet<ExecutableElementInfo> getCloneB() {
        return this.cloneB;
    }

    public boolean includedBy(final ClonePairInfo counterClonePair) {

        final SortedSet<ExecutableElementInfo> cloneA = this.getCloneA();
        final SortedSet<ExecutableElementInfo> cloneB = this.getCloneB();

        final SortedSet<ExecutableElementInfo> counterCloneA = counterClonePair.getCloneA();
        final SortedSet<ExecutableElementInfo> counterCloneB = counterClonePair.getCloneB();

        return counterCloneA.containsAll(cloneA) && counterCloneB.containsAll(cloneB);
    }

    public int getID() {
        return this.id;
    }

    final private SortedSet<ExecutableElementInfo> cloneA;

    final private SortedSet<ExecutableElementInfo> cloneB;

    final private int id;

    private static int number = 0;

    public static String CLONEPAIR = new String("CLONEPAIR");
}
