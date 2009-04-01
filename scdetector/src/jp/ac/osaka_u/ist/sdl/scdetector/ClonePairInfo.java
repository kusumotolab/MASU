package jp.ac.osaka_u.ist.sdl.scdetector;


import java.io.Serializable;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;


public class ClonePairInfo implements Serializable, Entity, Cloneable {

    public ClonePairInfo() {

        this.cloneA = new CodeFragmentInfo();
        this.cloneB = new CodeFragmentInfo();

        this.id = number++;
    }

    public ClonePairInfo(final ExecutableElementInfo elementA, final ExecutableElementInfo elementB) {

        this();

        this.add(elementA, elementB);
    }

    public void add(final ExecutableElementInfo elementA, final ExecutableElementInfo elementB) {
        this.cloneA.add(elementA);
        this.cloneB.add(elementB);
    }

    public void addAll(final Set<ExecutableElementInfo> elementsA,
            final Set<ExecutableElementInfo> elementsB) {
        this.cloneA.addAll(elementsA);
        this.cloneB.addAll(elementsB);
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

        final CodeFragmentInfo cloneA = this.getCloneA();
        final CodeFragmentInfo cloneB = this.getCloneB();

        final CodeFragmentInfo counterCloneA = counterClonePair.getCloneA();
        final CodeFragmentInfo counterCloneB = counterClonePair.getCloneB();

        return counterCloneA.containsAll(cloneA) && counterCloneB.containsAll(cloneB);
    }

    public int getID() {
        return this.id;
    }

    @Override
    public ClonePairInfo clone() {

        final ClonePairInfo clonePair = new ClonePairInfo();
        final CodeFragmentInfo cloneA = this.getCloneA();
        final CodeFragmentInfo cloneB = this.getCloneB();
        clonePair.addAll(cloneA, cloneB);

        return clonePair;
    }

    final private CodeFragmentInfo cloneA;

    final private CodeFragmentInfo cloneB;

    final private int id;

    private static int number = 0;

    public static String CLONEPAIR = new String("CLONEPAIR");
}
