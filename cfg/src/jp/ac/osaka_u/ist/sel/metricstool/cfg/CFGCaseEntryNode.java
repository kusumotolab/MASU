package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;


public class CFGCaseEntryNode extends CFGNormalNode<CaseEntryInfo> {

    public CFGCaseEntryNode(final CaseEntryInfo caseEntry) {
        super(caseEntry);
    }

    @Override
    protected void optimize() {

        //このノードのバックワードノード群を取得
        final Set<CFGNode<?>> backwardNodes = new HashSet<CFGNode<?>>();
        for (final CFGEdge backwardEdge : this.getBackwardEdges()) {

            backwardNodes.add(backwardEdge.getFromNode());
        }

        // このノードのフォワードノード群を取得
        final Set<CFGNode<?>> forwardNodes = new HashSet<CFGNode<?>>();
        for (final CFGEdge forwardEdge : this.getForwardEdges()) {
            forwardNodes.add(forwardEdge.getToNode());
        }

        // バックワードノードから，このノードをフォワードノードとするエッジを削除
        for (final CFGNode<?> backwardNode : this.getBackwardNodes()) {
            backwardNode.removeForwardEdges(this.getBackwardEdges());
        }

        // フォワードノードから，このノードをバックワードノードとするエッジを削除
        for (final CFGNode<?> forwardNode : this.getForwardNodes()) {
            forwardNode.removeBackwardEdges(this.getForwardEdges());
        }

        // バックワードノード群とフォワードノード群をつなぐ
        for (final CFGNode<?> backwardNode : backwardNodes) {
            for (final CFGNode<?> forwardNode : forwardNodes) {
                final CFGControlEdge edge = new CFGControlEdge(backwardNode, forwardNode, true);
                backwardNode.addForwardEdge(edge);
            }
        }
    }
}
