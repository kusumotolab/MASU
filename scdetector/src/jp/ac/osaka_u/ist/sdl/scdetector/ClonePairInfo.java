package jp.ac.osaka_u.ist.sdl.scdetector;


import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElement;


public class ClonePairInfo implements Serializable, Entity {

    public ClonePairInfo(final ExecutableElement statementA, final ExecutableElement statementB) {

        this.cloneA = new TreeSet<ExecutableElement>();
        this.cloneB = new TreeSet<ExecutableElement>();

        this.cloneA.add(statementA);
        this.cloneB.add(statementB);
        this.id = number++;
    }

    public void add(final ExecutableElement statementA, final ExecutableElement statementB) {
        this.cloneA.add(statementA);
        this.cloneB.add(statementB);
    }

    public int size() {
        return (this.cloneA.size() + this.cloneB.size()) / 2;
    }

    public SortedSet<ExecutableElement> getCloneA() {
        return this.cloneA;
    }

    public SortedSet<ExecutableElement> getCloneB() {
        return this.cloneB;
    }

    public int getID() {
        return this.id;
    }

    final private SortedSet<ExecutableElement> cloneA;

    final private SortedSet<ExecutableElement> cloneB;

    final private int id;

    private static int number = 0;

    public static String CLONEPAIR = new String("CLONEPAIR");
}
