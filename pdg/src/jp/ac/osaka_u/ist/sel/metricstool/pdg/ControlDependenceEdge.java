package jp.ac.osaka_u.ist.sel.metricstool.pdg;


/**
 * 制御依存辺を表すクラス
 * @author t-miyake, higo
 *
 */
public class ControlDependenceEdge extends PDGEdge {

    public ControlDependenceEdge(final PDGControlNode fromNode, final PDGNode<?> toNode) {
        super(fromNode, toNode);
    }
}
