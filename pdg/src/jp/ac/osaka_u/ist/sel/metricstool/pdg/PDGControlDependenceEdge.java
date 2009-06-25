package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


/**
 * 制御依存辺を表すクラス
 * @author t-miyake, higo
 *
 */
public class PDGControlDependenceEdge extends PDGEdge {

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

    public PDGControlDependenceEdge(final PDGControlNode fromNode, final PDGNode<?> toNode,
            final boolean trueDependence) {
        super(fromNode, toNode);

        this.trueDependence = trueDependence;

    }

    public boolean isTrueDependence() {
        return this.trueDependence;
    }

    public boolean isFalseDependence() {
        return !this.trueDependence;
    }

    @Override
    public String getDependenceString() {
        return this.trueDependence ? "true" : "false";
    }
    
    @Override
    public String getDependenceTypeString() {
        return "Control Dependency";
    }

    private final boolean trueDependence;
}
