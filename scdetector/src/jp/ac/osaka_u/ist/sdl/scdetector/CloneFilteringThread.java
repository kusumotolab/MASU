package jp.ac.osaka_u.ist.sdl.scdetector;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import jp.ac.osaka_u.ist.sdl.scdetector.data.ClonePairInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;

public class CloneFilteringThread implements Runnable {

	public CloneFilteringThread(
			final ClonePairInfo[] clonepairarray,
			final Set<ClonePairInfo> clonepairset,
			final ConcurrentMap<ExecutableElementInfo, Set<ClonePairInfo>> clonepairs,
			final AtomicInteger index,
			final Set<ClonePairInfo> refinedClonepairs) {
		this.clonepairarray = clonepairarray;
		this.clonepairset = clonepairset;
		this.clonepairs = clonepairs;
		this.index = index;
		this.refinedClonepairs = refinedClonepairs;
	}

	@Override
	public void run() {

		NEXT: while (true) {

			int i = this.index.getAndIncrement();
			if (!(i < this.clonepairarray.length)) {
				break;
			}

			final ClonePairInfo clonepair = this.clonepairarray[i];

			// matched‚ğ‰Šú‰»
			final Set<ClonePairInfo> matched = new HashSet<ClonePairInfo>(
					this.clonepairset);

			int count = 0;

			// codecloneA‚É‚Â‚¢‚Ä‚Ìˆ—
			for (final ExecutableElementInfo element : clonepair.codecloneA
					.getRealElements()) {
				final Set<ClonePairInfo> obtained = this.clonepairs
						.get(element);
				matched.retainAll(obtained);
				count++;
				if (1 == matched.size()) {
					System.out.println(count);
					this.refinedClonepairs.add(clonepair);
					continue NEXT;
				}
			}

			// codecloneB‚É‚Â‚¢‚Ä‚Ìˆ—
			for (final ExecutableElementInfo element : clonepair.codecloneB
					.getRealElements()) {
				final Set<ClonePairInfo> obtained = this.clonepairs
						.get(element);
				matched.retainAll(obtained);
				count++;
				if (1 == matched.size()) {
					System.out.println(count);
					this.refinedClonepairs.add(clonepair);
					continue NEXT;
				}
			}

			System.out.println("filtered: " + count);
		}
	}

	private final ClonePairInfo[] clonepairarray;

	private final Set<ClonePairInfo> clonepairset;

	private final ConcurrentMap<ExecutableElementInfo, Set<ClonePairInfo>> clonepairs;

	private final AtomicInteger index;

	private final Set<ClonePairInfo> refinedClonepairs;
}
