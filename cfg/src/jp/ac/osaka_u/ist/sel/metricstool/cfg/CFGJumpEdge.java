package jp.ac.osaka_u.ist.sel.metricstool.cfg;


public class CFGJumpEdge extends CFGEdge {

    public CFGJumpEdge(CFGNode<?> fromNode, final CFGNode<?> toNode) {
        super(fromNode, toNode);
    }

    @Override
    public String getDependenceTypeString() {
        return "jump";
    }
    
    @Override
    public String getDependenceString() {
        return "jump";
    }
}
