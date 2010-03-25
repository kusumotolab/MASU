package jp.ac.osaka_u.ist.sdl.scdetector.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import jp.ac.osaka_u.ist.sdl.scdetector.settings.Configuration;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGMethodEnterNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

public class NodePairListInfo {

	private final List<NodePairInfo> nodePairList;

	private AtomicInteger index;

	public NodePairListInfo(final Collection<List<PDGNode<?>>> nodeListSet) {
		this.index = new AtomicInteger(0);
		this.nodePairList = new ArrayList<NodePairInfo>();
		for (final List<PDGNode<?>> nodeList : nodeListSet) {

			// メソッド入口ノードの場合は読み飛ばす
			if (nodeList.get(0) instanceof PDGMethodEnterNode) {
				continue;
			}

			// 閾値以上一致するノードがある場合は読み飛ばす
			if (Configuration.INSTANCE.getC() <= nodeList.size()) {
				continue;
			}

			// コントロールノードでない場合は飛ばす
			if (Configuration.INSTANCE.getU().useControlFilter()
					&& !(nodeList.get(0) instanceof PDGControlNode)) {
				continue;
			}

			for (int i = 0; i < nodeList.size(); i++) {
				for (int j = i + 1; j < nodeList.size(); j++) {
					this.nodePairList.add(new NodePairInfo(nodeList.get(i),
							nodeList.get(j)));
				}
			}
		}
	}

	public boolean hasNext() {
		return this.index.get() < this.nodePairList.size();
	}

	public NodePairInfo next() {
		return this.nodePairList.get(this.index.getAndIncrement());
	}
}
