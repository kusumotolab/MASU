package jp.ac.osaka_u.ist.sel.metricstool.cfg.edge;


import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGNode;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;


public class CFGExceptionEdge extends CFGEdge {

    public CFGExceptionEdge(CFGNode<?> fromNode, final CFGNode<?> toNode,
            final TypeInfo thrownException) {
        super(fromNode, toNode);
        this.thrownException = thrownException;
    }

    public TypeInfo getThrownException() {
        return this.thrownException;
    }

    @Override
    public String getDependenceTypeString() {
        return "exception";
    }

    @Override
    public String getDependenceString() {
        return this.getThrownException().getTypeName();
    }

    private final TypeInfo thrownException;
}
