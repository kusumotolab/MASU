package jp.ac.osaka_u.ist.sel.metricstool.cfg;


public class CFGExceptionEdge extends CFGEdge {

    public CFGExceptionEdge(CFGNode<?> fromNode, final CFGNode<?> toNode) {
        super(fromNode, toNode);
    }

    @Override
    public String getDependenceTypeString() {
        return "exception";
    }
}
