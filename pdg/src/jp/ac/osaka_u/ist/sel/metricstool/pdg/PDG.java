package jp.ac.osaka_u.ist.sel.metricstool.pdg;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;

/**
 * PDGを表すクラス
 * 
 * @author t-miyake
 *
 */
public abstract class PDG {

    /**
     * PDGの入口ノード
     */
    protected final Set<PDGNode<?>> enterNodes;

    /**
     * PDGの出口ノード
     */
    protected final Set<PDGNode<?>> exitNodes;

    protected final IPDGNodeFactory nodeFactory;

    public PDG(final IPDGNodeFactory nodeFactory) {

        if(null == nodeFactory) {
            throw new IllegalArgumentException();
        }
        this.nodeFactory = nodeFactory;
        
        this.enterNodes = new HashSet<PDGNode<?>>();
        this.exitNodes = new HashSet<PDGNode<?>>();

        //this.statementNodeCache = new HashMap<StatementInfo, ControllableNode<? extends StatementInfo>>();

    }
    
    /**
     * PDGを構築する
     */
    protected abstract void buildPDG();
    
    /**
     * 入口ノードを取得
     * @return 入口ノード
     */
    public final Set<PDGNode<?>> getEnterNodes() {
        return this.enterNodes;
    }

    /**
     * 出口ノードを取得
     * @return 出口ノード
     */
    public final Set<PDGNode<?>> getExitNodes() {
        return this.exitNodes;
    }
    
    /**
     * PDG内のノード数を取得
     * @return 
     */
    public final int getNodeCount() {
        return this.nodeFactory.getAllNode().size();
    }
    
    /**
     * PDGのノードを取得
     * @param statement 取得したいノードに対応する文
     * @return 
     */
    public final PDGNode<?> getPDGNode(final StatementInfo statement) {
        return this.nodeFactory.getNode(statement);
    }
    
    /**
     * PDG上の全ノードを取得
     * @return PDGの全ノード
     */
    public final Collection<? extends PDGNode<?>> getAllNodes() {
        return this.nodeFactory.getAllNode();
    }
    
    public IPDGNodeFactory getNodeFactory() {
        return nodeFactory;
    }
    

}
