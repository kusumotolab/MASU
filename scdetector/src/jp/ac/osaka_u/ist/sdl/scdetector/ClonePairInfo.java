package jp.ac.osaka_u.ist.sdl.scdetector;


import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;


public class ClonePairInfo {

    public ClonePairInfo(final StatementInfo statementA, final StatementInfo statementB) {

        this.cloneA = new TreeSet<StatementInfo>();
        this.cloneB = new TreeSet<StatementInfo>();

        this.cloneA.add(statementA);
        this.cloneB.add(statementB);
    }

    public void add(final StatementInfo statementA, final StatementInfo statementB) {
        this.cloneA.add(statementA);
        this.cloneB.add(statementB);
    }

    public int size() {
        return (this.cloneA.size() + this.cloneB.size()) / 2;
    }

    public SortedSet<StatementInfo> getCloneA() {
        return this.cloneA;
    }

    public SortedSet<StatementInfo> getCloneB() {
        return this.cloneB;
    }

    final private SortedSet<StatementInfo> cloneA;

    final private SortedSet<StatementInfo> cloneB;
}
