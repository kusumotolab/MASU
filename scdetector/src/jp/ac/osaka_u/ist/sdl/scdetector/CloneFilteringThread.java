package jp.ac.osaka_u.ist.sdl.scdetector;

import java.util.SortedSet;

import jp.ac.osaka_u.ist.sdl.scdetector.data.ClonePairInfo;

public class CloneFilteringThread implements Runnable {

	public CloneFilteringThread(final SortedSet<ClonePairInfo> clonepairs,
			final SortedSet<ClonePairInfo> refinedClonepairs) {
		this.clonepairs = clonepairs;
		this.refinedClonepairs = refinedClonepairs;
	}

	@Override
	public void run() {
		CLONEPAIR: for (final ClonePairInfo clonePair : this.clonepairs) {

			// 他のクローンペアに内包されるクローンペアを除去する
			COUNTERCLONEPAIR: for (final ClonePairInfo counterClonePair : this.clonepairs) {

				if (clonePair.subsumedBy(counterClonePair)) {
					continue CLONEPAIR;
				}

				if (clonePair == counterClonePair) {
					continue COUNTERCLONEPAIR;
				}
			}

			this.refinedClonepairs.add(clonePair);
		}
	}

	private final SortedSet<ClonePairInfo> clonepairs;

	private final SortedSet<ClonePairInfo> refinedClonepairs;
}
