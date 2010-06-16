package jp.ac.osaka_u.ist.sdl.scdetector;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sdl.scdetector.data.ClonePairInfo;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.Configuration;
import jp.ac.osaka_u.ist.sdl.scdetector.settings.SLICE_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGControlDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGDataDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGExecutionDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

public class ProgramSlicing {

	public ClonePairInfo getClonePair(final PDGNode<?> nodeA,
			final PDGNode<?> nodeB, final Set<PDGNode<?>> checkedNodesA,
			final Set<PDGNode<?>> checkedNodesB) {

		// ���̃m�[�h���`�F�b�N�ς݃m�[�h�W���ɒǉ��C���̏����͍ċA�Ăяo���̑O�łȂ���΂Ȃ�Ȃ�
		checkedNodesA.add(nodeA);
		checkedNodesB.add(nodeB);

		// ��������C�e�G�b�W�̐�ɂ���m�[�h�̏W���𓾂邽�߂̏���
		final SortedSet<PDGEdge> backwardEdgesA = nodeA.getBackwardEdges();
		final SortedSet<PDGEdge> backwardEdgesB = nodeB.getBackwardEdges();
		final SortedSet<PDGEdge> forwardEdgesA = nodeA.getForwardEdges();
		final SortedSet<PDGEdge> forwardEdgesB = nodeB.getForwardEdges();

		final SortedSet<PDGExecutionDependenceEdge> backwardExecutionEdgesA = PDGExecutionDependenceEdge
				.getExecutionDependenceEdge(backwardEdgesA);
		final SortedSet<PDGDataDependenceEdge> backwardDataEdgesA = PDGDataDependenceEdge
				.getDataDependenceEdge(backwardEdgesA);
		final SortedSet<PDGControlDependenceEdge> backwardControlEdgesA = PDGControlDependenceEdge
				.getControlDependenceEdge(backwardEdgesA);
		final SortedSet<PDGExecutionDependenceEdge> backwardExecutionEdgesB = PDGExecutionDependenceEdge
				.getExecutionDependenceEdge(backwardEdgesB);
		final SortedSet<PDGDataDependenceEdge> backwardDataEdgesB = PDGDataDependenceEdge
				.getDataDependenceEdge(backwardEdgesB);
		final SortedSet<PDGControlDependenceEdge> backwardControlEdgesB = PDGControlDependenceEdge
				.getControlDependenceEdge(backwardEdgesB);

		final SortedSet<PDGNode<?>> backwardExecutionNodesA = getFromNodes(backwardExecutionEdgesA);
		final SortedSet<PDGNode<?>> backwardDataNodesA = getFromNodes(backwardDataEdgesA);
		final SortedSet<PDGNode<?>> backwardControlNodesA = getFromNodes(backwardControlEdgesA);
		final SortedSet<PDGNode<?>> backwardExecutionNodesB = getFromNodes(backwardExecutionEdgesB);
		final SortedSet<PDGNode<?>> backwardDataNodesB = getFromNodes(backwardDataEdgesB);
		final SortedSet<PDGNode<?>> backwardControlNodesB = getFromNodes(backwardControlEdgesB);

		final SortedSet<PDGExecutionDependenceEdge> forwardExecutionEdgesA = PDGExecutionDependenceEdge
				.getExecutionDependenceEdge(forwardEdgesA);
		final SortedSet<PDGDataDependenceEdge> forwardDataEdgesA = PDGDataDependenceEdge
				.getDataDependenceEdge(forwardEdgesA);
		final SortedSet<PDGControlDependenceEdge> forwardControlEdgesA = PDGControlDependenceEdge
				.getControlDependenceEdge(forwardEdgesA);
		final SortedSet<PDGExecutionDependenceEdge> forwardExecutionEdgesB = PDGExecutionDependenceEdge
				.getExecutionDependenceEdge(forwardEdgesB);
		final SortedSet<PDGDataDependenceEdge> forwardDataEdgesB = PDGDataDependenceEdge
				.getDataDependenceEdge(forwardEdgesB);
		final SortedSet<PDGControlDependenceEdge> forwardControlEdgesB = PDGControlDependenceEdge
				.getControlDependenceEdge(forwardEdgesB);

		final SortedSet<PDGNode<?>> forwardExecutionNodesA = getToNodes(forwardExecutionEdgesA);
		final SortedSet<PDGNode<?>> forwardDataNodesA = getToNodes(forwardDataEdgesA);
		final SortedSet<PDGNode<?>> forwardControlNodesA = getToNodes(forwardControlEdgesA);
		final SortedSet<PDGNode<?>> forwardExecutionNodesB = getToNodes(forwardExecutionEdgesB);
		final SortedSet<PDGNode<?>> forwardDataNodesB = getToNodes(forwardDataEdgesB);
		final SortedSet<PDGNode<?>> forwardControlNodesB = getToNodes(forwardControlEdgesB);

		final ClonePairInfo clonepair = new ClonePairInfo();

		// �e�m�[�h�̏W���ɑ΂��Ă��̐�ɂ���N���[���y�A�̍\�z
		// �o�b�N���[�h�X���C�X���g���ݒ�̏ꍇ
		if (Configuration.INSTANCE.getT().contains(SLICE_TYPE.BACKWARD)) {
			this.enlargeClonePair(clonepair, backwardExecutionNodesA,
					backwardExecutionNodesB, checkedNodesA, checkedNodesB);
			this.enlargeClonePair(clonepair, backwardDataNodesA,
					backwardDataNodesB, checkedNodesA, checkedNodesB);
			this.enlargeClonePair(clonepair, backwardControlNodesA,
					backwardControlNodesB, checkedNodesA, checkedNodesB);
		}

		// �t�H���[�h�X���C�X���g���ݒ�̏ꍇ
		if (Configuration.INSTANCE.getT().contains(SLICE_TYPE.FORWARD)) {
			this.enlargeClonePair(clonepair, forwardExecutionNodesA,
					forwardExecutionNodesB, checkedNodesA, checkedNodesB);
			this.enlargeClonePair(clonepair, forwardDataNodesA,
					forwardDataNodesB, checkedNodesA, checkedNodesB);
			this.enlargeClonePair(clonepair, forwardControlNodesA,
					forwardControlNodesB, checkedNodesA, checkedNodesB);
		}

		// ���݂̃m�[�h���N���[���y�A�ɒǉ�
		if (nodeA instanceof PDGMergedNode) {
			clonepair.codecloneA.addAll(((PDGMergedNode) nodeA).getCores());
		} else {
			clonepair.codecloneA.add(nodeA.getCore());
		}

		if (nodeB instanceof PDGMergedNode) {
			clonepair.codecloneB.addAll(((PDGMergedNode) nodeB).getCores());
		} else {
			clonepair.codecloneB.add(nodeB.getCore());
		}

		return clonepair;
	}

