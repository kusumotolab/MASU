package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGControlEdge;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;


/**
 * caseエントリを表すCFGノード
 * 
 * @author higo
 *
 */
public class CFGCaseEntryNode extends CFGNormalNode<CaseEntryInfo> {

    CFGCaseEntryNode(final CaseEntryInfo caseEntry) {
        super(caseEntry);
    }

    @Override
    public boolean optimize() {

        final Set<CFGNode<?>> backwardNodes = this.getBackwardNodes();
        final Set<CFGNode<?>> forwardNodes = this.getForwardNodes();

        this.remove();

        // バックワードノード群とフォワードノード群をつなぐ
        for (final CFGNode<?> backwardNode : backwardNodes) {
            for (final CFGNode<?> forwardNode : forwardNodes) {
                final CFGControlEdge edge = new CFGControlEdge(backwardNode, forwardNode, true);
                backwardNode.addForwardEdge(edge);
                forwardNode.addBackwardEdge(edge);
            }
        }

        return true;
    }
}
