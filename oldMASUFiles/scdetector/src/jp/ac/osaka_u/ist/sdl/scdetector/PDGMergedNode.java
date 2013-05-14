package jp.ac.osaka_u.ist.sdl.scdetector;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGNormalNode;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.IntraProceduralPDG;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGControlDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGDataDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGExecutionDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.IPDGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNormalNode;

public class PDGMergedNode extends PDGNormalNode<CFGNormalNode<?>> {

	/**
	 * �����ŗ^����ꂽPDG�̒��_���k���s��
	 * 
	 * @param pdg
	 */
	public static void merge(final IntraProceduralPDG pdg,
			final IPDGNodeFactory pdgNodeFactory) {

		for (final PDGNode<?> node : PDGControlNode.getControlNodes(pdg
				.getAllNodes())) {

			// ����m�[�h�̎����m�[�}���m�[�h�ł���Έ��k�\���ǂ����𒲂ׂ�
			if (node instanceof PDGControlNode) {
				for (final PDGEdge edge : PDGExecutionDependenceEdge
						.extractExecutionDependenceEdge(node.getForwardEdges())) {
					final PDGNode<?> toNode = edge.getToNode();
					if (toNode instanceof PDGNormalNode<?>) {
						findMergedNodes(pdg, (PDGNormalNode<?>) toNode,
								pdgNodeFactory);
					}
				}
			}
		}
	}

	/**
	 * �����ŗ^����ꂽ���_����C���s�ˑ��ł��ǂ��Ă����CPDGNormalNode�ł��邩����C���k�����݂�
	 * 
	 * @param node
	 */
	private static void findMergedNodes(final IntraProceduralPDG pdg,
			final PDGNormalNode<?> node, final IPDGNodeFactory pdgNodeFactory) {

		final int hash = Conversion.getNormalizedElement(node.getCore())
				.hashCode();
		final PDGMergedNode mergedNode = new PDGMergedNode(node);

		final SortedSet<PDGExecutionDependenceEdge> toEdges = PDGExecutionDependenceEdge
				.extractExecutionDependenceEdge(node.getForwardEdges());
		if (toEdges.isEmpty()) {
			return;
		}

		PDGNode<?> toNode = toEdges.first().getToNode();
		if (!(toNode instanceof PDGNormalNode<?>)) { // ����toNode���m�[�}���łȂ��ꍇ�͉������Ȃ��Ă���
			return;
		}
		int toHash = Conversion.getNormalizedElement(toNode.getCore())
				.hashCode();

		while (hash == toHash) {
			mergedNode.addNode((PDGNormalNode<?>) toNode);
			final SortedSet<PDGExecutionDependenceEdge> forwardEdges = PDGExecutionDependenceEdge
					.extractExecutionDependenceEdge(toNode.getForwardEdges());
			if (forwardEdges.isEmpty()) {
				insertMergedNode(pdg, mergedNode, pdgNodeFactory);
				return;
			}
			toNode = forwardEdges.first().getToNode();
			if (!(toNode instanceof PDGNormalNode<?>)) {
				insertMergedNode(pdg, mergedNode, pdgNodeFactory);
				return;
			}
			toHash = Conversion.getNormalizedElement(toNode.getCore())
					.hashCode();
		}
		insertMergedNode(pdg, mergedNode, pdgNodeFactory);
		findMergedNodes(pdg, (PDGNormalNode<?>) toNode, pdgNodeFactory);
	}

