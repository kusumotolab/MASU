package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ContinueStatementInfo;


/**
 * continue文を表すノードのためのクラス
 * 
 * @author higo
 *
 */
public class CFGContinueStatementNode extends CFGJumpStatementNode {

    /**
     * ノードを生成するcontinue文を与えて初期化
     * 
     * @param continueStatement
     */
    public CFGContinueStatementNode(final ContinueStatementInfo continueStatement) {
        super(continueStatement);
    }
    
    @Override
    protected void optimize() {

        final Set<CFGNode<?>> forwardNodes = (HashSet<CFGNode<?>>) ((HashSet<CFGNode<?>>) this.forwardNodes)
                .clone();
        final Set<CFGNode<?>> backwardNodes = (HashSet<CFGNode<?>>) ((HashSet<CFGNode<?>>) this.backwardNodes)
                .clone();

        for (final CFGNode<?> forwardNode : forwardNodes) {
            forwardNode.removeBackwardNode(this);
        }

        for (final CFGNode<?> backwardNode : backwardNodes) {
            backwardNode.removeForwardNode(this);
        }
    }
}
