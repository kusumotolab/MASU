package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;


public class PDGDataDependenceEdge extends PDGEdge {

    public PDGDataDependenceEdge(final PDGNode<?> fromNode, final PDGNode<?> toNode,
            final VariableInfo<?> data) {
        super(fromNode, toNode);

        this.data = data;
    }

    public VariableInfo<?> getVariable() {
        return this.data;
    }
    
    @Override
    public String getDependenceString(){
        return this.data.getName();
    }

    private final VariableInfo<?> data;
}
