package jp.ac.osaka_u.ist.sel.metricstool.cfg;


public abstract class CFGEdge {

    CFGEdge(final CFGNode<?> fromNode, final CFGNode<?> toNode) {

        if (null == fromNode || null == toNode) {
            throw new IllegalArgumentException();
        }

        this.fromNode = fromNode;
        this.toNode = toNode;
    }

    public final CFGNode<?> getFromNode() {
        return this.fromNode;
    }

    public final CFGNode<?> getToNode() {
        return this.toNode;
    }

    private final CFGNode<?> fromNode;

    private final CFGNode<?> toNode;
}