	private void enlargeClonePair(final ClonePairInfo clonepair,
			final SortedSet<PDGNode<?>> nodesA,
			final SortedSet<PDGNode<?>> nodesB,
			final Set<PDGNode<?>> checkedNodesA,
			final Set<PDGNode<?>> checkedNodesB) {

		for (final PDGNode<?> nodeA : nodesA) {

			// ���ɃN���[���ɓ��邱�Ƃ��m�肵�Ă���m�[�h�̂Ƃ��͒������Ȃ�
			// ���葤�̃N���[���ɓ����Ă���m�[�h�̂Ƃ����������Ȃ�
			if (checkedNodesA.contains(nodeA) || checkedNodesB.contains(nodeA)) {
				continue;
			}

			// �m�[�h�̃n�b�V���l�𓾂�
			Integer hashA = NODE_TO_HASH_MAP.get(nodeA);
			if (null == hashA) {
				final ExecutableElementInfo coreA = nodeA.getCore();
				hashA = Conversion.getNormalizedString(coreA).hashCode();
				NODE_TO_HASH_MAP.put(nodeA, hashA);
			}

			for (final PDGNode<?> nodeB : nodesB) {

				// ���ɃN���[���ɓ��邱�Ƃ��m�肵�Ă���m�[�h�̂Ƃ��͒������Ȃ�
				// ���葤�̃N���[���ɓ����Ă���m�[�h�̂Ƃ����������Ȃ�
				if (checkedNodesB.contains(nodeB)
						|| checkedNodesA.contains(nodeB)) {
					continue;
				}

				// �m�[�h�̃n�b�V���l�𓾂�
				Integer hashB = NODE_TO_HASH_MAP.get(nodeB);
				if (null == hashB) {
					final ExecutableElementInfo coreB = nodeB.getCore();
					hashB = Conversion.getNormalizedString(coreB).hashCode();
					NODE_TO_HASH_MAP.put(nodeB, hashB);
				}

				SlicingThread.increaseNumberOfComparison();

				// �m�[�h�̃n�b�V���l���������ꍇ�́C���̃m�[�h�y�A�̐������ɒ���
				if (hashA.equals(hashB)) {

					// �m�[�h�������ꍇ�͒������Ȃ�
					if (nodeA == nodeB) {
						continue;
					}

					final ClonePairInfo priorClonepair = this.getClonePair(
							nodeA, nodeB, checkedNodesA, checkedNodesB);
					clonepair.addAll(priorClonepair.codecloneA.getElements(),
							priorClonepair.codecloneB.getElements());
				}
			}
		}
	}

	private SortedSet<PDGNode<?>> getFromNodes(
			final SortedSet<? extends PDGEdge> edges) {

		final SortedSet<PDGNode<?>> fromNodes = new TreeSet<PDGNode<?>>();

		for (final PDGEdge edge : edges) {
			fromNodes.add(edge.getFromNode());
		}

		return fromNodes;
	}

	private SortedSet<PDGNode<?>> getToNodes(
			final SortedSet<? extends PDGEdge> edges) {

		final SortedSet<PDGNode<?>> toNodes = new TreeSet<PDGNode<?>>();

		for (final PDGEdge edge : edges) {
			toNodes.add(edge.getToNode());
		}

		return toNodes;
	}

	private static ConcurrentMap<PDGNode<?>, Integer> NODE_TO_HASH_MAP = new ConcurrentHashMap<PDGNode<?>, Integer>();
}