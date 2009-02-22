package jp.ac.osaka_u.ist.sel.metricstool.pdg;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;

/**
 * êßå‰àÀë∂ï”Çï\Ç∑ÉNÉâÉX
 * @author t-miyake
 *
 */
public class ControlDependenceEdge extends PDGEdge {

    public ControlDependenceEdge(final PDGControlNode fromNode, final PDGNode<?> toNode) {
        super(fromNode, toNode);
        
        if(!(fromNode.getCore() instanceof ConditionalBlockInfo)) {
            throw new IllegalArgumentException("fromNode is not a control statement.");
        }
    }
    
}
