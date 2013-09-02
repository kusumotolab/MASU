package jp.ac.osaka_u.ist.sdl.scorpio;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.Stack;

import jp.ac.osaka_u.ist.sdl.scorpio.data.ClonePairInfo;
import jp.ac.osaka_u.ist.sdl.scorpio.data.NodePairInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGAcrossEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGControlDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGDataDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGExecutionDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

public class SystemSlicing extends Slicing {

	final private PDGNode<?> pointA;
	final private PDGNode<?> pointB;

	final private Stack<CallInfo<?>> callStackA;
	final private Stack<CallInfo<?>> callStackB;

	private ClonePairInfo clonepair;

	public SystemSlicing(final PDGNode<?> pointA, final PDGNode<?> pointB) {
		this.pointA = pointA;
		this.pointB = pointB;
		this.callStackA = new Stack<CallInfo<?>>();
		this.callStackB = new Stack<CallInfo<?>>();
		this.clonepair = null;
	}

	public ClonePairInfo perform() {
		if (null == this.clonepair) {
			final Set<PDGNode<?>> predecessorsA = new HashSet<PDGNode<?>>();
			final Set<PDGNode<?>> predecessorsB = new HashSet<PDGNode<?>>();
			this.clonepair = this.perform(this.pointA, this.pointB,
					predecessorsA, predecessorsB);

			// ���ʕ�������菜������
			// final SortedSet<PDGNode<?>> commonNodes = new
			// TreeSet<PDGNode<?>>();
			// commonNodes.addAll(this.clonepair.codecloneA.getAllElements());
			// commonNodes.retainAll(this.clonepair.codecloneB.getAllElements());
			// this.clonepair.codecloneA.removeAll(commonNodes);
			// this.clonepair.codecloneB.removeAll(commonNodes);
		}
		return this.clonepair;
	}

