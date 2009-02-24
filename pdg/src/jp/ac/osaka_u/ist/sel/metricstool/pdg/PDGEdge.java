package jp.ac.osaka_u.ist.sel.metricstool.pdg;


public abstract class PDGEdge {

    private final PDGNode<?> fromNode;

    private final PDGNode<?> toNode;

    public PDGEdge(PDGNode<?> fromNode, final PDGNode<?> toNode) {
        if (null == fromNode || null == toNode) {
            throw new IllegalArgumentException();
        }

        this.fromNode = fromNode;
        this.toNode = toNode;
    }

    public PDGNode<?> getFromNode() {
        return this.fromNode;
    }

    public PDGNode<?> getToNode() {
        return this.toNode;
    }
    
    public void vanish(){
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

}
