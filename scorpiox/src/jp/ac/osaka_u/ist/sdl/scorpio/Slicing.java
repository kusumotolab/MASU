package jp.ac.osaka_u.ist.sdl.scorpio;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sdl.scorpio.data.ClonePairInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

abstract public class Slicing {

	protected static ConcurrentMap<PDGNode<?>, Integer> NODE_TO_HASH_MAP = new ConcurrentHashMap<PDGNode<?>, Integer>();
	protected static NodePairCache NODE_PAIR_CACHE = new NodePairCache();

	protected static class NodePairCache {

		final private Map<PDGNode<?>, Set<PDGNode<?>>> nodes;

		NodePairCache() {
			this.nodes = new HashMap<PDGNode<?>, Set<PDGNode<?>>>();
		}

		boolean cached(final PDGNode<?> nodeA, final PDGNode<?> nodeB) {

			final Set<PDGNode<?>> nodes1 = this.nodes.get(nodeA);
			if (null != nodes1 && nodes1.contains(nodeB)) {
				return true;
			}

			final Set<PDGNode<?>> nodes2 = this.nodes.get(nodeB);
			if (null != nodes2 && nodes2.contains(nodeA)) {
				return true;
			}

			return false;
		}

		void add(final PDGNode<?> nodeA, final PDGNode<?> nodeB) {

			Set<PDGNode<?>> nodes = this.nodes.get(nodeA);
			if (null == nodes) {
				nodes = new HashSet<PDGNode<?>>();
				this.nodes.put(nodeA, nodes);
			}
			nodes.add(nodeB);

			nodes = this.nodes.get(nodeB);
			if (null == nodes) {
				nodes = new HashSet<PDGNode<?>>();
				this.nodes.put(nodeB, nodes);
			}
			nodes.add(nodeA);

		}
	}

	abstract ClonePairInfo perform();

	protected SortedSet<PDGNode<?>> getFromNodes(final SortedSet<? extends PDGEdge> edges) {
	
		final SortedSet<PDGNode<?>> fromNodes = new TreeSet<PDGNode<?>>();
	
		for (final PDGEdge edge : edges) {
			fromNodes.add(edge.getFromNode());
		}
	
		return fromNodes;
	}

	protected SortedSet<PDGNode<?>> getToNodes(final SortedSet<? extends PDGEdge> edges) {
	
		final SortedSet<PDGNode<?>> toNodes = new TreeSet<PDGNode<?>>();
	
		for (final PDGEdge edge : edges) {
			toNodes.add(edge.getToNode());
		}
	
		return toNodes;
	}
}
