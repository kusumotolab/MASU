package jp.ac.osaka_u.ist.sel.metricstool.pdg;


/**
 * §ŒäˆË‘¶•Ó‚ğ•\‚·ƒNƒ‰ƒX
 * @author t-miyake, higo
 *
 */
public class ControlDependenceEdge extends PDGEdge {

    public ControlDependenceEdge(final PDGControlNode fromNode, final PDGNode<?> toNode) {
        super(fromNode, toNode);
    }
}
