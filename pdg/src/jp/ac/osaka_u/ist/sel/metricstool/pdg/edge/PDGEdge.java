package jp.ac.osaka_u.ist.sel.metricstool.pdg.edge;

import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;


public abstract class PDGEdge implements Comparable<PDGEdge> {

    private final PDGNode<?> fromNode;

    private final PDGNode<?> toNode;

    public PDGEdge(PDGNode<?> fromNode, final PDGNode<?> toNode) {
        if (null == fromNode || null == toNode) {
            throw new IllegalArgumentException();
        }

        this.fromNode = fromNode;
        this.toNode = toNode;
    }

    public abstract String getDependenceString();

    public abstract String getDependenceTypeString();

    public final PDGNode<?> getFromNode() {
        return this.fromNode;
    }

    public final PDGNode<?> getToNode() {
        return this.toNode;
    }

    public void vanish() {
        this.fromNode.removeForwardEdge(this);
        this.toNode.removeBackwardEdge(this);
    }

    @Override
    public boolean equals(Object arg) {
        if (this.getClass().equals(arg.getClass())) {
            PDGEdge edge = (PDGEdge) arg;
            return this.fromNode.equals(edge.getFromNode()) && this.toNode.equals(getToNode());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        final int fromHash = this.fromNode.hashCode() * 10;
        final int toHash = this.toNode.hashCode();
        return fromHash + toHash;
    }

    @Override
    public int compareTo(final PDGEdge edge) {

        if (null == edge) {
            throw new IllegalArgumentException();
        }

        final int fromOrder = this.getFromNode().compareTo(edge.getFromNode());
        if (0 != fromOrder) {
            return fromOrder;
        }

        final int toOrder = this.getToNode().compareTo(edge.getToNode());
        if (0 != toOrder) {
            return toOrder;
        }

        return this.getDependenceTypeString().compareTo(edge.getDependenceTypeString());
    }
}