	private static void insertMergedNode(final IntraProceduralPDG pdg,
			final PDGMergedNode mergedNode, final IPDGNodeFactory pdgNodeFactory) {

		final List<PDGNormalNode<?>> originalNodes = mergedNode
				.getOriginalNodes();

		// �W��m�[�h�̏W�񐔂�1�̏ꍇ�͂Ȃɂ����Ȃ��Ă���
		if (1 == originalNodes.size()) {
			return;
		}

		// ���̐擪�m�[�h������
		{
			final PDGNormalNode<?> startNode = originalNodes.get(0);
			for (final PDGEdge edge : startNode.getBackwardEdges()) {

				// ���s�ˑ��ӈȊO�͑ΏۊO
				if (!(edge instanceof PDGExecutionDependenceEdge)) {
					continue;
				}

				final PDGNode<?> previousNode = edge.getFromNode();

				// �I���W�i���m�[�h���폜
				for (final PDGEdge previousEdge : PDGExecutionDependenceEdge
						.extractExecutionDependenceEdge(previousNode
								.getForwardEdges())) {
					if (previousEdge.getToNode().equals(startNode)) {
						previousNode.removeForwardEdge(previousEdge);
					}
				}

				// �W��m�[�h��ǉ�
				final PDGEdge mergedEdge = new PDGExecutionDependenceEdge(
						previousNode, mergedNode);
				previousNode.addForwardEdge(mergedEdge);
				mergedNode.addBackwardEdge(mergedEdge);
			}
		}

		// ���̍Ō�m�[�h������
		{
			final PDGNormalNode<?> endNode = originalNodes.get(originalNodes
					.size() - 1);
			for (final PDGEdge edge : endNode.getForwardEdges()) {

				// ���s�ˑ��ӈȊO�͑ΏۊO
				if (!(edge instanceof PDGExecutionDependenceEdge)) {
					continue;
				}

				final PDGNode<?> postiousNode = edge.getToNode();

				// �I���W�i���m�[�h���폜
				for (final PDGEdge postiousEdge : PDGExecutionDependenceEdge
						.extractExecutionDependenceEdge(postiousNode
								.getBackwardEdges())) {
					if (postiousEdge.getFromNode().equals(endNode)) {
						postiousNode.removeBackwardEdge(postiousEdge);
					}
				}

				// �W��m�[�h��ǉ�
				final PDGEdge mergedEdge = new PDGExecutionDependenceEdge(
						mergedNode, postiousNode);
				mergedNode.addForwardEdge(mergedEdge);
				postiousNode.addBackwardEdge(mergedEdge);
			}
		}

		// �m�[�h�t�@�N�g���ւ̓o�^�ƍ폜
		for (final PDGNode<?> node : originalNodes) {
			final ExecutableElementInfo element = node.getCore();
			pdgNodeFactory.removeNode(element);
		}
		pdgNodeFactory.addNode(mergedNode);

		// PDG�ւ̓o�^�ƍ폜
		for (final PDGNode<?> node : originalNodes) {
			pdg.removeNode(node);
		}
		pdg.addNode(mergedNode);

		// �W��m�[�h�ɑ΂���f�[�^�ˑ��ӁC����ˑ��ӂ̍\�z
		for (final PDGNode<?> originalNode : originalNodes) {

			for (final PDGDataDependenceEdge edge : PDGDataDependenceEdge
					.extractDataDependenceEdge(originalNode.getBackwardEdges())) {

				final PDGNode<?> fromNode = edge.getFromNode();
				if (!originalNodes.contains(fromNode)) {
					final VariableInfo<?> variable = edge.getVariable();
					final PDGDataDependenceEdge newEdge = new PDGDataDependenceEdge(
							fromNode, mergedNode, variable);
					fromNode.addForwardEdge(newEdge);
					mergedNode.addBackwardEdge(newEdge);
					fromNode.removeForwardEdge(edge);
				}
			}

			for (final PDGDataDependenceEdge edge : PDGDataDependenceEdge
					.extractDataDependenceEdge(originalNode.getForwardEdges())) {

				final PDGNode<?> toNode = edge.getToNode();
				if (!originalNodes.contains(toNode)) {
					final VariableInfo<?> variable = edge.getVariable();
					final PDGDataDependenceEdge newEdge = new PDGDataDependenceEdge(
							mergedNode, toNode, variable);
					mergedNode.addForwardEdge(newEdge);
					toNode.addBackwardEdge(newEdge);
					toNode.removeBackwardEdge(edge);
				}
			}

			for (final PDGControlDependenceEdge edge : PDGControlDependenceEdge
					.extractControlDependenceEdge(originalNode
							.getBackwardEdges())) {

				final PDGControlNode fromNode = (PDGControlNode) edge
						.getFromNode();
				if (!originalNodes.contains(fromNode)) {
					final boolean flag = edge.isTrueDependence();
					final PDGControlDependenceEdge newEdge = new PDGControlDependenceEdge(
							fromNode, mergedNode, flag);
					fromNode.addForwardEdge(newEdge);
					mergedNode.addBackwardEdge(newEdge);
					fromNode.removeForwardEdge(edge);
				}
			}
		}
	}

	public PDGMergedNode(final PDGNormalNode<?> node) {
		super(node.getCFGNode());
		this.originalNodes = new LinkedList<PDGNormalNode<?>>();
		this.originalNodes.add(node);
	}

	@Override
	public SortedSet<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
		final SortedSet<VariableInfo<? extends UnitInfo>> variables = new TreeSet<VariableInfo<? extends UnitInfo>>();
		for (final PDGNode<?> originalNode : this.getOriginalNodes()) {
			variables.addAll(originalNode.getDefinedVariables());
		}

		return Collections.unmodifiableSortedSet(variables);
	}

	@Override
	public SortedSet<VariableInfo<? extends UnitInfo>> getReferencedVariables() {
		final SortedSet<VariableInfo<? extends UnitInfo>> variables = new TreeSet<VariableInfo<? extends UnitInfo>>();
		for (final PDGNode<?> originalNode : this.getOriginalNodes()) {
			variables.addAll(originalNode.getReferencedVariables());
		}

		return Collections.unmodifiableSortedSet(variables);
	}

	public void addNode(final PDGNormalNode<?> node) {
		this.originalNodes.add(node);
	}

	public List<PDGNormalNode<?>> getOriginalNodes() {
		return Collections.unmodifiableList(this.originalNodes);
	}

	public SortedSet<ExecutableElementInfo> getCores() {
		final SortedSet<ExecutableElementInfo> cores = new TreeSet<ExecutableElementInfo>();
		for (final PDGNode<?> originalNode : this.getOriginalNodes()) {
			cores.add(originalNode.getCore());
		}
		return Collections.unmodifiableSortedSet(cores);
	}

	private final List<PDGNormalNode<?>> originalNodes;
}
