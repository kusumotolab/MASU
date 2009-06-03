package jp.ac.osaka_u.ist.sel.metricstool.pdg;


public class PDGExecutionDependenceEdge extends PDGEdge {

    public PDGExecutionDependenceEdge(final PDGNode<?> fromNode, final PDGNode<?> toNode) {
        super(fromNode, toNode);
    }

    @Override
    public String getDependenceString() {
        return "";
    }
}
