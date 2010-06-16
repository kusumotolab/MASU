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
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

class SlicingThread implements Runnable {

	private final NodePairListInfo nodePairList;

	private final int id;

	private final Map<TwoClassHash, SortedSet<ClonePairInfo>> clonePairs;

	SlicingThread(final int id, final NodePairListInfo nodePairList,
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

			final PDGNode<?> nodeA = nodePair.getNodeA();
			final PDGNode<?> nodeB = nodePair.getNodeB();
			final HashSet<PDGNode<?>> checkedNodesA = new HashSet<PDGNode<?>>();
			final HashSet<PDGNode<?>> checkedNodesB = new HashSet<PDGNode<?>>();

			final ProgramSlicing slicing = new ProgramSlicing();
			final ClonePairInfo clonepair = slicing.getClonePair(nodeA, nodeB,
					checkedNodesA, checkedNodesB);

			if (Configuration.INSTANCE.getS() <= Math.min(clonepair.codecloneA
					.length(), clonepair.codecloneB.length())) {
				pairs.add(clonepair);
			}

			increaseNumberOfPairs();
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
