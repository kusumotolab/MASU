package jp.ac.osaka_u.ist.sdl.scdetector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sdl.scdetector.data.ClonePairInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.Configuration;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.SLICE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGAcrossEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGControlDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGDataDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGExecutionDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

public class SystemSlicing extends Slicing {

	final private PDGNode<?> pointA;
	final private PDGNode<?> pointB;

	final private Set<PDGNode<?>> checkedNodesA;
	final private Set<PDGNode<?>> checkedNodesB;

	final private Stack<CallInfo<?>> callStackA;
	final private Stack<CallInfo<?>> callStackB;

	private ClonePairInfo clonepair;
	
	public SystemSlicing(final PDGNode<?> pointA, final PDGNode<?> pointB) {
		this.pointA = pointA;
		this.pointB = pointB;
		this.checkedNodesA = new HashSet<PDGNode<?>>();
		this.checkedNodesB = new HashSet<PDGNode<?>>();
		this.callStackA = new Stack<CallInfo<?>>();
		this.callStackB = new Stack<CallInfo<?>>();
		this.clonepair = null;
	}

	public ClonePairInfo perform() {
		if (null == this.clonepair) {
			this.clonepair = new ClonePairInfo();
			this.perform(this.pointA, this.pointB);
		}
		return this.clonepair;
	}

