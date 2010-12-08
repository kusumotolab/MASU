package jp.ac.osaka_u.ist.sdl.scdetector;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sdl.scdetector.data.ClonePairInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.Configuration;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.SLICE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGControlDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGDataDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGExecutionDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGDataNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

public class ProgramSlicing extends Slicing {

	public ProgramSlicing(final PDGNode<?> pointA, final PDGNode<?> pointB) {
		this.pointA = pointA;
		this.pointB = pointB;
		this.checkedNodesA = new HashSet<PDGNode<?>>();
		this.checkedNodesB = new HashSet<PDGNode<?>>();
		this.clonepair = null;
	}

	public ClonePairInfo perform() {
		if (null != this.clonepair) {
			return this.clonepair;
		} else {
			this.clonepair = new ClonePairInfo();
			this.perform(this.pointA, this.pointB);
			return this.clonepair;
		}
	}

	final private PDGNode<?> pointA;
	final private PDGNode<?> pointB;

	final private Set<PDGNode<?>> checkedNodesA;
	final private Set<PDGNode<?>> checkedNodesB;

	private ClonePairInfo clonepair;

	private void perform(final PDGNode<?> nodeA, final PDGNode<?> nodeB) {

		// このノードをチェック済みノード集合に追加，この処理は再帰呼び出しの前でなければならない
		this.checkedNodesA.add(nodeA);
		this.checkedNodesB.add(nodeB);

		// ここから，各エッジの先にあるノードの集合を得るための処理
		final SortedSet<PDGEdge> backwardEdgesA = nodeA.getBackwardEdges();
		final SortedSet<PDGEdge> backwardEdgesB = nodeB.getBackwardEdges();
		final SortedSet<PDGEdge> forwardEdgesA = nodeA.getForwardEdges();
		final SortedSet<PDGEdge> forwardEdgesB = nodeB.getForwardEdges();

		final SortedSet<PDGExecutionDependenceEdge> backwardExecutionEdgesA = PDGExecutionDependenceEdge
				.extractExecutionDependenceEdge(backwardEdgesA);
		final SortedSet<PDGDataDependenceEdge> backwardDataEdgesA = PDGDataDependenceEdge
				.extractDataDependenceEdge(backwardEdgesA);
		final SortedSet<PDGControlDependenceEdge> backwardControlEdgesA = PDGControlDependenceEdge
				.extractControlDependenceEdge(backwardEdgesA);
		final SortedSet<PDGExecutionDependenceEdge> backwardExecutionEdgesB = PDGExecutionDependenceEdge
				.extractExecutionDependenceEdge(backwardEdgesB);
		final SortedSet<PDGDataDependenceEdge> backwardDataEdgesB = PDGDataDependenceEdge
				.extractDataDependenceEdge(backwardEdgesB);
		final SortedSet<PDGControlDependenceEdge> backwardControlEdgesB = PDGControlDependenceEdge
				.extractControlDependenceEdge(backwardEdgesB);

		final SortedSet<PDGNode<?>> backwardExecutionNodesA = this
				.getFromNodes(backwardExecutionEdgesA);
		final SortedSet<PDGNode<?>> backwardDataNodesA = this
				.getFromNodes(backwardDataEdgesA);
		final SortedSet<PDGNode<?>> backwardControlNodesA = this
				.getFromNodes(backwardControlEdgesA);
		final SortedSet<PDGNode<?>> backwardExecutionNodesB = this
				.getFromNodes(backwardExecutionEdgesB);
		final SortedSet<PDGNode<?>> backwardDataNodesB = this
				.getFromNodes(backwardDataEdgesB);
		final SortedSet<PDGNode<?>> backwardControlNodesB = this
				.getFromNodes(backwardControlEdgesB);

		final SortedSet<PDGExecutionDependenceEdge> forwardExecutionEdgesA = PDGExecutionDependenceEdge
				.extractExecutionDependenceEdge(forwardEdgesA);
		final SortedSet<PDGDataDependenceEdge> forwardDataEdgesA = PDGDataDependenceEdge
				.extractDataDependenceEdge(forwardEdgesA);
		final SortedSet<PDGControlDependenceEdge> forwardControlEdgesA = PDGControlDependenceEdge
				.extractControlDependenceEdge(forwardEdgesA);
		final SortedSet<PDGExecutionDependenceEdge> forwardExecutionEdgesB = PDGExecutionDependenceEdge
				.extractExecutionDependenceEdge(forwardEdgesB);
		final SortedSet<PDGDataDependenceEdge> forwardDataEdgesB = PDGDataDependenceEdge
				.extractDataDependenceEdge(forwardEdgesB);
		final SortedSet<PDGControlDependenceEdge> forwardControlEdgesB = PDGControlDependenceEdge
				.extractControlDependenceEdge(forwardEdgesB);

		final SortedSet<PDGNode<?>> forwardExecutionNodesA = this
				.getToNodes(forwardExecutionEdgesA);
		final SortedSet<PDGNode<?>> forwardDataNodesA = this
				.getToNodes(forwardDataEdgesA);
		final SortedSet<PDGNode<?>> forwardControlNodesA = this
				.getToNodes(forwardControlEdgesA);
		final SortedSet<PDGNode<?>> forwardExecutionNodesB = this
				.getToNodes(forwardExecutionEdgesB);
		final SortedSet<PDGNode<?>> forwardDataNodesB = this
				.getToNodes(forwardDataEdgesB);
		final SortedSet<PDGNode<?>> forwardControlNodesB = this
				.getToNodes(forwardControlEdgesB);

		// 各ノードの集合に対してその先にあるクローンペアの構築
		// バックワードスライスを使う設定の場合
		if (Configuration.INSTANCE.getT().contains(SLICE_TYPE.BACKWARD)) {
			this.enlargeClonePair(backwardExecutionNodesA,
					backwardExecutionNodesB);
			this.enlargeClonePair(backwardDataNodesA, backwardDataNodesB);
			this.enlargeClonePair(backwardControlNodesA, backwardControlNodesB);
		}

		// フォワードスライスを使う設定の場合
		if (Configuration.INSTANCE.getT().contains(SLICE_TYPE.FORWARD)) {
			this.enlargeClonePair(forwardExecutionNodesA,
					forwardExecutionNodesB);
			this.enlargeClonePair(forwardDataNodesA, forwardDataNodesB);
			this.enlargeClonePair(forwardControlNodesA, forwardControlNodesB);
		}

		// 現在のノードをクローンペアに追加
		this.clonepair.add(nodeA, nodeB);
	}

