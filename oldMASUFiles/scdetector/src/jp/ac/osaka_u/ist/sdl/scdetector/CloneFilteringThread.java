package jp.ac.osaka_u.ist.sdl.scdetector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import jp.ac.osaka_u.ist.sdl.scdetector.data.ClonePairInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

public class CloneFilteringThread implements Runnable {

	public CloneFilteringThread(
			final List<ClonePairInfo> clonepairList,
			final ConcurrentMap<PDGNode<?>, List<ClonePairInfo>> clonepairListGroup,
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

			// �t�B���^�����O���邩�ǂ����𒲍����邽�߂̑ΏۃN���[���y�A���X�g���擾
			final List<List<ClonePairInfo>> candidateList = new ArrayList<List<ClonePairInfo>>();
			for (final PDGNode<?> node : clonepair.codecloneA.getRealElements()) {
				final List<ClonePairInfo> candidate = this.clonepairListGroup
						.get(node);
				candidateList.add(candidate);
			}
			for (final PDGNode<?> node : clonepair.codecloneB.getRealElements()) {
				final List<ClonePairInfo> candidate = this.clonepairListGroup
						.get(node);
				candidateList.add(candidate);
			}
			List<ClonePairInfo> target = candidateList.get(0);
			for (int index = 1; index < candidateList.size(); index++) {
				final List<ClonePairInfo> candidate = candidateList.get(index);
				if (candidate.size() < target.size()) {
					target = candidate;
				}
			}

			// �ΏۃN���[���y�A���X�g�ɂ����āCclonepair���܂��Ă�����̂����邩�𒲍�
			for (final ClonePairInfo another : target) {
				if (clonepair.subsumedBy(another)) {
					continue NEXT;
				}
			}

			this.refinedClonepairs.add(clonepair);
		}
	}

	private final List<ClonePairInfo> clonepairList;

	private final ConcurrentMap<PDGNode<?>, List<ClonePairInfo>> clonepairListGroup;

	private final AtomicInteger index;

	private final Set<ClonePairInfo> refinedClonepairs;
}
