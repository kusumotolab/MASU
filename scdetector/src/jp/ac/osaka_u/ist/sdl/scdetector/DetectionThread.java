package jp.ac.osaka_u.ist.sdl.scdetector;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sdl.scdetector.data.ClonePairInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.data.NodePairInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.data.NodePairListInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.Configuration;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.SLICE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

class DetectionThread implements Runnable {

	private final NodePairListInfo nodePairList;

	private final int id;

	private final Map<TwoClassHash, SortedSet<ClonePairInfo>> clonePairs;

	DetectionThread(final int id, final NodePairListInfo nodePairList,
			final Map<TwoClassHash, SortedSet<ClonePairInfo>> clonePairs) {
		this.id = id;
		this.nodePairList = nodePairList;
		this.clonePairs = clonePairs;
	}

	@Override
	public void run() {

		final Set<ClonePairInfo> pairs = new HashSet<ClonePairInfo>();
		while (nodePairList.hasNext()) {
			final NodePairInfo nodePair = this.nodePairList.next();

			// System.out.println(this.id);
			final PDGNode<?> nodeA = nodePair.getNodeA();
			final PDGNode<?> nodeB = nodePair.getNodeB();

			/*
			 * // nodeAとnodeBの間がすべて同値ノードである場合はクローンを検出しない //
			 * 一時的な処理，これを改良するのは1つの研究テーマになる if
			 * (Utility.getEquivalenceNodesFollowedWithExecutionDependency
			 * (nodeA).contains(nodeB) ||
			 * Utility.getEquivalenceNodesFollowedWithExecutionDependency
			 * (nodeB).contains( nodeA)) { continue; }
			 */

			final ClonePairInfo clonePair = new ClonePairInfo();
			if (nodeA instanceof PDGMergedNode) {
				clonePair.codecloneA.addAll(((PDGMergedNode) nodeA).getCores());
			} else {
				clonePair.codecloneA.add(nodeA.getCore());
			}

			if (nodeB instanceof PDGMergedNode) {
				clonePair.codecloneB.addAll(((PDGMergedNode) nodeB).getCores());
			} else {
				clonePair.codecloneB.add(nodeB.getCore());
			}

			final HashSet<PDGNode<?>> checkedNodesA = new HashSet<PDGNode<?>>();
			final HashSet<PDGNode<?>> checkedNodesB = new HashSet<PDGNode<?>>();
			checkedNodesA.add(nodeA);
			checkedNodesB.add(nodeB);

			increaseNumberOfPairs();

			if (Configuration.INSTANCE.getT().contains(SLICE_TYPE.BACKWARD)) {
				ProgramSlice.addDuplicatedElementsWithBackwordSlice(nodeA,
						nodeB, clonePair, checkedNodesA, checkedNodesB);
			}
			if (Configuration.INSTANCE.getT().contains(SLICE_TYPE.FORWARD)) {
				ProgramSlice.addDuplicatedElementsWithForwordSlice(nodeA,
						nodeB, clonePair, checkedNodesA, checkedNodesB);
			}

			if ((Configuration.INSTANCE.getS() <= clonePair.codecloneA.length())
					&& (Configuration.INSTANCE.getS() <= clonePair.codecloneB
							.length())) {
				pairs.add(clonePair);

				/*
				 * SortedSet<ClonePairInfo> pairs = this.clonePairs .get(new
				 * TwoClassHash(clonePair)); if (null == pairs) { pairs =
				 * Collections .<ClonePairInfo> synchronizedSortedSet(new
				 * TreeSet<ClonePairInfo>()); this.clonePairs.put(new
				 * TwoClassHash(clonePair), pairs); } pairs.add(clonePair);
				 */
			}
		}

		addClonePairs(this.clonePairs, pairs);
	}

	synchronized static void addClonePairs(
			Map<TwoClassHash, SortedSet<ClonePairInfo>> clonePairs,
			Set<ClonePairInfo> detectedPairs) {

		for (final ClonePairInfo detectedPair : detectedPairs) {
			SortedSet<ClonePairInfo> pairs = clonePairs.get(new TwoClassHash(
					detectedPair));
			if (null == pairs) {
				pairs = new TreeSet<ClonePairInfo>();
				clonePairs.put(new TwoClassHash(detectedPair), pairs);
			}
			pairs.add(detectedPair);
		}
	}

	synchronized static void increaseNumberOfPairs() {
		numberOfPairs++;
	}

	synchronized static void increaseNumberOfComparison() {
		numberOfComparion++;
	}

	static int numberOfPairs = 0;

	static long numberOfComparion = 0;
}