	public ClonePairInfo perform(final PDGNode<?> nodeA,
			final PDGNode<?> nodeB, final Set<PDGNode<?>> predecessorsA,
			final Set<PDGNode<?>> predecessorsB) {

		// ���łɗ��p���ꂽ�m�[�h�̃y�A���ǂ������`�F�b�N
		final NodePairInfo nodepair = NodePairInfo.getInstance(nodeA, nodeB);
		if (NODE_PAIR_CACHE.contains(nodepair)) {
			return new ClonePairInfo();
		}

		// ���̃m�[�h��O�C�҃Z�b�g�ɒǉ��C���̏����͍ċA�Ăяo���̑O�łȂ���΂Ȃ�Ȃ�
		predecessorsA.add(nodeA);
		predecessorsB.add(nodeB);

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

		// ���\�b�h�ԃo�b�N���[�h�X���C�X�p�̕K�v�ȏ����擾
		// Data Dependency�ɑ΂���������X���C�X�́C�ȉ��̏�Ԃ̂Ƃ��͍s��Ȃ�
		// 1. ���\�b�h�X�^�b�N����̂Ƃ�
		// 2. ���\�b�h�X�^�b�N����łȂ��C�X���C�X��̃m�[�h���X�^�b�N�̍ŏ㕔�̃m�[�h�ƈقȂ郁�\�b�h�ɂ���Ƃ�
		final Map<PDGNode<?>, CallInfo<?>> acrossBackwardNodesA = new HashMap<PDGNode<?>, CallInfo<?>>();
		if (this.callStackA.isEmpty()) {
			for (final Iterator<PDGDataDependenceEdge> iterator = backwardDataEdgesA
					.iterator(); iterator.hasNext();) {
				final PDGDataDependenceEdge edge = iterator.next();
				if (edge instanceof PDGAcrossEdge) {
					iterator.remove();
				}
			}
		} else {
			final CallInfo<?> call = this.callStackA.peek();
			for (final Iterator<PDGDataDependenceEdge> iterator = backwardDataEdgesA
					.iterator(); iterator.hasNext();) {
				final PDGDataDependenceEdge edge = iterator.next();
				if (edge instanceof PDGAcrossEdge) {
					final PDGAcrossEdge acrossEdge = (PDGAcrossEdge) edge;
					if (!call.equals(acrossEdge.getHolder())) {
						iterator.remove();
					} else {
						acrossBackwardNodesA.put(edge.getFromNode(), acrossEdge
								.getHolder());
					}
				}
			}
		}

		final Map<PDGNode<?>, CallInfo<?>> acrossBackwardNodesB = new HashMap<PDGNode<?>, CallInfo<?>>();
		if (this.callStackB.isEmpty()) {
			for (final Iterator<PDGDataDependenceEdge> iterator = backwardDataEdgesB
					.iterator(); iterator.hasNext();) {
				final PDGDataDependenceEdge edge = iterator.next();
				if (edge instanceof PDGAcrossEdge) {
					iterator.remove();
				}
			}
		} else {
			final CallInfo<?> call = this.callStackB.peek();
			for (final Iterator<PDGDataDependenceEdge> iterator = backwardDataEdgesB
					.iterator(); iterator.hasNext();) {
				final PDGDataDependenceEdge edge = iterator.next();
				if (edge instanceof PDGAcrossEdge) {
					final PDGAcrossEdge acrossEdge = (PDGAcrossEdge) edge;
					if (!call.equals(acrossEdge.getHolder())) {
						iterator.remove();
					} else {
						acrossBackwardNodesB.put(edge.getFromNode(), acrossEdge
								.getHolder());
					}
				}
			}
		}

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

		// ���\�b�h�ԃt�H���[�h�X���C�X�p�̕K�v�ȏ����擾
		final Map<PDGNode<?>, CallInfo<?>> acrossForwardNodesA = new HashMap<PDGNode<?>, CallInfo<?>>();
		for (final PDGDataDependenceEdge edge : forwardDataEdgesA) {
			if (edge instanceof PDGAcrossEdge) {
				final PDGAcrossEdge acrossEdge = (PDGAcrossEdge) edge;
				acrossForwardNodesA.put(edge.getToNode(), acrossEdge
						.getHolder());
			}
		}
		final Map<PDGNode<?>, CallInfo<?>> acrossForwardNodesB = new HashMap<PDGNode<?>, CallInfo<?>>();
		for (final PDGDataDependenceEdge edge : forwardDataEdgesB) {
			if (edge instanceof PDGAcrossEdge) {
				final PDGAcrossEdge acrossEdge = (PDGAcrossEdge) edge;
				acrossForwardNodesB.put(edge.getToNode(), acrossEdge
						.getHolder());
			}
		}

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
		final ClonePairInfo clonepair = new ClonePairInfo();

		// �e�m�[�h�̏W���ɑ΂��Ă��̐�ɂ���N���[���y�A�̍\�z
		{ // �o�b�N���[�h�X���C�X���g���ݒ�̏ꍇ
			final ClonePairInfo successor1 = this.enlargeClonePair(
					backwardExecutionNodesA, backwardExecutionNodesB,
					predecessorsA, predecessorsB, acrossBackwardNodesA,
					acrossBackwardNodesB, acrossForwardNodesA,
					acrossForwardNodesB);
			clonepair.add(successor1);
			final ClonePairInfo successor2 = this.enlargeClonePair(
					backwardDataNodesA, backwardDataNodesB, predecessorsA,
					predecessorsB, acrossBackwardNodesA, acrossBackwardNodesB,
					acrossForwardNodesA, acrossForwardNodesB);
			clonepair.add(successor2);
			final ClonePairInfo successor3 = this.enlargeClonePair(
					backwardControlNodesA, backwardControlNodesB,
					predecessorsA, predecessorsB, acrossBackwardNodesA,
					acrossBackwardNodesB, acrossForwardNodesA,
					acrossForwardNodesB);
			clonepair.add(successor3);
		}

		{ // �t�H���[�h�X���C�X���g���ݒ�̏ꍇ
			final ClonePairInfo successor1 = this.enlargeClonePair(
					forwardExecutionNodesA, forwardExecutionNodesB,
					predecessorsA, predecessorsB, acrossBackwardNodesA,
					acrossBackwardNodesB, acrossForwardNodesA,
					acrossForwardNodesB);
			clonepair.add(successor1);
			final ClonePairInfo successor2 = this.enlargeClonePair(
					forwardDataNodesA, forwardDataNodesB, predecessorsA,
					predecessorsB, acrossBackwardNodesA, acrossBackwardNodesB,
					acrossForwardNodesA, acrossForwardNodesB);
			clonepair.add(successor2);
			final ClonePairInfo successor3 = this.enlargeClonePair(
					forwardControlNodesA, forwardControlNodesB, predecessorsA,
					predecessorsB, acrossBackwardNodesA, acrossBackwardNodesB,
					acrossForwardNodesA, acrossForwardNodesB);
			clonepair.add(successor3);
		}

		// ���݂̃m�[�h���N���[���y�A�ɒǉ�
		NODE_PAIR_CACHE.add(nodepair);
		clonepair.add(nodepair);
		return clonepair;
	}

