package jp.ac.osaka_u.ist.sel.metricstool.cfg;

import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;

public abstract class CFG {

    protected final ICFGNodeFactory nodeFactory;

    protected CFGNode<? extends StatementInfo> enterNode;

    protected final Set<CFGNode<? extends StatementInfo>> exitNodes;

    protected CFG(final ICFGNodeFactory nodeFactory) {
        if (null == nodeFactory) {
            throw new IllegalArgumentException();
        }

        this.nodeFactory = nodeFactory;
        
        this.exitNodes = new HashSet<CFGNode<? extends StatementInfo>>();

    }

    protected void connectNodes(final Set<CFGNode<? extends StatementInfo>> fromNodes,
            CFGNode<? extends StatementInfo> toNode) {
        for (final CFGNode<? extends StatementInfo> fromNode : fromNodes) {
            fromNode.addForwardNode(toNode);
        }
    }

    /**
     * CFGの入り口ノードを返す
     * @return CFGの入り口ノード
     */
    public CFGNode<? extends StatementInfo> getEnterNode() {
        return enterNode;
    }

    /**
     * CFGの出口ノードを返す
     * @return CFGの出口ノード
     */
    public Set<CFGNode<? extends StatementInfo>> getExitNodes() {
        return exitNodes;
    }
    
    public boolean isEmpty() {
    	return null == this.enterNode;
    }
    
    public CFGNode<? extends StatementInfo> getCFGNode(final StatementInfo statement) {
        return this.nodeFactory.getNode(statement);
    }
    
}
