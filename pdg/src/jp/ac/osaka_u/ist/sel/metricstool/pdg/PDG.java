package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;


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
    protected final SortedSet<PDGNode<?>> enterNodes;

    /**
     * PDGの出口ノード
     */
    protected final SortedSet<PDGNode<?>> exitNodes;

    /**
     * PDGを構成するノード
     */
    protected final SortedSet<PDGNode<?>> nodes;

    /**
     * 
     * ノード作成時に用いるファクトリ
     */
    protected final IPDGNodeFactory nodeFactory;

    public PDG(final IPDGNodeFactory nodeFactory) {

        if (null == nodeFactory) {
            throw new IllegalArgumentException();
        }
        this.nodeFactory = nodeFactory;

        this.enterNodes = new TreeSet<PDGNode<?>>();
        this.exitNodes = new TreeSet<PDGNode<?>>();
        this.nodes = new TreeSet<PDGNode<?>>();
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
    public final SortedSet<PDGNode<?>> getEnterNodes() {
        return Collections.unmodifiableSortedSet(this.enterNodes);
    }

    /**
     * 出口ノードを取得
     * @return 出口ノード
     */
    public final SortedSet<PDGNode<?>> getExitNodes() {
        return Collections.unmodifiableSortedSet(this.exitNodes);
    }

    /**
     * PDG内のノード数を取得
     * @return PDG内のノード数
     */
    public final int getNumberOfNodes() {
        return this.nodes.size();
    }

    /**
     * PDGのノードを取得，ない場合はnullを返す
     * 
     * @param element 取得したいノードに対応する要素
     * @return ノード
     */
    public final PDGNode<?> getNode(final Object element) {

        if (null == element) {
            throw new IllegalArgumentException();
        }

        final PDGNode<?> node = this.getNodeFactory().getNode(element);
        return null == node ? null : this.getAllNodes().contains(node) ? node : null;
    }

    /**
     * PDGの全ノードを返す
     * @return PDGの全ノード
     */
    public final SortedSet<? extends PDGNode<?>> getAllNodes() {
        return Collections.unmodifiableSortedSet(this.nodes);
    }

    /**
     * PDGの全エッジを返す
     * 
     * @return PDGの全エッジ
     */
    public final SortedSet<? extends PDGEdge> getAllEdges() {
        final SortedSet<PDGEdge> edges = new TreeSet<PDGEdge>();
        for (final PDGNode<?> node : this.getAllNodes()) {
            edges.addAll(node.getBackwardEdges());
            edges.addAll(node.getForwardEdges());
        }
        return Collections.unmodifiableSortedSet(edges);
    }

    /**
     * PDG構築に用いたファクトリを返す
     * 
     * @return PDG構築に用いたファクトリ
     */
    public IPDGNodeFactory getNodeFactory() {
        return this.nodeFactory;
    }

}
