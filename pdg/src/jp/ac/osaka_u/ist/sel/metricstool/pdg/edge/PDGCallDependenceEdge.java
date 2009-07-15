package jp.ac.osaka_u.ist.sel.metricstool.pdg.edge;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;


public class PDGCallDependenceEdge extends PDGEdge {

    /**
     * エッジの集合から，制御依存を表すエッジのみを抽出し，そのSetを返す
     * 
     * @param edges
     * @return
     */
    public static Set<PDGControlDependenceEdge> getControlDependenceEdge(final Set<PDGEdge> edges) {
        final Set<PDGControlDependenceEdge> controlDependenceEdges = new HashSet<PDGControlDependenceEdge>();
        for (final PDGEdge edge : edges) {
            if (edge instanceof PDGControlDependenceEdge) {
                controlDependenceEdges.add((PDGControlDependenceEdge) edge);
            }
        }
        return Collections.unmodifiableSet(controlDependenceEdges);
    }

    public PDGCallDependenceEdge(final PDGNode<?> fromNode, final PDGNode<?> toNode,
            final CallInfo<?> call) {
        super(fromNode, toNode);
        this.call = call;
    }

    public CallInfo<? extends CallableUnitInfo> getCallInfo() {
        return this.call;
    }

    @Override
    public String getDependenceString() {
        return this.getCallInfo().getText();
    }

    @Override
    public String getDependenceTypeString() {
        return "Control Dependency";
    }

    private final CallInfo<? extends CallableUnitInfo> call;
}
