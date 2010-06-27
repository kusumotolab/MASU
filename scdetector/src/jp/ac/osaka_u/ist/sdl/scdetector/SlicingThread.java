package jp.ac.osaka_u.ist.sdl.scdetector;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

import jp.ac.osaka_u.ist.sdl.scdetector.data.ClonePairInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.data.NodePairInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.Configuration;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

class SlicingThread implements Runnable {

	private final List<NodePairInfo> nodepairs;

	private final AtomicInteger index;

	private final Map<TwoClassHash, SortedSet<ClonePairInfo>> clonePairs;

	SlicingThread(final List<NodePairInfo> nodepairs,
			final AtomicInteger index,
			final Map<TwoClassHash, SortedSet<ClonePairInfo>> clonePairs) {

		this.nodepairs = nodepairs;
		this.index = index;
		this.clonePairs = clonePairs;
	}

	@Override
	public void run() {

		final Set<ClonePairInfo> pairs = new HashSet<ClonePairInfo>();

		while (true) {

			int i = this.index.getAndIncrement();
			if (!(i < this.nodepairs.size())) {
				break;
			}

			final NodePairInfo nodepair = this.nodepairs.get(i);

			final PDGNode<?> nodeA = nodepair.nodeA;
			final PDGNode<?> nodeB = nodepair.nodeB;
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
