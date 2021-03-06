package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGReturnStatementNode;


/**
 * Return文を表すPDGノード
 * 
 * @author higo
 *
 */
public class PDGReturnStatementNode extends PDGStatementNode<CFGReturnStatementNode> {

    PDGReturnStatementNode(final CFGReturnStatementNode node) {
        super(node);
    }
}
