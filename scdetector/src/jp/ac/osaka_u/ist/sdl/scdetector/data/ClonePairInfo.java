package jp.ac.osaka_u.ist.sdl.scdetector.data;


import java.io.Serializable;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sdl.scdetector.Entity;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;


public class ClonePairInfo implements Serializable, Entity, Cloneable {

    public ClonePairInfo() {

        this.codeFragmentA = new CodeFragmentInfo();
        this.codeFragmentB = new CodeFragmentInfo();

        this.id = number++;
    }

    public ClonePairInfo(final ExecutableElementInfo elementA, final ExecutableElementInfo elementB) {

        this();

        this.add(elementA, elementB);
    }

    public void add(final ExecutableElementInfo elementA, final ExecutableElementInfo elementB) {
        this.codeFragmentA.add(elementA);
        this.codeFragmentB.add(elementB);
    }

    public void addAll(final SortedSet<ExecutableElementInfo> elementsA,
            final SortedSet<ExecutableElementInfo> elementsB) {
        this.codeFragmentA.addAll(elementsA);
        this.codeFragmentB.addAll(elementsB);
    }

    public int length() {
        return (this.codeFragmentA.length() + this.codeFragmentB.length()) / 2;
    }

    public CodeFragmentInfo getCodeFragmentA() {
        return this.codeFragmentA;
    }

    public CodeFragmentInfo getCodeFragmentB() {
        return this.codeFragmentB;
    }

    public boolean includedBy(final ClonePairInfo clonePair) {

        return (this.getCodeFragmentA().includedBy(clonePair.getCodeFragmentA()) && this
                .getCodeFragmentB().includedBy(clonePair.getCodeFragmentB()))
                || (this.getCodeFragmentB().includedBy(clonePair.getCodeFragmentA()) && this
                        .getCodeFragmentA().includedBy(clonePair.getCodeFragmentB()));
    }

    public int getID() {
        return this.id;
    }

    @Override
    public ClonePairInfo clone() {

        final ClonePairInfo clonePair = new ClonePairInfo();
        final CodeFragmentInfo cloneA = this.getCodeFragmentA();
        final CodeFragmentInfo cloneB = this.getCodeFragmentB();
        clonePair.addAll(cloneA.getElements(), cloneB.getElements());

        return clonePair;
    }

    final private CodeFragmentInfo codeFragmentA;

    final private CodeFragmentInfo codeFragmentB;

    final private int id;

    private static int number = 0;

    public static String CLONEPAIR = new String("CLONEPAIR");
}
