package jp.ac.osaka_u.ist.sdl.scdetector;

import java.util.HashSet;
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
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGDataNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

public class ProgramSlicing extends Slicing {

	final private PDGNode<?> pointA;
	final private PDGNode<?> pointB;

	final private Set<PDGNode<?>> checkedNodesA;
	final private Set<PDGNode<?>> checkedNodesB;

	private ClonePairInfo clonepair;

	public ProgramSlicing(final PDGNode<?> pointA, final PDGNode<?> pointB) {
		this.pointA = pointA;
		this.pointB = pointB;
		this.checkedNodesA = new HashSet<PDGNode<?>>();
		this.checkedNodesB = new HashSet<PDGNode<?>>();
		this.clonepair = null;
	}

	public ClonePairInfo perform() {
		if (null == this.clonepair) {
			this.clonepair = new ClonePairInfo();
			this.perform(this.pointA, this.pointB);
		}
		return this.clonepair;
	}

	private void perform(final PDGNode<?> nodeA, final PDGNode<?> nodeB) {

		// ���̃m�[�h���`�F�b�N�ς݃m�[�h�W���ɒǉ��C���̏����͍ċA�Ăяo���̑O�łȂ���΂Ȃ�Ȃ�
		this.checkedNodesA.add(nodeA);
		this.checkedNodesB.add(nodeB);

		// ��������C�e�G�b�W�̐�ɂ���m�[�h�̏W���𓾂邽�߂̏���
		final SortedSet<PDGEdge> backwardEdgesA = nodeA.getBackwardEdges();
		final SortedSet<PDGEdge> backwardEdgesB = nodeB.getBackwardEdges();
		final SortedSet<PDGEdge> forwardEdgesA = nodeA.getForwardEdges();
		final SortedSet<PDGEdge> forwardEdgesB = nodeB.getForwardEdges();

		final SortedSet<PDGExecutionDependenceEdge> backwardExecutionEdgesA = PDGExecutionDependenceEdge
				.extractExecutionDependenceEdge(backwardEdgesA);
		final SortedSet<PDGDataDependenceEdge> backwardDataEdgesA = PDGDataDependenceEdge
				.extractDataDependenceEdge(backwardEdgesA);
		final SortedSet<PDGControlDependenceEdge> backwardControlEdgesA = PDGControlDependenceEdge
				.extractControlDependenceEdge(backwardEdgesA);
		final SortedSet<PDGExecutionDependenceEdge> backwardExecutionEdgesB = PDGExecutionDependenceEdge
				.extractExecutionDependenceEdge(backwardEdgesB);
		final SortedSet<PDGDataDependenceEdge> backwardDataEdgesB = PDGDataDependenceEdge
				.extractDataDependenceEdge(backwardEdgesB);
		final SortedSet<PDGControlDependenceEdge> backwardControlEdgesB = PDGControlDependenceEdge
				.extractControlDependenceEdge(backwardEdgesB);

		final SortedSet<PDGNode<?>> backwardExecutionNodesA = this
				.getFromNodes(backwardExecutionEdgesA);
		final SortedSet<PDGNode<?>> backwardDataNodesA = this
				.getFromNodes(backwardDataEdgesA);
		final SortedSet<PDGNode<?>> backwardControlNodesA = this
				.getFromNodes(backwardControlEdgesA);
		final SortedSet<PDGNode<?>> backwardExecutionNodesB = this
				.getFromNodes(backwardExecutionEdgesB);
		final SortedSet<PDGNode<?>> backwardDataNodesB = this
				.getFromNodes(backwardDataEdgesB);
		final SortedSet<PDGNode<?>> backwardControlNodesB = this
				.getFromNodes(backwardControlEdgesB);

		final SortedSet<PDGExecutionDependenceEdge> forwardExecutionEdgesA = PDGExecutionDependenceEdge
				.extractExecutionDependenceEdge(forwardEdgesA);
		final SortedSet<PDGDataDependenceEdge> forwardDataEdgesA = PDGDataDependenceEdge
				.extractDataDependenceEdge(forwardEdgesA);
		final SortedSet<PDGControlDependenceEdge> forwardControlEdgesA = PDGControlDependenceEdge
				.extractControlDependenceEdge(forwardEdgesA);
		final SortedSet<PDGExecutionDependenceEdge> forwardExecutionEdgesB = PDGExecutionDependenceEdge
				.extractExecutionDependenceEdge(forwardEdgesB);
		final SortedSet<PDGDataDependenceEdge> forwardDataEdgesB = PDGDataDependenceEdge
				.extractDataDependenceEdge(forwardEdgesB);
		final SortedSet<PDGControlDependenceEdge> forwardControlEdgesB = PDGControlDependenceEdge
				.extractControlDependenceEdge(forwardEdgesB);

		final SortedSet<PDGNode<?>> forwardExecutionNodesA = this
				.getToNodes(forwardExecutionEdgesA);
		final SortedSet<PDGNode<?>> forwardDataNodesA = this
				.getToNodes(forwardDataEdgesA);
		final SortedSet<PDGNode<?>> forwardControlNodesA = this
				.getToNodes(forwardControlEdgesA);
		final SortedSet<PDGNode<?>> forwardExecutionNodesB = this
				.getToNodes(forwardExecutionEdgesB);
		final SortedSet<PDGNode<?>> forwardDataNodesB = this
				.getToNodes(forwardDataEdgesB);
		final SortedSet<PDGNode<?>> forwardControlNodesB = this
				.getToNodes(forwardControlEdgesB);

		// �e�m�[�h�̏W���ɑ΂��Ă��̐�ɂ���N���[���y�A�̍\�z
		// �o�b�N���[�h�X���C�X���g���ݒ�̏ꍇ
		if (Configuration.INSTANCE.getT().contains(SLICE_TYPE.BACKWARD)) {
			this.enlargeClonePair(backwardExecutionNodesA,
					backwardExecutionNodesB);
			this.enlargeClonePair(backwardDataNodesA, backwardDataNodesB);
			this.enlargeClonePair(backwardControlNodesA, backwardControlNodesB);
		}

		// �t�H���[�h�X���C�X���g���ݒ�̏ꍇ
		if (Configuration.INSTANCE.getT().contains(SLICE_TYPE.FORWARD)) {
			this.enlargeClonePair(forwardExecutionNodesA,
					forwardExecutionNodesB);
			this.enlargeClonePair(forwardDataNodesA, forwardDataNodesB);
			this.enlargeClonePair(forwardControlNodesA, forwardControlNodesB);
		}

		// ���݂̃m�[�h���N���[���y�A�ɒǉ�
		this.clonepair.add(nodeA, nodeB);
	}

