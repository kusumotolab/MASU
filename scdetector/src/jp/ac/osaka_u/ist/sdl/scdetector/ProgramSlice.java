package jp.ac.osaka_u.ist.sdl.scdetector;

import java.util.HashSet;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sdl.scdetector.data.ClonePairInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.data.CodeCloneInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.Configuration;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.SLICE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGControlDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGDataDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGExecutionDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

/**
 * フォワードスライス，バックワードスライスを行うクラス
 * 
 * @author higo
 * 
 */
class ProgramSlice {

	/**
	 * バックワードスライスを行う
	 * 
	 * @param nodeA
	 *            　たどる元となる頂点A
	 * @param nodeB
	 *            　たどる元となる頂点B
	 * @param clonePair
	 *            現在構築中のクローンペア
	 * @param checkedNodesA
	 *            調査済み頂点群を保存するためのキャッシュA
	 * @param checkedNodesB
	 *            調査済み頂点群を保存するためのキャッシュB
	 */
	static void addDuplicatedElementsWithBackwordSlice(final PDGNode<?> nodeA,
			final PDGNode<?> nodeB, final ClonePairInfo clonePair,
			final HashSet<PDGNode<?>> checkedNodesA,
			final HashSet<PDGNode<?>> checkedNodesB) {

		final SortedSet<PDGEdge> edgesA = nodeA.getBackwardEdges();
		final SortedSet<PDGEdge> edgesB = nodeB.getBackwardEdges();

		final SortedSet<PDGExecutionDependenceEdge> executionEdgesA = PDGExecutionDependenceEdge
				.getExecutionDependenceEdge(edgesA);
		final SortedSet<PDGDataDependenceEdge> dataEdgesA = PDGDataDependenceEdge
				.getDataDependenceEdge(edgesA);
		final SortedSet<PDGControlDependenceEdge> controlEdgesA = PDGControlDependenceEdge
				.getControlDependenceEdge(edgesA);
		final SortedSet<PDGExecutionDependenceEdge> executionEdgesB = PDGExecutionDependenceEdge
				.getExecutionDependenceEdge(edgesB);
		final SortedSet<PDGDataDependenceEdge> dataEdgesB = PDGDataDependenceEdge
				.getDataDependenceEdge(edgesB);
		final SortedSet<PDGControlDependenceEdge> controlEdgesB = PDGControlDependenceEdge
				.getControlDependenceEdge(edgesB);

		backwardSlicing(executionEdgesA, executionEdgesB, clonePair,
				checkedNodesA, checkedNodesB);
		backwardSlicing(dataEdgesA, dataEdgesB, clonePair, checkedNodesA,
				checkedNodesB);
		backwardSlicing(controlEdgesA, controlEdgesB, clonePair, checkedNodesA,
				checkedNodesB);
	}

	/**
	 * フォワードスライスを行う
	 * 
	 * @param nodeA
	 *            　たどる元となる頂点A
	 * @param nodeB
	 *            　たどる元となる頂点B
	 * @param clonePair
	 *            現在構築中のクローンペア
	 * @param checkedNodesA
	 *            調査済み頂点群を保存するためのキャッシュA
	 * @param checkedNodesB
	 *            調査済み頂点群を保存するためのキャッシュB
	 */
	static void addDuplicatedElementsWithForwordSlice(final PDGNode<?> nodeA,
			final PDGNode<?> nodeB, final ClonePairInfo clonePair,
			final HashSet<PDGNode<?>> checkedNodesA,
			final HashSet<PDGNode<?>> checkedNodesB) {

		final SortedSet<PDGEdge> edgesA = nodeA.getForwardEdges();
		final SortedSet<PDGEdge> edgesB = nodeB.getForwardEdges();

		final SortedSet<PDGExecutionDependenceEdge> executionEdgesA = PDGExecutionDependenceEdge
				.getExecutionDependenceEdge(edgesA);
		final SortedSet<PDGDataDependenceEdge> dataEdgesA = PDGDataDependenceEdge
				.getDataDependenceEdge(edgesA);
		final SortedSet<PDGControlDependenceEdge> controlEdgesA = PDGControlDependenceEdge
				.getControlDependenceEdge(edgesA);
		final SortedSet<PDGExecutionDependenceEdge> executionEdgesB = PDGExecutionDependenceEdge
				.getExecutionDependenceEdge(edgesB);
		final SortedSet<PDGDataDependenceEdge> dataEdgesB = PDGDataDependenceEdge
				.getDataDependenceEdge(edgesB);
		final SortedSet<PDGControlDependenceEdge> controlEdgesB = PDGControlDependenceEdge
				.getControlDependenceEdge(edgesB);

		forwardSlicing(executionEdgesA, executionEdgesB, clonePair,
				checkedNodesA, checkedNodesB);
		forwardSlicing(dataEdgesA, dataEdgesB, clonePair, checkedNodesA,
				checkedNodesB);
		forwardSlicing(controlEdgesA, controlEdgesB, clonePair, checkedNodesA,
				checkedNodesB);

	}

