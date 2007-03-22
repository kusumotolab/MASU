package jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Member;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Visualizable;


public class FieldVertex extends AbstractVertex {
    final private ClassVertex ownerClass;

    public FieldVertex(final int id, final ClassVertex ownerClass, final String label,
            final String sortKey, final Visualizable visualizable, final Member member) {
        super(id, label, sortKey, visualizable, member);
        this.ownerClass = ownerClass;
    }

    public ClassVertex getOwnerClass() {
        return ownerClass;
    }
}