	private void enlargeClonePair(final SortedSet<PDGNode<?>> nodesA,
			final SortedSet<PDGNode<?>> nodesB) {

		NODEA: for (final PDGNode<?> nodeA : nodesA) {

			// ���ɃN���[���ɓ��邱�Ƃ��m�肵�Ă���m�[�h�̂Ƃ��͒������Ȃ�
			// ���葤�̃N���[���ɓ����Ă���m�[�h�̂Ƃ����������Ȃ�
			if (this.checkedNodesA.contains(nodeA)
					|| this.checkedNodesB.contains(nodeA)) {
				continue NODEA;
			}

			// �f�[�^�m�[�h�̎��͒������Ȃ�
			if (nodeA instanceof PDGDataNode<?>) {
				continue NODEA;
			}

			NODEB: for (final PDGNode<?> nodeB : nodesB) {

				// ���ɃN���[���ɓ��邱�Ƃ��m�肵�Ă���m�[�h�̂Ƃ��͒������Ȃ�
				// ���葤�̃N���[���ɓ����Ă���m�[�h�̂Ƃ����������Ȃ�
				// �����ł����̏����͕K�v�I
				if (this.checkedNodesA.contains(nodeA)
						|| this.checkedNodesB.contains(nodeA)) {
					continue NODEA;
				}

				// ���ɃN���[���ɓ��邱�Ƃ��m�肵�Ă���m�[�h�̂Ƃ��͒������Ȃ�
				// ���葤�̃N���[���ɓ����Ă���m�[�h�̂Ƃ����������Ȃ�
				if (this.checkedNodesB.contains(nodeB)
						|| this.checkedNodesA.contains(nodeB)) {
					continue NODEB;
				}

				// �f�[�^�m�[�h�̎��͒������Ȃ�
				if (nodeB instanceof PDGDataNode<?>) {
					continue NODEB;
				}

				// �m�[�hA�̃n�b�V���l�𓾂�
				Integer hashA = NODE_TO_HASH_MAP.get(nodeA);
				if (null == hashA) {
					final ExecutableElementInfo coreA = nodeA.getCore();
					hashA = Conversion.getNormalizedElement(coreA).hashCode();
					NODE_TO_HASH_MAP.put(nodeA, hashA);
				}

				// �m�[�hB�̃n�b�V���l�𓾂�
				Integer hashB = NODE_TO_HASH_MAP.get(nodeB);
				if (null == hashB) {
					final ExecutableElementInfo coreB = nodeB.getCore();
					hashB = Conversion.getNormalizedElement(coreB).hashCode();
					NODE_TO_HASH_MAP.put(nodeB, hashB);
				}

				SlicingThread.increaseNumberOfComparison();

				// �m�[�h�̃n�b�V���l���������ꍇ�́C���̃m�[�h�y�A�̐������ɒ���
				if (hashA.equals(hashB)) {

					// �m�[�h�������ꍇ�͒������Ȃ�
					if (nodeA == nodeB) {
						continue;
					}

					this.perform(nodeA, nodeB);
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
