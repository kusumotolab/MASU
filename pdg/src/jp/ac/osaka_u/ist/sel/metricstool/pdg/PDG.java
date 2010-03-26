package jp.ac.osaka_u.ist.sel.metricstool.pdg;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

/**
 * PDGを表すクラス
 * 
 * @author t-miyake
 * 
 */
public abstract class PDG {

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

		this.nodes = new TreeSet<PDGNode<?>>();
		// this.statementNodeCache = new HashMap<StatementInfo,
		// ControllableNode<? extends StatementInfo>>();

	}

	/**
	 * PDGを構築する
	 */
	protected abstract void buildPDG();

	/**
	 * PDG内のノード数を取得
	 * 
	 * @return PDG内のノード数
	 */
	public final int getNumberOfNodes() {
		return this.nodes.size();
	}

	/**
	 * PDGのノードを取得，ない場合はnullを返す
	 * 
	 * @param element
	 *            取得したいノードに対応する要素
	 * @return ノード
	 */
	public final PDGNode<?> getNode(final Object element) {

		if (null == element) {
			throw new IllegalArgumentException();
		}

		final PDGNode<?> node = this.getNodeFactory().getNode(element);
		return null == node ? null : this.getAllNodes().contains(node) ? node
				: null;
	}

	/**
	 * PDGの全ノードを返す
	 * 
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

	public void addNode(final PDGNode<?> node) {
		this.nodes.add(node);
	}

	public void removeNode(final PDGNode<?> node) {
		this.nodes.remove(node);
	}
}
