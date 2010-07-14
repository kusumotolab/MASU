package jp.ac.osaka_u.ist.sel.metricstool.cfg.edge;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGNode;


public class CFGControlEdge extends CFGEdge {

    public CFGControlEdge(CFGNode<?> fromNode, final CFGNode<?> toNode, final boolean control) {
        super(fromNode, toNode);
        this.control = control;
    }

    public boolean getControl() {
        return this.control;
    }

    @Override
    public String getDependenceString() {
        return Boolean.toString(this.control);
    }

    @Override
    public String getDependenceTypeString() {
        return "control";
    }
    
    private final boolean control;
}