	private void enlargeClonePair(final SortedSet<PDGNode<?>> nodesA,
			final SortedSet<PDGNode<?>> nodesB) {

		for (final PDGNode<?> nodeA : nodesA) {

			// 既にクローンに入ることが確定しているノードのときは調査しない
			// 相手側のクローンに入っているノードのときも調査しない
			if (checkedNodesA.contains(nodeA) || checkedNodesB.contains(nodeA)) {
				continue;
			}

			// データノードの時は調査しない
			if (nodeA instanceof PDGDataNode<?>) {
				continue;
			}

			// ノードのハッシュ値を得る
			Integer hashA = NODE_TO_HASH_MAP.get(nodeA);
			if (null == hashA) {
				final ExecutableElementInfo coreA = nodeA.getCore();
				hashA = Conversion.getNormalizedString(coreA).hashCode();
				NODE_TO_HASH_MAP.put(nodeA, hashA);
			}

			for (final PDGNode<?> nodeB : nodesB) {

				// 既にクローンに入ることが確定しているノードのときは調査しない
				// 相手側のクローンに入っているノードのときも調査しない
				if (checkedNodesB.contains(nodeB)
						|| checkedNodesA.contains(nodeB)) {
					continue;
				}

				// データノードの時は調査しない
				if (nodeB instanceof PDGDataNode<?>) {
					continue;
				}

				// ノードのハッシュ値を得る
				Integer hashB = NODE_TO_HASH_MAP.get(nodeB);
				if (null == hashB) {
					final ExecutableElementInfo coreB = nodeB.getCore();
					hashB = Conversion.getNormalizedString(coreB).hashCode();
					NODE_TO_HASH_MAP.put(nodeB, hashB);
				}

				SlicingThread.increaseNumberOfComparison();

				// ノードのハッシュ値が等しい場合は，そのノードペアの先をさらに調査
				if (hashA.equals(hashB)) {

					// ノードが同じ場合は調査しない
					if (nodeA == nodeB) {
						continue;
					}

					this.perform(nodeA, nodeB);
				}
			}
		}
	}

	private SortedSet<PDGNode<?>> getFromNodes(
			final SortedSet<? extends PDGEdge> edges) {

		final SortedSet<PDGNode<?>> fromNodes = new TreeSet<PDGNode<?>>();

		for (final PDGEdge edge : edges) {
			fromNodes.add(edge.getFromNode());
		}

		return fromNodes;
	}

	private SortedSet<PDGNode<?>> getToNodes(
			final SortedSet<? extends PDGEdge> edges) {

		final SortedSet<PDGNode<?>> toNodes = new TreeSet<PDGNode<?>>();

		for (final PDGEdge edge : edges) {
			toNodes.add(edge.getToNode());
		}

		return toNodes;
	}

	private static ConcurrentMap<PDGNode<?>, Integer> NODE_TO_HASH_MAP = new ConcurrentHashMap<PDGNode<?>, Integer>();
}
