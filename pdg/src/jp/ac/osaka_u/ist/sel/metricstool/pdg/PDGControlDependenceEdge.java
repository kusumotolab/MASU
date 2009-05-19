package jp.ac.osaka_u.ist.sel.metricstool.pdg;


/**
 * êßå‰àÀë∂ï”Çï\Ç∑ÉNÉâÉX
 * @author t-miyake, higo
 *
 */
public class PDGControlDependenceEdge extends PDGEdge {

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

    private final boolean trueDependence;
}
