package jp.ac.osaka_u.ist.sdl.scdetector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import jp.ac.osaka_u.ist.sdl.scdetector.data.ClonePairInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.data.NodePairInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.Configuration;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

class SlicingThread implements Runnable {

	private final List<NodePairInfo> nodepairs;

	private final AtomicInteger index;

	private final ConcurrentMap<ExecutableElementInfo, List<ClonePairInfo>> clonepairs;

	SlicingThread(
			final List<NodePairInfo> nodepairs,
			final AtomicInteger index,
			final ConcurrentMap<ExecutableElementInfo, List<ClonePairInfo>> clonepairs) {

		this.nodepairs = nodepairs;
		this.index = index;
		this.clonepairs = clonepairs;
	}

	@Override
	public void run() {

		while (true) {

			int i = this.index.getAndIncrement();
			if (!(i < this.nodepairs.size())) {
				break;
			}

			final NodePairInfo nodepair = this.nodepairs.get(i);

			final PDGNode<?> nodeA = nodepair.nodeA;
			final PDGNode<?> nodeB = nodepair.nodeB;

			final Slicing slicing;
			switch (Configuration.INSTANCE.getP()) {
			case INTRA:
				slicing = new ProgramSlicing(nodeA, nodeB);
				break;
			case INTER:
				slicing = new SystemSlicing(nodeA, nodeB);
				break;
			default:
				throw new IllegalStateException();
			}
			final ClonePairInfo clonepair = slicing.perform();

			if (Configuration.INSTANCE.getS() <= Math.min(clonepair.codecloneA
					.length(), clonepair.codecloneB.length())) {
				this.addClonepair(clonepair);
			}

			increaseNumberOfPairs();
		}
	}

	private void addClonepair(final ClonePairInfo clonepair) {

		for (final ExecutableElementInfo element : clonepair.codecloneA
				.getRealElements()) {
			List<ClonePairInfo> list = this.clonepairs.get(element);
			if (null == list) {
				list = Collections
						.synchronizedList(new ArrayList<ClonePairInfo>());
				this.clonepairs.put(element, list);
			}
			final boolean added = list.add(clonepair);
		}

		for (final ExecutableElementInfo element : clonepair.codecloneB
				.getRealElements()) {
			List<ClonePairInfo> list = this.clonepairs.get(element);
			if (null == list) {
				list = Collections
						.synchronizedList(new ArrayList<ClonePairInfo>());
				this.clonepairs.put(element, list);
			}
			final boolean added = list.add(clonepair);
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
