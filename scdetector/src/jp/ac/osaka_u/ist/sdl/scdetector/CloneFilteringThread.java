package jp.ac.osaka_u.ist.sdl.scdetector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import jp.ac.osaka_u.ist.sdl.scdetector.data.ClonePairInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;

public class CloneFilteringThread implements Runnable {

	public CloneFilteringThread(
			final List<ClonePairInfo> clonepairList,
			final ConcurrentMap<ExecutableElementInfo, List<ClonePairInfo>> clonepairListGroup,
			final AtomicInteger index,
			final Set<ClonePairInfo> refinedClonepairs) {
		this.clonepairList = clonepairList;
		this.clonepairListGroup = clonepairListGroup;
		this.index = index;
		this.refinedClonepairs = refinedClonepairs;
	}

	@Override
	public void run() {

		NEXT: while (true) {

			int i = this.index.getAndIncrement();
			if (!(i < this.clonepairList.size())) {
				break;
			}

			final ClonePairInfo clonepair = this.clonepairList.get(i);

			// フィルタリングするかどうかを調査するための対象クローンペアリストを取得
			final List<List<ClonePairInfo>> candidateList = new ArrayList<List<ClonePairInfo>>();
			for (final ExecutableElementInfo element : clonepair.codecloneA
					.getRealElements()) {
				final List<ClonePairInfo> candidate = this.clonepairListGroup
						.get(element);
				candidateList.add(candidate);
			}
			for (final ExecutableElementInfo element : clonepair.codecloneA
					.getRealElements()) {
				final List<ClonePairInfo> candidate = this.clonepairListGroup
						.get(element);
				candidateList.add(candidate);
			}
			List<ClonePairInfo> target = candidateList.get(0);
			for (int index = 1; index < candidateList.size(); index++) {
				final List<ClonePairInfo> candidate = candidateList.get(index);
				if (candidate.size() < target.size()) {
					target = candidate;
				}
			}

			// 対象クローンペアリストにおいて，clonepairを包含しているものがあるかを調査
			for (final ClonePairInfo another : target) {
				if (clonepair.subsumedBy(another)) {
					continue NEXT;
				}
			}

			this.refinedClonepairs.add(clonepair);
		}
	}

	private final List<ClonePairInfo> clonepairList;

	private final ConcurrentMap<ExecutableElementInfo, List<ClonePairInfo>> clonepairListGroup;

	private final AtomicInteger index;

	private final Set<ClonePairInfo> refinedClonepairs;
}