	private ClonePairInfo enlargeClonePair(final SortedSet<PDGNode<?>> nodesA,
			final SortedSet<PDGNode<?>> nodesB,
			final Set<PDGNode<?>> predecessorsA,
			final Set<PDGNode<?>> predecessorsB,
			final Map<PDGNode<?>, CallInfo<?>> acrossBackwardNodesA,
			final Map<PDGNode<?>, CallInfo<?>> acrossBackwardNodesB,
			final Map<PDGNode<?>, CallInfo<?>> acrossForwardNodesA,
			final Map<PDGNode<?>, CallInfo<?>> acrossForwardNodesB) {

		final ClonePairInfo clonepair = new ClonePairInfo();

		NODEA: for (final PDGNode<?> nodeA : nodesA) {

			// ���ɃN���[���ɓ��邱�Ƃ��m�肵�Ă���m�[�h�̂Ƃ��͒������Ȃ�
			// ���葤�̃N���[���ɓ����Ă���m�[�h�̂Ƃ����������Ȃ�
			if (predecessorsA.contains(nodeA) || predecessorsB.contains(nodeA)) {
				continue NODEA;
			}

			// �m�[�h�̃n�b�V���l�𓾂�
			Integer hashA = NODE_TO_HASH_MAP.get(nodeA);
			if (null == hashA) {
				final ExecutableElementInfo coreA = nodeA.getCore();
				hashA = Conversion.getNormalizedElement(coreA).hashCode();
				NODE_TO_HASH_MAP.put(nodeA, hashA);
			}

			NODEB: for (final PDGNode<?> nodeB : nodesB) {

				// ���ɃN���[���ɓ��邱�Ƃ��m�肵�Ă���m�[�h�̂Ƃ��͒������Ȃ�
				// ���葤�̃N���[���ɓ����Ă���m�[�h�̂Ƃ����������Ȃ�
				if (predecessorsB.contains(nodeB)
						|| predecessorsA.contains(nodeB)) {
					continue NODEB;
				}

				// �m�[�h�̃n�b�V���l�𓾂�
				Integer hashB = NODE_TO_HASH_MAP.get(nodeB);
				if (null == hashB) {
					final ExecutableElementInfo coreB = nodeB.getCore();
					hashB = Conversion.getNormalizedElement(coreB).hashCode();
					NODE_TO_HASH_MAP.put(nodeB, hashB);
				}

				SlicingThread.increaseNumberOfComparison();

				// �m�[�h�̃n�b�V���l���������ꍇ�́C���̃m�[�h�y�A�̐������ɒ���
				if (hashA.intValue() == hashB.intValue()) {

					// �m�[�h�������ꍇ�͒������Ȃ�
					if (nodeA == nodeB) {
						continue NODEB;
					}

					// ���\�b�h���܂�����ꍇ�̓R�[���X�^�b�N�̍X�V���s��
					if (acrossBackwardNodesA.containsKey(nodeA)) {
						if (!this.callStackA.peek().equals(
								acrossBackwardNodesA.get(nodeA))) {
							throw new IllegalStateException();
						} else {
							this.callStackA.pop();
						}
					} else if (acrossForwardNodesA.containsKey(nodeA)) {
						this.callStackA.push(acrossForwardNodesA.get(nodeA));
					}
					if (acrossBackwardNodesB.containsKey(nodeB)) {
						if (!this.callStackB.peek().equals(
								acrossBackwardNodesB.get(nodeB))) {
							throw new IllegalStateException();
						} else {
							this.callStackB.pop();
						}
					} else if (acrossForwardNodesB.containsKey(nodeB)) {
						this.callStackB.push(acrossForwardNodesB.get(nodeB));
					}

					final Set<PDGNode<?>> newPredicessorsA = new HashSet<PDGNode<?>>(
							predecessorsA);
					final Set<PDGNode<?>> newPredicessorsB = new HashSet<PDGNode<?>>(
							predecessorsB);

					final ClonePairInfo successor = this.perform(nodeA, nodeB,
							newPredicessorsA, newPredicessorsB);
					clonepair.add(successor);

					// ���\�b�h���܂�����ꍇ�̓R�[���X�^�b�N�̍X�V���s���i�㏈���Ƃ��ĕK�v�j
					if (acrossBackwardNodesA.containsKey(nodeA)) {
						this.callStackA.push(acrossBackwardNodesA.get(nodeA));
					} else if (acrossForwardNodesA.containsKey(nodeA)) {
						final CallInfo<?> call = this.callStackA.pop();
						// successor�̑傫����0�łȂ��Ȃ�΁C�Y�����郁�\�b�h�Ăяo�����N���[���ɒǉ�
						if (0 < successor.length()) {
							clonepair.addCallA(call);
						}
					}
					if (acrossBackwardNodesB.containsKey(nodeB)) {
						this.callStackB.push(acrossBackwardNodesB.get(nodeB));
					} else if (acrossForwardNodesB.containsKey(nodeB)) {
						final CallInfo<?> call = this.callStackB.pop();
						// successor�̑傫����0�łȂ��Ȃ�΁C�Y�����郁�\�b�h�Ăяo�����N���[���ɒǉ�
						if (0 < successor.length()) {
							clonepair.addCallB(call);
						}
					}
				}
			}
		}

		return clonepair;
	}
}
