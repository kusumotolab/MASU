package jp.ac.osaka_u.ist.sdl.scdetector;


import java.io.Serializable;
import java.util.Set;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;


public class ClonePairInfo implements Serializable, Entity, Cloneable {

    public ClonePairInfo() {

        this.cloneA = new CodeFragmentInfo();
        this.cloneB = new CodeFragmentInfo();

        this.id = number++;
    }

    public ClonePairInfo(final ExecutableElementInfo statementA,
            final ExecutableElementInfo statementB) {

        this();

        this.add(statementA, statementB);
    }

    public void add(final ExecutableElementInfo statementA, final ExecutableElementInfo statementB) {
        this.cloneA.add(statementA);
        this.cloneB.add(statementB);
    }

    public void addAll(final Set<ExecutableElementInfo> statementsA,
            final Set<ExecutableElementInfo> statementsB) {
        this.cloneA.addAll(statementsA);
        this.cloneB.addAll(statementsB);
    }

    public int size() {
        return (this.cloneA.size() + this.cloneB.size()) / 2;
    }

    public CodeFragmentInfo getCloneA() {
        return this.cloneA;
    }

    public CodeFragmentInfo getCloneB() {
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

    @Override
    public ClonePairInfo clone() {

        final ClonePairInfo clonePair = new ClonePairInfo();
        final Set<ExecutableElementInfo> statementsA = this.getCloneA();
        final Set<ExecutableElementInfo> statementsB = this.getCloneB();
        clonePair.addAll(statementsA, statementsB);

        return clonePair;
    }

    final private CodeFragmentInfo cloneA;

    final private CodeFragmentInfo cloneB;

    final private int id;

    private static int number = 0;

    public static String CLONEPAIR = new String("CLONEPAIR");
}
