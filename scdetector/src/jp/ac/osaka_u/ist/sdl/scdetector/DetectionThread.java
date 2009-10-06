package jp.ac.osaka_u.ist.sdl.scdetector;

import java.util.Collections;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sdl.scdetector.data.ClonePairInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.data.NodePairInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.data.NodePairListInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.Configuration;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.SLICE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

class DetectionThread implements Runnable {

	private final NodePairListInfo nodePairList;
	private final int id;
	private final ConcurrentMap<TwoClassHash, SortedSet<ClonePairInfo>> clonePairs;

	DetectionThread(
			final int id,
			final NodePairListInfo nodePairList,
			final ConcurrentMap<TwoClassHash, SortedSet<ClonePairInfo>> clonePairs) {
		this.id = id;
		this.nodePairList = nodePairList;
		this.clonePairs = clonePairs;
	}

	@Override
	public void run() {

		while (nodePairList.hasNext()) {

			final NodePairInfo nodePair = this.nodePairList.next();

			//System.out.println(this.id);

			final PDGNode<?> nodeA = nodePair.getNodeA();
			final PDGNode<?> nodeB = nodePair.getNodeB();
			final ExecutableElementInfo elementA = nodeA.getCore();
			final ExecutableElementInfo elementB = nodeB.getCore();
			final ClonePairInfo clonePair = new ClonePairInfo(elementA,
					elementB);

			final HashSet<PDGNode<?>> checkedNodesA = new HashSet<PDGNode<?>>();
			final HashSet<PDGNode<?>> checkedNodesB = new HashSet<PDGNode<?>>();
			checkedNodesA.add(nodeA);
			checkedNodesB.add(nodeB);

			// increaseNumberOfPairs();

			if (Configuration.INSTANCE.getT().contains(SLICE_TYPE.BACKWARD)) {
				ProgramSlice.addDuplicatedElementsWithBackwordSlice(nodeA,
						nodeB, clonePair, checkedNodesA, checkedNodesB);
			}
			if (Configuration.INSTANCE.getT().contains(SLICE_TYPE.FORWARD)) {
				ProgramSlice.addDuplicatedElementsWithForwordSlice(nodeA,
						nodeB, clonePair, checkedNodesA, checkedNodesB);
			}

			if (Configuration.INSTANCE.getS() <= clonePair.length()) {
				SortedSet<ClonePairInfo> pairs = this.clonePairs
						.get(new TwoClassHash(clonePair));
				if (null == pairs) {
					pairs = Collections
							.<ClonePairInfo> synchronizedSortedSet(new TreeSet<ClonePairInfo>());
					this.clonePairs.put(new TwoClassHash(clonePair), pairs);
				}
				pairs.add(clonePair);
			}

		}
	}

	synchronized static void increaseNumberOfPairs() {
		numberOfPairs++;
	}

	synchronized static void increaseNumberOfComparison() {
		numberOfComparion++;
	}

	static int numberOfPairs;

	static long numberOfComparion;
}