	private static void backwardSlicing(
			final SortedSet<? extends PDGEdge> edgesA,
			final SortedSet<? extends PDGEdge> edgesB,
			final ClonePairInfo clonePair,
			final HashSet<PDGNode<?>> checkedNodesA,
			final HashSet<PDGNode<?>> checkedNodesB) {

		for (final PDGEdge edgeA : edgesA) {

			final PDGNode<?> fromNodeA = edgeA.getFromNode();

			if (checkedNodesA.contains(fromNodeA)) {
				continue;
			}

			final ExecutableElementInfo coreA = fromNodeA.getCore();
			final int hashA = Conversion.getNormalizedString(coreA).hashCode();

			for (final PDGEdge edgeB : edgesB) {

				// エッジの種類が違う場合は何もしない
				if (edgeA.getClass() != edgeB.getClass()) {
					continue;
				}

				final PDGNode<?> fromNodeB = edgeB.getFromNode();

				if (checkedNodesB.contains(fromNodeB)) {
					continue;
				}

				final ExecutableElementInfo coreB = fromNodeB.getCore();
				final int hashB = Conversion.getNormalizedString(coreB)
						.hashCode();

				DetectionThread.increaseNumberOfComparison();

				if (hashA == hashB) {

					// A,Bどちらからの要素がすでにクローンペアに含まれている場合は何もしない
					final CodeCloneInfo codecloneA = clonePair.codecloneA;
					final CodeCloneInfo codecloneB = clonePair.codecloneB;
					if (codecloneA.contain(coreA) || codecloneB.contain(coreB)) {
						continue;
					}

					// A,Bどちらがの要素が，相手側のクローンペアに含まれている場合は何もしない
					if (codecloneA.contain(coreB) || codecloneB.contain(coreA)) {
						continue;
					}

					// coreAとcoreBが同じExecutableElementであれば何もしない
					if (coreA == coreB) {
						continue;
					}

					if (fromNodeA instanceof PDGMergedNode) {
						clonePair.codecloneA.addAll(((PDGMergedNode) fromNodeA)
								.getCores());
					} else {
						clonePair.codecloneA.add(fromNodeA.getCore());
					}

					if (fromNodeB instanceof PDGMergedNode) {
						clonePair.codecloneB.addAll(((PDGMergedNode) fromNodeB)
								.getCores());
					} else {
						clonePair.codecloneB.add(fromNodeB.getCore());
					}

					// clonePair.add(coreA, coreB);
					checkedNodesA.add(fromNodeA);
					checkedNodesB.add(fromNodeB);

					if (Configuration.INSTANCE.getT().contains(
							SLICE_TYPE.BACKWARD)) {
						addDuplicatedElementsWithBackwordSlice(fromNodeA,
								fromNodeB, clonePair, checkedNodesA,
								checkedNodesB);
					}
					if (Configuration.INSTANCE.getT().contains(
							SLICE_TYPE.FORWARD)) {
						addDuplicatedElementsWithForwordSlice(fromNodeA,
								fromNodeB, clonePair, checkedNodesA,
								checkedNodesB);
					}
				}
			}
		}
	}

	private static void forwardSlicing(
			final SortedSet<? extends PDGEdge> edgesA,
			final SortedSet<? extends PDGEdge> edgesB,
			final ClonePairInfo clonePair,
			final HashSet<PDGNode<?>> checkedNodesA,
			final HashSet<PDGNode<?>> checkedNodesB) {

		for (final PDGEdge edgeA : edgesA) {

			final PDGNode<?> toNodeA = edgeA.getToNode();

			if (checkedNodesA.contains(toNodeA)) {
				continue;
			}

			final ExecutableElementInfo coreA = toNodeA.getCore();
			final int hashA = Conversion.getNormalizedString(coreA).hashCode();

			for (final PDGEdge edgeB : edgesB) {

				// エッジの種類が違う場合は何もしない
				if (edgeA.getClass() != edgeB.getClass()) {
					continue;
				}

				final PDGNode<?> toNodeB = edgeB.getToNode();

				if (checkedNodesB.contains(toNodeB)) {
					continue;
				}

				final ExecutableElementInfo coreB = toNodeB.getCore();
				final int hashB = Conversion.getNormalizedString(coreB)
						.hashCode();

				DetectionThread.increaseNumberOfComparison();

				if (hashA == hashB) {

					// A,Bどちらからの要素がすでにクローンペアに含まれている場合は何もしない
					final CodeCloneInfo codecloneA = clonePair.codecloneA;
					final CodeCloneInfo codecloneB = clonePair.codecloneB;
					if (codecloneA.contain(coreA) || codecloneB.contain(coreB)) {
						continue;
					}

					// A,Bどちらがの要素が，相手側のクローンペアに含まれている場合は何もしない
					if (codecloneA.contain(coreB) || codecloneB.contain(coreA)) {
						continue;
					}

					// coreAとcoreBが同じExecutableElementであれば何もしない
					if (coreA == coreB) {
						continue;
					}

					if (toNodeA instanceof PDGMergedNode) {
						clonePair.codecloneA.addAll(((PDGMergedNode) toNodeA)
								.getCores());
					} else {
						clonePair.codecloneA.add(toNodeA.getCore());
					}

					if (toNodeB instanceof PDGMergedNode) {
						clonePair.codecloneB.addAll(((PDGMergedNode) toNodeB)
								.getCores());
					} else {
						clonePair.codecloneB.add(toNodeB.getCore());
					}

					// clonePair.add(coreA, coreB);
					checkedNodesA.add(toNodeA);
					checkedNodesB.add(toNodeB);

					if (Configuration.INSTANCE.getT().contains(
							SLICE_TYPE.BACKWARD)) {
						addDuplicatedElementsWithBackwordSlice(toNodeA,
								toNodeB, clonePair, checkedNodesA,
								checkedNodesB);
					}
					if (Configuration.INSTANCE.getT().contains(
							SLICE_TYPE.FORWARD)) {
						addDuplicatedElementsWithForwordSlice(toNodeA, toNodeB,
								clonePair, checkedNodesA, checkedNodesB);
					}
				}
			}
		}
	}
}
