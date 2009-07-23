package jp.ac.osaka_u.ist.sel.metricstool.cfg;


public class CFGNormalEdge extends CFGEdge {

    public CFGNormalEdge(CFGNode<?> fromNode, final CFGNode<?> toNode) {
        super(fromNode, toNode);
    }

    @Override
    public String getDependenceTypeString() {
        return "normal";
    }
}