	public void perform(final PDGNode<?> nodeA, final PDGNode<?> nodeB) {

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

		// メソッド間バックワードスライス用の必要な情報を取得
		// Data Dependencyに対する後ろ向きスライスは，以下の状態のときは行わない
		// 1. メソッドスタックが空のとき
		// 2. メソッドスタックが空でなく，スライス先のノードがスタックの最上部のノードと異なるメソッドにあるとき
		final Map<PDGNode<?>, CallInfo<?>> acrossBackwardNodesA = new HashMap<PDGNode<?>, CallInfo<?>>();
		if (this.callStackA.isEmpty()) {
			for (final Iterator<PDGDataDependenceEdge> iterator = backwardDataEdgesA
					.iterator(); iterator.hasNext();) {
				final PDGDataDependenceEdge edge = iterator.next();
				if (edge instanceof PDGAcrossEdge) {
					iterator.remove();
				}
			}
		} else {
			final CallInfo<?> call = this.callStackA.peek();
			for (final Iterator<PDGDataDependenceEdge> iterator = backwardDataEdgesA
					.iterator(); iterator.hasNext();) {
				final PDGDataDependenceEdge edge = iterator.next();
				if (edge instanceof PDGAcrossEdge) {
					final PDGAcrossEdge acrossEdge = (PDGAcrossEdge) edge;
					if (!call.equals(acrossEdge.getHolder())) {
						iterator.remove();
					} else {
						acrossBackwardNodesA.put(edge.getFromNode(), acrossEdge
								.getHolder());
					}
				}
			}
		}

		final Map<PDGNode<?>, CallInfo<?>> acrossBackwardNodesB = new HashMap<PDGNode<?>, CallInfo<?>>();
		if (this.callStackB.isEmpty()) {
			for (final Iterator<PDGDataDependenceEdge> iterator = backwardDataEdgesB
					.iterator(); iterator.hasNext();) {
				final PDGDataDependenceEdge edge = iterator.next();
				if (edge instanceof PDGAcrossEdge) {
					iterator.remove();
				}
			}
		} else {
			final CallInfo<?> call = this.callStackB.peek();
			for (final Iterator<PDGDataDependenceEdge> iterator = backwardDataEdgesB
					.iterator(); iterator.hasNext();) {
				final PDGDataDependenceEdge edge = iterator.next();
				if (edge instanceof PDGAcrossEdge) {
					final PDGAcrossEdge acrossEdge = (PDGAcrossEdge) edge;
					if (!call.equals(acrossEdge.getHolder())) {
						iterator.remove();
					} else {
						acrossBackwardNodesB.put(edge.getFromNode(), acrossEdge
								.getHolder());
					}
				}
			}
		}

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

		// メソッド間フォワードスライス用の必要な情報を取得
		final Map<PDGNode<?>, CallInfo<?>> acrossForwardNodesA = new HashMap<PDGNode<?>, CallInfo<?>>();
		for (final PDGDataDependenceEdge edge : forwardDataEdgesA) {
			if (edge instanceof PDGAcrossEdge) {
				final PDGAcrossEdge acrossEdge = (PDGAcrossEdge) edge;
				acrossForwardNodesA.put(edge.getToNode(), acrossEdge
						.getHolder());
			}
		}
		final Map<PDGNode<?>, CallInfo<?>> acrossForwardNodesB = new HashMap<PDGNode<?>, CallInfo<?>>();
		for (final PDGDataDependenceEdge edge : forwardDataEdgesB) {
			if (edge instanceof PDGAcrossEdge) {
				final PDGAcrossEdge acrossEdge = (PDGAcrossEdge) edge;
				acrossForwardNodesB.put(edge.getToNode(), acrossEdge
						.getHolder());
			}
		}

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
					backwardExecutionNodesB, acrossBackwardNodesA,
					acrossBackwardNodesB, acrossForwardNodesA,
					acrossForwardNodesB);
			this.enlargeClonePair(backwardDataNodesA, backwardDataNodesB,
					acrossBackwardNodesA, acrossBackwardNodesB,
					acrossForwardNodesA, acrossForwardNodesB);
			this.enlargeClonePair(backwardControlNodesA, backwardControlNodesB,
					acrossBackwardNodesA, acrossBackwardNodesB,
					acrossForwardNodesA, acrossForwardNodesB);
		}

		// フォワードスライスを使う設定の場合
		if (Configuration.INSTANCE.getT().contains(SLICE_TYPE.FORWARD)) {
			this.enlargeClonePair(forwardExecutionNodesA,
					forwardExecutionNodesB, acrossBackwardNodesA,
					acrossBackwardNodesB, acrossForwardNodesA,
					acrossForwardNodesB);
			this.enlargeClonePair(forwardDataNodesA, forwardDataNodesB,
					acrossBackwardNodesA, acrossBackwardNodesB,
					acrossForwardNodesA, acrossForwardNodesB);
			this.enlargeClonePair(forwardControlNodesA, forwardControlNodesB,
					acrossBackwardNodesA, acrossBackwardNodesB,
					acrossForwardNodesA, acrossForwardNodesB);
		}

		// 現在のノードをクローンペアに追加
		this.clonepair.add(nodeA, nodeB);
	}

	private void enlargeClonePair(final SortedSet<PDGNode<?>> nodesA,
			final SortedSet<PDGNode<?>> nodesB,
			final Map<PDGNode<?>, CallInfo<?>> acrossBackwardNodesA,
			final Map<PDGNode<?>, CallInfo<?>> acrossBackwardNodesB,
			final Map<PDGNode<?>, CallInfo<?>> acrossForwardNodesA,
			final Map<PDGNode<?>, CallInfo<?>> acrossForwardNodesB) {

		NODEA: for (final PDGNode<?> nodeA : nodesA) {

			// 既にクローンに入ることが確定しているノードのときは調査しない
			// 相手側のクローンに入っているノードのときも調査しない
			if (this.checkedNodesA.contains(nodeA)
					|| this.checkedNodesB.contains(nodeA)) {
				continue NODEA;
			}

			// ノードのハッシュ値を得る
			Integer hashA = NODE_TO_HASH_MAP.get(nodeA);
			if (null == hashA) {
				final ExecutableElementInfo coreA = nodeA.getCore();
				hashA = Conversion.getNormalizedString(coreA).hashCode();
				NODE_TO_HASH_MAP.put(nodeA, hashA);
			}

			NODEB: for (final PDGNode<?> nodeB : nodesB) {

				// 既にクローンに入ることが確定しているノードのときは調査しない
				// 相手側のクローンに入っているノードのときも調査しない
				if (this.checkedNodesB.contains(nodeB)
						|| this.checkedNodesA.contains(nodeB)) {
					continue NODEB;
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
				if (hashA.intValue() == hashB.intValue()) {

					// ノードが同じ場合は調査しない
					if (nodeA == nodeB) {
						continue NODEB;
					}

					// メソッドをまたがる場合はコールスタックの更新を行う
					if (acrossBackwardNodesA.containsKey(nodeA)) {
						if (!this.callStackA.peek().equals(
								acrossBackwardNodesA.get(nodeA))) {
							throw new IllegalStateException();
						} else {
							this.callStackA.pop();
						}
					} else if (acrossForwardNodesA.containsKey(nodeA)) {
						this.callStackA.push(acrossForwardNodesA.get(nodeA));
					}
					if (acrossBackwardNodesB.containsKey(nodeB)) {
						if (!this.callStackB.peek().equals(
								acrossBackwardNodesB.get(nodeB))) {
							throw new IllegalStateException();
						} else {
							this.callStackB.pop();
						}
					} else if (acrossForwardNodesB.containsKey(nodeB)) {
						this.callStackB.push(acrossForwardNodesB.get(nodeB));
					}

					this.perform(nodeA, nodeB);

					// メソッドをまたがる場合はコールスタックの更新を行う（後処理として必要）
					if (acrossBackwardNodesA.containsKey(nodeA)) {
						this.callStackA.push(acrossBackwardNodesA.get(nodeA));
					} else if (acrossForwardNodesA.containsKey(nodeA)) {
						this.callStackA.pop();
					}
					if (acrossBackwardNodesB.containsKey(nodeB)) {
						this.callStackB.push(acrossBackwardNodesB.get(nodeB));
					} else if (acrossForwardNodesB.containsKey(nodeB)) {
						this.callStackB.pop();
					}
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
