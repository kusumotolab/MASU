package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGUtility;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.SimpleCFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGControlEdge;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGNormalEdge;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayElementUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BinominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CastUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalClauseInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EmptyExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LiteralUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MonominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NullUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.OPERATOR;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParenthesesExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TernaryOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;

/**
 * ����ˑ��O���t�̃m�[�h��\���N���X
 * 
 * @author t-miyake, higo
 * 
 * @param <T>
 *            �m�[�h�̊j�ƂȂ���̌^
 */
public abstract class CFGNode<T extends ExecutableElementInfo> implements
		Comparable<CFGNode<? extends ExecutableElementInfo>> {

	private static final AtomicLong DUMMY_VARIABLE_NUMBER = new AtomicLong();

	/**
	 * ���_�𕪉�����Ƃ��ɗp����ϐ��̖��O�𐶐����邽�߂̃��\�b�h
	 * 
	 * @return
	 */
	protected static final String getDummyVariableName() {
		final StringBuilder text = new StringBuilder();
		text.append("$");
		text.append(Long.toString(DUMMY_VARIABLE_NUMBER.getAndIncrement()));
		return text.toString();

	}

	/**
	 * ���̃m�[�h�̃t�H���[�h�m�[�h�̃Z�b�g
	 */
	protected final Set<CFGEdge> forwardEdges;

	/**
	 * ���̃m�[�h�̃o�b�N���[�h�m�[�h�̃Z�b�g
	 */
	protected final Set<CFGEdge> backwardEdges;

	/**
	 * ���̒��_�̃e�L�X�g�\��
	 */
	private String text;

	/**
	 * ���̃m�[�h�ɑΉ����镶
	 */
	private T core;

	/**
	 * �j�ƂȂ�v���O�����v�f��^����CFG��������
	 * 
	 * @param core
	 */
	protected CFGNode(final T core) {

		this.forwardEdges = new HashSet<CFGEdge>();
		this.backwardEdges = new HashSet<CFGEdge>();

		this.setCore(core);
	}

	/**
	 * �����ŗ^����ꂽ�ӂ����̒��_�̃t�H���[�h�G�b�W�Ƃ��Ēǉ�����
	 * 
	 * @param forwardEdge
	 */
	public void addForwardEdge(final CFGEdge forwardEdge) {

		if (null == forwardEdge) {
			throw new IllegalArgumentException();
		}

		if (!this.equals(forwardEdge.getFromNode())) {
			throw new IllegalArgumentException();
		}

		if (this.forwardEdges.add(forwardEdge)) {
			forwardEdge.getToNode().backwardEdges.add(forwardEdge);
		}
	}

	/**
	 * �����ŗ^����ꂽ�ӂ����̒��_�̃o�b�N���[�h�G�b�W�Ƃ��Ēǉ�����
	 * 
	 * @param backwardEdge
	 */
	public void addBackwardEdge(final CFGEdge backwardEdge) {

		if (null == backwardEdge) {
			throw new IllegalArgumentException();
		}

		if (!this.equals(backwardEdge.getToNode())) {
			throw new IllegalArgumentException();
		}

		if (this.backwardEdges.add(backwardEdge)) {
			backwardEdge.getFromNode().forwardEdges.add(backwardEdge);
		}
	}

	/**
	 * �����ŗ^����ꂽ�ӂ��t�H���[�h�G�b�W�����菜��
	 * 
	 * @param forwardEdge
	 */
	void removeForwardEdge(final CFGEdge forwardEdge) {

		if (null == forwardEdge) {
			throw new IllegalArgumentException();
		}

		this.forwardEdges.remove(forwardEdge);
	}

	/**
	 * �����ŗ^����ꂽ�ӌQ���t�H���[�h�G�b�W�����菜��
	 * 
	 * @param forwardEdge
	 */
	void removeForwardEdges(final Collection<CFGEdge> forwardEdges) {

		if (null == forwardEdges) {
			throw new IllegalArgumentException();
		}

		this.forwardEdges.removeAll(forwardEdges);
	}

	/**
	 * �����ŗ^����ꂽ�ӂ��o�b�N���[�h�G�b�W�����菜��
	 * 
	 * @param backwardEdge
	 */
	void removeBackwardEdge(final CFGEdge backwardEdge) {

		if (null == backwardEdge) {
			throw new IllegalArgumentException();
		}

		this.backwardEdges.remove(backwardEdge);
	}

	/**
	 * �����ŗ^����ꂽ�ӌQ���o�b�N���[�h�G�b�W�����菜��
	 * 
	 * @param backwardEdges
	 */
	void removeBackwardEdges(final Collection<CFGEdge> backwardEdges) {

		if (null == backwardEdges) {
			throw new IllegalArgumentException();
		}

		this.backwardEdges.removeAll(backwardEdges);
	}

	/**
	 * ���̃m�[�h��O�m�[�h�ƌ��m�[�h����H��Ȃ��悤�ɂ���
	 */
	void remove() {
		for (final CFGEdge edge : this.getBackwardEdges()) {
			final CFGNode<?> backwardNode = edge.getFromNode();
			backwardNode.removeForwardEdge(edge);
		}
		for (final CFGEdge edge : this.getForwardEdges()) {
			final CFGNode<?> forwardNode = edge.getToNode();
			forwardNode.removeBackwardEdge(edge);
		}
	}

	/**
	 * ���̃m�[�h�����݂���ʒu�ɁC�����ŗ^����ꂽ�m�[�h��ǉ����C���̃m�[�h���폜
	 * 
	 * @param node
	 */
	void replace(final CFGNode<?> node) {
		if (null == node) {
			throw new IllegalArgumentException();
		}
		this.remove();
		for (final CFGEdge edge : this.getBackwardEdges()) {
			final CFGNode<?> backwardNode = edge.getFromNode();
			final CFGEdge newEdge = edge.replaceToNode(node);
			backwardNode.addForwardEdge(newEdge);
		}
		for (final CFGEdge edge : this.getForwardEdges()) {
			final CFGNode<?> forwardNode = edge.getToNode();
			final CFGEdge newEdge = edge.replaceFromNode(node);
			forwardNode.addBackwardEdge(newEdge);
		}
	}

	void setCore(final ExecutableElementInfo core) {

		if (null == core) {
			throw new IllegalArgumentException("core is null");
		}

		this.core = (T) core;

		final StringBuilder text = new StringBuilder();
		text.append(core.getText());
		text.append(" <");
		text.append(core.getFromLine());
		text.append(".");
		text.append(core.getFromColumn());
		text.append(" - ");
		text.append(core.getToLine());
		text.append(".");
		text.append(core.getToColumn());
		text.append("> ");
		this.text = text.toString();
	}

	/**
	 * ���̃m�[�h�ɑΉ����镶�̏����擾
	 * 
	 * @return ���̃m�[�h�ɑΉ����镶
	 */
	public T getCore() {
		return this.core;
	}

	/**
	 * ���̃m�[�h�̃t�H���[�h�m�[�h�̃Z�b�g���擾
	 * 
	 * @return ���̃m�[�h�̃t�H���[�h�m�[�h�̃Z�b�g
	 */
	public Set<CFGNode<? extends ExecutableElementInfo>> getForwardNodes() {
		final Set<CFGNode<? extends ExecutableElementInfo>> forwardNodes = new HashSet<CFGNode<? extends ExecutableElementInfo>>();
		for (final CFGEdge forwardEdge : this.getForwardEdges()) {
			forwardNodes.add(forwardEdge.getToNode());
		}
		return Collections.unmodifiableSet(forwardNodes);
	}

	/**
	 * ���̃m�[�h�̃t�H���[�h�G�b�W�̃Z�b�g���擾
	 * 
	 * @return ���̃m�[�h�̃t�H���[�h�G�b�W�̃Z�b�g
	 */
	public Set<CFGEdge> getForwardEdges() {
		return Collections.unmodifiableSet(this.forwardEdges);
	}

	/**
	 * ���̃m�[�h�̃o�b�N���[�h�m�[�h�̃Z�b�g���擾
	 * 
	 * @return ���̃m�[�h�̃o�b�N���[�h�m�[�h�̃Z�b�g
	 */
	public Set<CFGNode<? extends ExecutableElementInfo>> getBackwardNodes() {
		final Set<CFGNode<? extends ExecutableElementInfo>> backwardNodes = new HashSet<CFGNode<? extends ExecutableElementInfo>>();
		for (final CFGEdge backwardEdge : this.getBackwardEdges()) {
			backwardNodes.add(backwardEdge.getFromNode());
		}
		return Collections.unmodifiableSet(backwardNodes);
	}

	/**
	 * ���̃m�[�h�̃o�b�N���[�h�G�b�W�̃Z�b�g���擾
	 * 
	 * @return ���̃m�[�h�̃o�b�N���[�h�G�b�W�̃Z�b�g
	 */
	public Set<CFGEdge> getBackwardEdges() {
		return Collections.unmodifiableSet(this.backwardEdges);
	}

	@Override
	public int compareTo(final CFGNode<? extends ExecutableElementInfo> node) {

		if (null == node) {
			throw new IllegalArgumentException();
		}

		final int methodOrder = this.getCore().getOwnerMethod().compareTo(
				node.getCore().getOwnerMethod());
		if (0 != methodOrder) {
			return methodOrder;
		}

		return this.getCore().compareTo(node.getCore());
	}

	/**
	 * �K�v�̂Ȃ��m�[�h�̏ꍇ�́C���̃��\�b�h���I�[�o�[���C�h���邱�Ƃɂ���āC�폜�����
	 * �œK���ɂ��m�[�h���폜�����Ƃ���true,�����łȂ��Ƃ���false��Ԃ�.
	 */
	public boolean optimize() {
		return false;
	}

	/**
	 * ���̒��_�̃e�L�X�g�\����Ԃ�
	 * 
	 * @return
	 */
	public final String getText() {
		return this.text;
	}

	/**
	 * ���̃m�[�h�Œ�`�E�ύX����Ă���ϐ���Set��Ԃ�
	 * 
	 * @param countObjectStateChange
	 *            �Ăяo���ꂽ���\�b�h�Ȃ��ł̃I�u�W�F�N�g�̏�ԕύX
	 *            �i�t�B�[���h�ւ̑���Ȃǁj���Q�Ƃ���Ă���ϐ��̕ύX�Ƃ���ꍇ��true�D
	 * 
	 * @return
	 */
	public final Set<VariableInfo<? extends UnitInfo>> getDefinedVariables(
			final boolean countObjectStateChange) {

		final Set<VariableInfo<? extends UnitInfo>> assignments = new HashSet<VariableInfo<? extends UnitInfo>>();
		assignments.addAll(VariableUsageInfo.getUsedVariables(VariableUsageInfo
				.getAssignments(this.getCore().getVariableUsages())));

		// �I�u�W�F�N�g�̏�ԕύX���C�ϐ��̕ύX�Ƃ����ꍇ
		if (countObjectStateChange) {
			for (final CallInfo<? extends CallableUnitInfo> call : this
					.getCore().getCalls()) {
				if (call instanceof MethodCallInfo) {
					final MethodCallInfo methodCall = (MethodCallInfo) call;
					final MethodInfo callee = methodCall.getCallee();

					// methodCall��quantifier�𒲍�
					if (CFGUtility.stateChange(callee)) {
						final ExpressionInfo qualifier = methodCall
								.getQualifierExpression();
						if (qualifier instanceof VariableUsageInfo<?>) {
							assignments.add(((VariableUsageInfo<?>) qualifier)
									.getUsedVariable());
						}
					}

					for (final MethodInfo overrider : callee.getOverriders()) {
						if (CFGUtility.stateChange(overrider)) {
							final ExpressionInfo qualifier = methodCall
									.getQualifierExpression();
							if (qualifier instanceof VariableUsageInfo<?>) {
								assignments
										.add(((VariableUsageInfo<?>) qualifier)
												.getUsedVariable());
							}
						}
					}
				}

				if (call instanceof MethodCallInfo
						|| call instanceof ClassConstructorCallInfo) {
					// methodCall��parameter�𒲍�
					final List<ExpressionInfo> arguments = call.getArguments();
					for (int index = 0; index < arguments.size(); index++) {

						final CallableUnitInfo callee = call.getCallee();
						if (CFGUtility.stateChange(callee, index)) {
							final ExpressionInfo argument = call.getArguments()
									.get(index);
							if (argument instanceof VariableUsageInfo<?>) {
								assignments
										.add(((VariableUsageInfo<?>) argument)
												.getUsedVariable());
							}
						}

						if (callee instanceof MethodInfo) {
							for (final MethodInfo overrider : ((MethodInfo) callee)
									.getOverriders()) {
								if (CFGUtility.stateChange(overrider, index)) {
									final ExpressionInfo argument = call
											.getArguments().get(index);
									if (argument instanceof VariableUsageInfo<?>) {
										assignments
												.add(((VariableUsageInfo<?>) argument)
														.getUsedVariable());
									}
								}
							}
						}

					}
				}
			}
		}

		return assignments;

	}

	/**
	 * ���̃m�[�h�ŗ��p�i�Q�Ɓj����Ă���ϐ���Set��Ԃ�
	 * 
	 * @return
	 */
	public final Set<VariableInfo<? extends UnitInfo>> getReferencedVariables() {
		return VariableUsageInfo.getUsedVariables(VariableUsageInfo
				.getReferencees(this.getCore().getVariableUsages()));
	}

	/**
	 * ����ΏۂƂȂ肤��ExpressionInfo��Ԃ��D
	 * 
	 * @return
	 */
	abstract ExpressionInfo getDissolvingTarget();

	/**
	 * ���������m�[�h���g���čč\�z���s���D
	 * 
	 * @param requiredExpressions
	 * @return
	 */
	abstract T makeNewElement(LocalSpaceInfo ownerSpace,
			ExpressionInfo... requiredExpressions);

	/**
	 * �ʒu���w�肵�ĕ��������m�[�h���g���čč\�z���s��
	 * 
	 * @param ownerSpace
	 * @param fromLine
	 * @param fromColumn
	 * @param toLine
	 * @param toColumn
	 * @param requiredExpressions
	 * @return
	 */
	abstract T makeNewElement(LocalSpaceInfo ownerSpace, int fromLine,
			int fromColumn, int toLine, int toColumn,
			ExpressionInfo... requiredExpressions);

	/**
	 * ���̃m�[�h�𕪉����郁�\�b�h�D �������ꂽ�ꍇ�́C������̃m�[�h�Q����Ȃ�CFG��Ԃ��D �������s���Ȃ������ꍇ��null��Ԃ��D
	 * 
	 * @param nodeFactory
	 * @return
	 */
	public CFG dissolve(final ICFGNodeFactory nodeFactory) {

		final ExpressionInfo dissolvingTarget = (ExpressionInfo) this
				.getDissolvingTarget();

		// ����Ώۂ��Ȃ��ꍇ�͉������Ȃ��Ŕ�����
		if (null == dissolvingTarget) {
			return null;
		}

		final CFG cfg;
		if (dissolvingTarget instanceof ArrayElementUsageInfo) {

			cfg = this.dissolveArrayElementUsage(
					(ArrayElementUsageInfo) dissolvingTarget.copy(),
					nodeFactory);

		} else if (dissolvingTarget instanceof ArrayInitializerInfo) {

			cfg = this
					.dissolveArrayInitializer(
							(ArrayInitializerInfo) dissolvingTarget.copy(),
							nodeFactory);

		} else if (dissolvingTarget instanceof ArrayTypeReferenceInfo) {

			cfg = null;

		} else if (dissolvingTarget instanceof BinominalOperationInfo) {

			cfg = this.dissolveBinominalOperationInfo(
					(BinominalOperationInfo) dissolvingTarget.copy(),
					nodeFactory);

		} else if (dissolvingTarget instanceof CallInfo<?>) {

			cfg = this.dissolveCall((CallInfo<?>) dissolvingTarget.copy(),
					nodeFactory);

		} else if (dissolvingTarget instanceof CastUsageInfo) {

			cfg = this.dissolveCastUsage((CastUsageInfo) dissolvingTarget
					.copy(), nodeFactory);

		} else if (dissolvingTarget instanceof ClassReferenceInfo) {

			cfg = null;

		} else if (dissolvingTarget instanceof EmptyExpressionInfo) {

			cfg = null;

		} else if (dissolvingTarget instanceof ForeachConditionInfo) {

			cfg = null;

		} else if (dissolvingTarget instanceof LiteralUsageInfo) {

			cfg = null;

		} else if (dissolvingTarget instanceof MonominalOperationInfo) {

			cfg = this.dissolveMonominalOperation(
					(MonominalOperationInfo) dissolvingTarget.copy(),
					nodeFactory);

		} else if (dissolvingTarget instanceof NullUsageInfo) {

			cfg = null;

		} else if (dissolvingTarget instanceof ParenthesesExpressionInfo) {

			cfg = this.dissolveParenthesesExpression(
					(ParenthesesExpressionInfo) dissolvingTarget.copy(),
					nodeFactory);

		} else if (dissolvingTarget instanceof TernaryOperationInfo) {

			cfg = this
					.dissolveTernaryOperation(
							(TernaryOperationInfo) dissolvingTarget.copy(),
							nodeFactory);

		} else if (dissolvingTarget instanceof UnknownEntityUsageInfo) {

			cfg = null;

		} else if (dissolvingTarget instanceof VariableUsageInfo<?>) {

			cfg = null;

		} else {
			throw new IllegalStateException("unknown expression type.");
		}

		if (null == cfg) {
			return null;
		}

		return cfg;
	}

	/**
	 * �E�ӂ�ArrayElementUsage�ł��������𕪉����邽�߂̃��\�b�h
	 * 
	 * @param arrayElementUsage
	 * @param nodeFactory
	 * @return
	 */
	private CFG dissolveArrayElementUsage(
			final ArrayElementUsageInfo arrayElementUsage,
			final ICFGNodeFactory nodeFactory) {

		final T core = this.getCore();
		final ExpressionInfo indexExpression = (ExpressionInfo) arrayElementUsage
				.getIndexExpression().copy();
		final ExpressionInfo qualifiedExpression = (ExpressionInfo) arrayElementUsage
				.getQualifierExpression().copy();

		final boolean indexExpressionIsDissolved = CFGUtility
				.isDissolved(indexExpression);
		final boolean qualifiedExpressionIsDissolved = CFGUtility
				.isDissolved(qualifiedExpression);

		// indexExpression��qualifiedExpression����������Ȃ��Ƃ��͉��������ɔ�����
		if (!indexExpressionIsDissolved && !qualifiedExpressionIsDissolved) {
			return null;
		}

		// ����O�̕�����K�v�ȏ����擾
		final LocalSpaceInfo ownerSpace = arrayElementUsage.getOwnerSpace();
		final ConditionalBlockInfo ownerConditionalBlock = arrayElementUsage
				.getOwnerConditionalBlock();
		final ExecutableElementInfo ownerExecutableElement = arrayElementUsage
				.getOwnerExecutableElement();
		final int fromLine = arrayElementUsage.getFromLine();
		final int fromColumn = arrayElementUsage.getFromColumn();
		final int toLine = arrayElementUsage.getToLine();
		final int toColumn = arrayElementUsage.getToColumn();

		final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
				: ownerSpace.getOuterCallableUnit();

		final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
		final LinkedList<LocalVariableUsageInfo> dissolvedVariableUsageList = new LinkedList<LocalVariableUsageInfo>();

		if (indexExpressionIsDissolved) {
			this.makeDissolvedNode(indexExpression, nodeFactory,
					dissolvedNodeList, dissolvedVariableUsageList);
		}

		if (qualifiedExpressionIsDissolved) {
			this.makeDissolvedNode(qualifiedExpression, nodeFactory,
					dissolvedNodeList, dissolvedVariableUsageList);
		}

		// �Â��m�[�h���폜
		nodeFactory.removeNode(core);
		this.remove();

		// �_�~�[�ϐ��𗘗p����ArrayElementUsageInfo�C����т����p�����V�����v���O�����v�f���쐬
		int index = 0;
		final ArrayElementUsageInfo newArrayElementUsage = new ArrayElementUsageInfo(
				indexExpressionIsDissolved ? dissolvedVariableUsageList
						.get(index++) : indexExpression,
				qualifiedExpressionIsDissolved ? dissolvedVariableUsageList
						.get(index++) : qualifiedExpression, outerCallableUnit,
				fromLine, fromColumn, toLine, toColumn);
		newArrayElementUsage.setOwnerConditionalBlock(ownerConditionalBlock);
		newArrayElementUsage.setOwnerExecutableElement(ownerExecutableElement);
		final ExecutableElementInfo newElement = this.makeNewElement(
				ownerSpace, newArrayElementUsage);
		final CFGNode<?> newNode = nodeFactory.makeNormalNode(newElement);
		dissolvedNodeList.add(newNode);

		// ���������m�[�h���G�b�W�łȂ�
		this.makeEdges(dissolvedNodeList);

		// ���������m�[�h�Q����CFG���\�z
		final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

		// ���������m�[�h���m�[�h�t�@�N�g����dissolvedNode�ɓo�^
		nodeFactory.addDissolvedNodes(core, newCFG.getAllNodes());

		return newCFG;
	}

	/**
	 * �E�ӂ�ArrayInitializerInfo�ł��������𕪉����邽�߂̃��\�b�h
	 * 
	 * @param arrayInitialier
	 * @param nodeFactory
	 * @return
	 */
	private CFG dissolveArrayInitializer(
			final ArrayInitializerInfo arrayInitializer,
			final ICFGNodeFactory nodeFactory) {

		final T core = this.getCore();
		final List<ExpressionInfo> initializers = new LinkedList<ExpressionInfo>();
		for (final ExpressionInfo initializer : arrayInitializer
				.getElementInitializers()) {
			initializers.add((ExpressionInfo) initializer.copy());
		}

		final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
		final LinkedList<LocalVariableUsageInfo> dissolvedVariableUsageList = new LinkedList<LocalVariableUsageInfo>();

		// �e�C�j�V�����C�U�𕪉����ׂ����`�F�b�N���C�������C��������������V�K�m�[�h���쐬
		final List<ExpressionInfo> newInitializers = new LinkedList<ExpressionInfo>();
		for (final ExpressionInfo initializer : initializers) {

			if (CFGUtility.isDissolved(initializer)) {

				this.makeDissolvedNode(initializer, nodeFactory,
						dissolvedNodeList, dissolvedVariableUsageList);

				newInitializers.add(dissolvedVariableUsageList.getLast());
			}

			else {
				newInitializers.add(initializer);
			}
		}

		// �������ꂽ�C�j�V�����C�U���Ȃ���Ή��������ɔ�����
		if (dissolvedNodeList.isEmpty()) {
			return null;
		}

		// �Â��m�[�h���폜
		nodeFactory.removeNode(core);
		this.remove();

		// ����O�̕�����K�v�ȏ����擾
		final LocalSpaceInfo ownerSpace = core.getOwnerSpace();
		final ConditionalBlockInfo ownerConditionalBlock = arrayInitializer
				.getOwnerConditionalBlock();
		final ExecutableElementInfo ownerExecutableElement = arrayInitializer
				.getOwnerExecutableElement();
		final int fromLine = arrayInitializer.getFromLine();
		final int fromColumn = arrayInitializer.getFromColumn();
		final int toLine = arrayInitializer.getToLine();
		final int toColumn = arrayInitializer.getToColumn();

		final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
				: ownerSpace.getOuterCallableUnit();

		final ArrayInitializerInfo newArrayInitializer = new ArrayInitializerInfo(
				newInitializers, outerCallableUnit, fromLine, fromColumn,
				toLine, toColumn);
		newArrayInitializer.setOwnerConditionalBlock(ownerConditionalBlock);
		newArrayInitializer.setOwnerExecutableElement(ownerExecutableElement);
		final ExecutableElementInfo newElement = this.makeNewElement(
				ownerSpace, newArrayInitializer);
		final CFGNode<?> newNode = nodeFactory.makeNormalNode(newElement);
		dissolvedNodeList.add(newNode);

		// ���������m�[�h���G�b�W�łȂ�
		this.makeEdges(dissolvedNodeList);

		// ���������m�[�h�Q����CFG���\�z
		final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

		// ���������m�[�h���m�[�h�t�@�N�g����dissolvedNode�ɓo�^
		nodeFactory.addDissolvedNodes(core, newCFG.getAllNodes());

		return newCFG;
	}

	/**
	 * �E�ӂ� BinominalOperation�ł��������𕪉����邽�߂̃��\�b�h
	 * 
	 * @param binominalOperation
	 * @param nodeFactory
	 * @return
	 */
	private CFG dissolveBinominalOperationInfo(
			final BinominalOperationInfo binominalOperation,
			final ICFGNodeFactory nodeFactory) {

		final T core = this.getCore();
		final ExpressionInfo firstOperand = (ExpressionInfo) binominalOperation
				.getFirstOperand().copy();
		final ExpressionInfo secondOperand = (ExpressionInfo) binominalOperation
				.getSecondOperand().copy();

		final boolean firstOperandIsDissolved = CFGUtility
				.isDissolved(firstOperand);
		final boolean secondOperandIsDissolved = CFGUtility
				.isDissolved(secondOperand);

		// �����̕K�v�̂Ȃ��ꍇ�͔�����
		if (!firstOperandIsDissolved && !secondOperandIsDissolved) {
			return null;
		}

		// ����O�̕�����K�v�ȏ����擾
		final LocalSpaceInfo ownerSpace = core.getOwnerSpace();
		final ConditionalBlockInfo ownerConditionalBlock = binominalOperation
				.getOwnerConditionalBlock();
		final ExecutableElementInfo ownerExecutableElement = binominalOperation
				.getOwnerExecutableElement();
		final int fromLine = binominalOperation.getFromLine();
		final int fromColumn = binominalOperation.getFromColumn();
		final int toLine = binominalOperation.getToLine();
		final int toColumn = binominalOperation.getToColumn();
		final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
				: ownerSpace.getOuterCallableUnit();

		final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
		final LinkedList<LocalVariableUsageInfo> dissolvedVariableUsageList = new LinkedList<LocalVariableUsageInfo>();

		// firstOperand���K�v�ł���Ε���
		if (firstOperandIsDissolved) {
			this.makeDissolvedNode(firstOperand, nodeFactory,
					dissolvedNodeList, dissolvedVariableUsageList);
		}

		// secondOperand���K�v�ł���Ε���
		if (secondOperandIsDissolved) {
			this.makeDissolvedNode(secondOperand, nodeFactory,
					dissolvedNodeList, dissolvedVariableUsageList);
		}

		// �Â��m�[�h���폜
		nodeFactory.removeNode(core);
		this.remove();

		// �V�����񍀉��Z�I�u�W�F�N�g����ѐV�����v���O�����v�f�𐶐�
		int index = 0;
		final BinominalOperationInfo newBinominalOperation = new BinominalOperationInfo(
				binominalOperation.getOperator(),
				firstOperandIsDissolved ? dissolvedVariableUsageList
						.get(index++) : firstOperand,
				secondOperandIsDissolved ? dissolvedVariableUsageList
						.get(index++) : secondOperand, outerCallableUnit,
				fromLine, fromColumn, toLine, toColumn);
		newBinominalOperation.setOwnerConditionalBlock(ownerConditionalBlock);
		newBinominalOperation.setOwnerExecutableElement(ownerExecutableElement);
		final ExecutableElementInfo newElement = this.makeNewElement(
				ownerSpace, newBinominalOperation);
		final CFGNode<?> newNode = nodeFactory.makeNormalNode(newElement);
		dissolvedNodeList.add(newNode);

		// ���������m�[�h���G�b�W�łȂ�
		this.makeEdges(dissolvedNodeList);

		// ���������m�[�h�Q����CFG���\�z
		final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

		// ���������m�[�h���m�[�h�t�@�N�g����dissolvedNode�ɓo�^
		nodeFactory.addDissolvedNodes(core, newCFG.getAllNodes());

		return newCFG;
	}

	/**
	 * �E�ӂ� CallInfo<?>�ł��������𕪉����邽�߂̃��\�b�h
	 * 
	 * @param call
	 * @param nodeFactory
	 * @return
	 */
	private CFG dissolveCall(final CallInfo<? extends CallableUnitInfo> call,
			final ICFGNodeFactory nodeFactory) {

		final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
		final LinkedList<LocalVariableUsageInfo> dissolvedVariableUsageList = new LinkedList<LocalVariableUsageInfo>();

		// �����𕪉�
		final List<ExpressionInfo> newArguments = new ArrayList<ExpressionInfo>();
		for (final ExpressionInfo originalArgument : call.getArguments()) {
			final ExpressionInfo argument = (ExpressionInfo) originalArgument
					.copy();
			if (CFGUtility.isDissolved(argument)) {
				this.makeDissolvedNode(argument, nodeFactory,
						dissolvedNodeList, dissolvedVariableUsageList);
				newArguments.add(dissolvedVariableUsageList.getLast());
			} else {
				newArguments.add(argument);
			}
		}

		// ���\�b�h�Ăяo���ł���΁CqualifiedExpression�𕪉�
		final ExpressionInfo newQualifiedExpression;
		if (call instanceof MethodCallInfo) {

			final MethodCallInfo methodCall = (MethodCallInfo) call;
			final ExpressionInfo qualifiedExpression = (ExpressionInfo) methodCall
					.getQualifierExpression().copy();
			if (CFGUtility.isDissolved(qualifiedExpression)) {
				this.makeDissolvedNode(qualifiedExpression, nodeFactory,
						dissolvedNodeList, dissolvedVariableUsageList);
				newQualifiedExpression = dissolvedVariableUsageList.getLast();
			} else {
				newQualifiedExpression = qualifiedExpression;
			}
		} else {
			newQualifiedExpression = null;
		}

		// �z��R���X�g���N�^�ł���΁CindexExpression�𕪉�
		final SortedMap<Integer, ExpressionInfo> newIndexExpressions = new TreeMap<Integer, ExpressionInfo>();
		if (call instanceof ArrayConstructorCallInfo) {

			final ArrayConstructorCallInfo arrayConstructorCall = (ArrayConstructorCallInfo) call;
			for (final Entry<Integer, ExpressionInfo> entry : arrayConstructorCall
					.getIndexExpressions().entrySet()) {

				final Integer dimension = entry.getKey();
				final ExpressionInfo indexExpression = (ExpressionInfo) entry
						.getValue().copy();

				if (CFGUtility.isDissolved(indexExpression)) {
					this.makeDissolvedNode(indexExpression, nodeFactory,
							dissolvedNodeList, dissolvedVariableUsageList);
					newIndexExpressions.put(dimension,
							dissolvedVariableUsageList.getLast());
				} else {
					newIndexExpressions.put(dimension, indexExpression);
				}
			}
		}

		// �������s���Ȃ������ꍇ�͉��������ɔ�����
		if (dissolvedNodeList.isEmpty()) {
			return null;
		}

		// ����O�̕�����K�v�ȏ����擾
		final T core = this.getCore();
		final LocalSpaceInfo ownerSpace = core.getOwnerSpace();
		final ConditionalBlockInfo ownerConditionalBlock = call
				.getOwnerConditionalBlock();
		final ExecutableElementInfo ownerExecutableElement = call
				.getOwnerExecutableElement();
		final int fromLine = call.getFromLine();
		final int fromColumn = call.getFromColumn();
		final int toLine = call.getToLine();
		final int toColumn = call.getToColumn();
		final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
				: ownerSpace.getOuterCallableUnit();

		// �Â��m�[�h���폜
		nodeFactory.removeNode(core);
		this.remove();

		final CallInfo<? extends CallableUnitInfo> newCall;
		if (call instanceof MethodCallInfo) {
			final MethodCallInfo methodCall = (MethodCallInfo) call;
			newCall = new MethodCallInfo(newQualifiedExpression.getType(),
					newQualifiedExpression, methodCall.getCallee(), methodCall
							.getType(), outerCallableUnit, fromLine,
					fromColumn, toLine, toColumn);
		} else if (call instanceof ClassConstructorCallInfo) {
			final ClassConstructorCallInfo classConstructorCall = (ClassConstructorCallInfo) call;
			newCall = new ClassConstructorCallInfo(classConstructorCall
					.getType(), classConstructorCall.getCallee(),
					outerCallableUnit, fromLine, fromColumn, toLine, toColumn);
		} else if (call instanceof ArrayConstructorCallInfo) {
			final ArrayConstructorCallInfo arrayConstructorCall = (ArrayConstructorCallInfo) call;
			newCall = new ArrayConstructorCallInfo(arrayConstructorCall
					.getType(), outerCallableUnit, fromLine, fromColumn,
					toLine, toColumn);

			for (final Entry<Integer, ExpressionInfo> entry : newIndexExpressions
					.entrySet()) {
				final Integer dimension = entry.getKey();
				final ExpressionInfo newIndexExpression = entry.getValue();
				((ArrayConstructorCallInfo) newCall).addIndexExpression(
						dimension, newIndexExpression);
			}

		} else {
			throw new IllegalStateException();
		}

		// ������ǉ�
		for (final ExpressionInfo newArgument : newArguments) {
			newCall.addArgument(newArgument);
		}

		newCall.setOwnerConditionalBlock(ownerConditionalBlock);
		newCall.setOwnerExecutableElement(ownerExecutableElement);
		final ExecutableElementInfo newElement = this.makeNewElement(
				ownerSpace, newCall);
		final CFGNode<?> newNode = nodeFactory.makeNormalNode(newElement);
		dissolvedNodeList.add(newNode);

		// ���������m�[�h���G�b�W�łȂ�
		this.makeEdges(dissolvedNodeList);

		// ���������m�[�h�Q����CFG���\�z
		final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

		// ���������m�[�h���m�[�h�t�@�N�g����dissolvedNode�ɓo�^
		nodeFactory.addDissolvedNodes(core, newCFG.getAllNodes());

		return newCFG;
	}

	/**
	 * �E�ӂ� CastUsageInfo�ł��������𕪉����邽�߂̃��\�b�h
	 * 
	 * @param castUsage
	 * @param nodeFactory
	 * @return
	 */
	private CFG dissolveCastUsage(final CastUsageInfo castUsage,
			final ICFGNodeFactory nodeFactory) {

		final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
		final LinkedList<LocalVariableUsageInfo> dissolvedVariableUsageList = new LinkedList<LocalVariableUsageInfo>();

		final ExpressionInfo castedUsage = (ExpressionInfo) castUsage
				.getCastedUsage().copy();
		if (CFGUtility.isDissolved(castedUsage)) {
			this.makeDissolvedNode(castedUsage, nodeFactory, dissolvedNodeList,
					dissolvedVariableUsageList);
		}

		if (dissolvedNodeList.isEmpty()) {
			return null;
		}

		// ����O�̕�����K�v�ȏ����擾
		final T core = this.getCore();
		final LocalSpaceInfo ownerSpace = core.getOwnerSpace();
		final ConditionalBlockInfo ownerConditionalBlock = castUsage
				.getOwnerConditionalBlock();
		final ExecutableElementInfo ownerExecutableElement = castUsage
				.getOwnerExecutableElement();
		final int fromLine = castUsage.getFromLine();
		final int fromColumn = castUsage.getFromColumn();
		final int toLine = castUsage.getToLine();
		final int toColumn = castUsage.getToColumn();
		final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
				: ownerSpace.getOuterCallableUnit();

		// �Â��m�[�h���폜
		nodeFactory.removeNode(core);
		this.remove();

		// �V����CastUsageInfo����т�����E�ӂƂ��Ď���������쐬
		final CastUsageInfo newCastUsage = new CastUsageInfo(castUsage
				.getType(), dissolvedVariableUsageList.getFirst(),
				outerCallableUnit, fromLine, fromColumn, toLine, toColumn);
		newCastUsage.setOwnerConditionalBlock(ownerConditionalBlock);
		newCastUsage.setOwnerExecutableElement(ownerExecutableElement);
		final ExecutableElementInfo newElement = this.makeNewElement(
				ownerSpace, newCastUsage);
		final CFGNode<?> newNode = nodeFactory.makeNormalNode(newElement);
		dissolvedNodeList.add(newNode);

		// ���������m�[�h���G�b�W�łȂ�
		this.makeEdges(dissolvedNodeList);

		// ���������m�[�h�Q����CFG���\�z
		final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

		// ���������m�[�h���m�[�h�t�@�N�g����dissolvedNode�ɓo�^
		nodeFactory.addDissolvedNodes(core, newCFG.getAllNodes());

		return newCFG;
	}

	/**
	 * �E�ӂ�MonominalOperationInfo�ł��������𕪉����邽�߂̃��\�b�h
	 * 
	 * @param monominalOperation
	 * @param nodeFactory
	 * @return
	 */
	private CFG dissolveMonominalOperation(
			final MonominalOperationInfo monominalOperation,
			final ICFGNodeFactory nodeFactory) {

		final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
		final LinkedList<LocalVariableUsageInfo> dissolvedVariableUsageList = new LinkedList<LocalVariableUsageInfo>();

		final ExpressionInfo operand = (ExpressionInfo) monominalOperation
				.getOperand().copy();
		if (CFGUtility.isDissolved(operand)) {
			this.makeDissolvedNode(operand, nodeFactory, dissolvedNodeList,
					dissolvedVariableUsageList);
		}

		if (dissolvedNodeList.isEmpty()) {
			return null;
		}

		// ����O�̕�����K�v�ȏ����擾
		final T core = this.getCore();
		final LocalSpaceInfo ownerSpace = core.getOwnerSpace();
		final ConditionalBlockInfo ownerConditionalBlock = monominalOperation
				.getOwnerConditionalBlock();
		final ExecutableElementInfo ownerExecutableElement = monominalOperation
				.getOwnerExecutableElement();
		final int fromLine = monominalOperation.getFromLine();
		final int fromColumn = monominalOperation.getFromColumn();
		final int toLine = monominalOperation.getToLine();
		final int toColumn = monominalOperation.getToColumn();
		final boolean isPreposed = monominalOperation.isPreposed();
		final OPERATOR operator = monominalOperation.getOperator();
		final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
				: ownerSpace.getOuterCallableUnit();

		// �Â��m�[�h���폜
		nodeFactory.removeNode(core);
		this.remove();

		// �V�����񍀉��Z�I�u�W�F�N�g����т�����E�ӂƂ��Ď���������쐬
		final MonominalOperationInfo newOperation = new MonominalOperationInfo(
				dissolvedVariableUsageList.getFirst(), operator, isPreposed,
				outerCallableUnit, fromLine, fromColumn, toLine, toColumn);
		newOperation.setOwnerConditionalBlock(ownerConditionalBlock);
		newOperation.setOwnerExecutableElement(ownerExecutableElement);
		final ExecutableElementInfo newElement = this.makeNewElement(
				ownerSpace, newOperation);
		final CFGNode<?> newNode = nodeFactory.makeNormalNode(newElement);
		dissolvedNodeList.add(newNode);

		// ���������m�[�h���G�b�W�łȂ�
		this.makeEdges(dissolvedNodeList);

		// ���������m�[�h�Q����CFG���\�z
		final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

		// ���������m�[�h���m�[�h�t�@�N�g����dissolvedNode�ɓo�^
		nodeFactory.addDissolvedNodes(core, newCFG.getAllNodes());

		return newCFG;
	}

	/**
	 * �E�ӂ�ParenthesesExpressionInfo�ł��������𕪉����邽�߂̃��\�b�h
	 * 
	 * @param parenthesExpression
	 * @param nodeFactory
	 * @return
	 */
	private CFG dissolveParenthesesExpression(
			final ParenthesesExpressionInfo parenthesesExpression,
			final ICFGNodeFactory nodeFactory) {

		final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
		final LinkedList<LocalVariableUsageInfo> dissolvedVariableUsageList = new LinkedList<LocalVariableUsageInfo>();

		final ExpressionInfo innerExpression = (ExpressionInfo) parenthesesExpression
				.getParnentheticExpression().copy();
		if (CFGUtility.isDissolved(innerExpression)) {
			this.makeDissolvedNode(innerExpression, nodeFactory,
					dissolvedNodeList, dissolvedVariableUsageList);
		}

		if (dissolvedNodeList.isEmpty()) {
			return null;
		}

		// ����O�̕�����K�v�ȏ����擾
		final T core = this.getCore();
		final LocalSpaceInfo ownerSpace = core.getOwnerSpace();
		final ConditionalBlockInfo ownerConditionalBlock = parenthesesExpression
				.getOwnerConditionalBlock();
		final ExecutableElementInfo ownerExecutableElement = parenthesesExpression
				.getOwnerExecutableElement();
		final int fromLine = parenthesesExpression.getFromLine();
		final int fromColumn = parenthesesExpression.getFromColumn();
		final int toLine = parenthesesExpression.getToLine();
		final int toColumn = parenthesesExpression.getToColumn();
		final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
				: ownerSpace.getOuterCallableUnit();

		// �Â��m�[�h���폜
		nodeFactory.removeNode(core);
		this.remove();

		// �V�������ʎ��I�u�W�F�N�g����т�����E�ӂƂ��Ď���������쐬
		final ParenthesesExpressionInfo newInnerExpression = new ParenthesesExpressionInfo(
				dissolvedVariableUsageList.getFirst(), outerCallableUnit,
				fromLine, fromColumn, toLine, toColumn);
		newInnerExpression.setOwnerConditionalBlock(ownerConditionalBlock);
		newInnerExpression.setOwnerExecutableElement(ownerExecutableElement);
		final ExecutableElementInfo newElement = this.makeNewElement(
				ownerSpace, newInnerExpression);
		final CFGNode<?> newNode = nodeFactory.makeNormalNode(newElement);
		dissolvedNodeList.add(newNode);

		// ���������m�[�h���G�b�W�łȂ�
		this.makeEdges(dissolvedNodeList);

		// ���������m�[�h�Q����CFG���\�z
		final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

		// ���������m�[�h���m�[�h�t�@�N�g����dissolvedNode�ɓo�^
		nodeFactory.addDissolvedNodes(core, newCFG.getAllNodes());

		return newCFG;
	}

	/**
	 * �E�ӂ�TernaryOperationInfo�ł��������𕪉����邽�߂̃��\�b�h
	 * 
	 * @param ternaryOperation
	 * @param nodeFactory
	 * @return
	 */
	private CFG dissolveTernaryOperation(
			final TernaryOperationInfo ternaryOperation,
			final ICFGNodeFactory nodeFactory) {

		final T core = this.getCore();
		final LocalSpaceInfo ownerSpace = core.getOwnerSpace();

		final ConditionInfo condition = (ConditionInfo) ternaryOperation
				.getCondition().copy();
		final ExpressionInfo trueExpression = (ExpressionInfo) ternaryOperation
				.getTrueExpression().copy();
		final ExpressionInfo falseExpression = (ExpressionInfo) ternaryOperation
				.getFalseExpression().copy();

		final int conditionFromLine = condition.getFromLine();
		final int conditionFromColumn = condition.getFromColumn();
		final int conditionToLine = condition.getToLine();
		final int conditionToColumn = condition.getToColumn();
		final int trueExpressionFromLine = trueExpression.getFromLine();
		final int trueExpressionFromColumn = trueExpression.getFromColumn();
		final int trueExpressionToLine = trueExpression.getToLine();
		final int trueExpressionToColumn = trueExpression.getToColumn();
		final int falseExpressionFromLine = falseExpression.getFromLine();
		final int falseExpressionFromColumn = falseExpression.getFromColumn();
		final int falseExpressionToLine = falseExpression.getToLine();
		final int falseExpressionToColumn = falseExpression.getToColumn();

		// condition���č\�z
		final IfBlockInfo newIfBlock = new IfBlockInfo(conditionFromLine,
				conditionFromColumn, conditionToLine, conditionToColumn);
		newIfBlock.setOuterUnit(ownerSpace);
		final ConditionalClauseInfo newConditionalClause = new ConditionalClauseInfo(
				newIfBlock, condition, conditionFromLine, conditionFromColumn,
				conditionToLine, conditionFromColumn); // �킴�� fromColumn�ɂ��Ă���
		newIfBlock.setConditionalClause(newConditionalClause);
		condition.setOwnerConditionalBlock(newIfBlock);
		final ElseBlockInfo newElseBlock = new ElseBlockInfo(conditionFromLine,
				conditionToColumn, conditionToLine, conditionToColumn); // �킴��
		// toColumn�ɂ��Ă���
		newElseBlock.setOuterUnit(ownerSpace);
		newElseBlock.setOwnerBlock(newIfBlock);
		newIfBlock.setSequentElseBlock(newElseBlock);

		// trueExpression���č\�z
		ExecutableElementInfo trueElement = this.makeNewElement(newIfBlock,
				trueExpressionFromLine, trueExpressionFromColumn,
				trueExpressionToLine, trueExpressionToColumn, trueExpression);

		// falseExpression���č\�z
		ExecutableElementInfo falseElement = this
				.makeNewElement(newElseBlock, falseExpressionFromLine,
						falseExpressionFromColumn, falseExpressionToLine,
						falseExpressionToColumn, falseExpression);

		// ExpressionInfo�̂Ƃ���ExpressionStatementInfo�ɕϊ�
		if (trueElement instanceof ExpressionInfo) {
			trueElement = new ExpressionStatementInfo(newIfBlock,
					(ExpressionInfo) trueElement, trueElement.getFromLine(),
					trueElement.getFromColumn(), trueElement.getToLine(),
					trueElement.getFromColumn());
		}

		if (falseElement instanceof ExpressionInfo) {
			falseElement = new ExpressionStatementInfo(newElseBlock,
					(ExpressionInfo) falseElement, falseElement.getFromLine(),
					falseElement.getFromColumn(), falseElement.getToLine(),
					falseElement.getFromColumn());
		}

		newIfBlock.addStatement((StatementInfo) trueElement); // trueElement��V����if���ɒǉ�
		newElseBlock.addStatement((StatementInfo) falseElement); // falseElement��V����else���ɒǉ�

		// �Â��m�[�h���폜
		nodeFactory.removeNode(core);
		this.remove();

		// �m�[�h���쐬���C�Ȃ�
		final CFGControlNode conditionNode = nodeFactory
				.makeControlNode(condition);
		final CFGNode<?> trueNode = nodeFactory.makeNormalNode(trueElement);
		final CFGNode<?> falseNode = nodeFactory.makeNormalNode(falseElement);
		final CFGControlEdge trueEdge = new CFGControlEdge(conditionNode,
				trueNode, true);
		final CFGControlEdge falseEdge = new CFGControlEdge(conditionNode,
				falseNode, false);
		conditionNode.addForwardEdge(trueEdge);
		conditionNode.addForwardEdge(falseEdge);
		trueNode.addBackwardEdge(trueEdge);
		falseNode.addBackwardEdge(falseEdge);

		for (final CFGEdge backwardEdge : this.getBackwardEdges()) {
			final CFGNode<?> backwardNode = backwardEdge.getFromNode();
			final CFGEdge newBackwardEdge = backwardEdge
					.replaceToNode(conditionNode);
			backwardNode.addForwardEdge(newBackwardEdge);
			conditionNode.addBackwardEdge(newBackwardEdge);
		}
		for (final CFGEdge forwardEdge : this.getForwardEdges()) {
			final CFGNode<?> forwardNode = forwardEdge.getToNode();
			final CFGEdge newTrueForwardEdge = forwardEdge
					.replaceFromNode(trueNode);
			final CFGEdge newFalseForwardEdge = forwardEdge
					.replaceFromNode(falseNode);
			forwardNode.addBackwardEdge(newTrueForwardEdge);
			forwardNode.addBackwardEdge(newFalseForwardEdge);
			trueNode.addForwardEdge(newTrueForwardEdge);
			falseNode.addForwardEdge(newFalseForwardEdge);
		}

		// ���������m�[�h�Q����CFG���\�z
		final SimpleCFG newCFG = new SimpleCFG(nodeFactory);
		newCFG.addNode(conditionNode);
		newCFG.setEnterNode(conditionNode);
		newCFG.addNode(trueNode);
		newCFG.addExitNode(trueNode);
		newCFG.addNode(falseNode);
		newCFG.addExitNode(falseNode);

		// ���������m�[�h�ɂ��čċA�I�ɏ���
		final CFG conditionCFG = conditionNode.dissolve(nodeFactory);
		final CFG trueCFG = trueNode.dissolve(nodeFactory);
		final CFG falseCFG = falseNode.dissolve(nodeFactory);

		if (null != conditionCFG) {
			newCFG.removeNode(conditionNode);
			newCFG.addNodes(conditionCFG.getAllNodes());
			newCFG.setEnterNode(conditionCFG.getEnterNode());
		}

		if (null != trueCFG) {
			newCFG.removeNode(trueNode);
			newCFG.addNodes(trueCFG.getAllNodes());
			newCFG.addExitNodes(trueCFG.getExitNodes());
		}

		if (null != falseCFG) {
			newCFG.removeNode(falseNode);
			newCFG.addNodes(falseCFG.getAllNodes());
			newCFG.addExitNodes(falseCFG.getExitNodes());
		}

		// ���������m�[�h���m�[�h�t�@�N�g����dissolvedNode�ɓo�^
		nodeFactory.addDissolvedNodes(core, newCFG.getAllNodes());

		return newCFG;
	}

	/**
	 * �����ŗ^����ꂽoriginalExpression���E�ӂƂȂ��������쐬����D
	 * �쐬�����������CFG�m�[�h��dissolvedNodeList�̍Ō�ɒǉ�����C
	 * ������̍��ӂ̕ϐ��g�p�I�u�W�F�N�g��dissolvedVariableUsageList�̍Ō�ɒǉ������D
	 * 
	 * @param originalExpression
	 * @param nodeFactory
	 * @param dissolvedNodeList
	 * @param dissolvedVariableUsageList
	 */
	protected final void makeDissolvedNode(
			final ExpressionInfo originalExpression,
			final ICFGNodeFactory nodeFactory,
			final List<CFGNode<?>> dissolvedNodeList,
			final List<LocalVariableUsageInfo> dissolvedVariableUsageList) {

		final LocalSpaceInfo ownerSpace = originalExpression.getOwnerSpace();
		assert null != ownerSpace : "ownerSpace is null!";
		final CallableUnitInfo outerCallableUnit = originalExpression
				.getOwnerMethod();
		assert null != outerCallableUnit : "outerCallableUnit is null!";
		final int fromLine = originalExpression.getFromLine();
		final int fromColumn = originalExpression.getFromColumn();
		final int toLine = originalExpression.getToLine();
		final int toColumn = originalExpression.getToColumn();
		final TypeInfo type = originalExpression.getType();

		final LocalVariableInfo dummyVariable = new LocalVariableInfo(
				Collections.<ModifierInfo> emptySet(), getDummyVariableName(),
				type, ownerSpace, fromLine, fromColumn, toLine, fromColumn); // �킴��fromColumn�ɂ��Ă���
		final VariableDeclarationStatementInfo dummyVariableDeclarationStatement = new VariableDeclarationStatementInfo(
				ownerSpace, LocalVariableUsageInfo.getInstance(dummyVariable,
						false, true, outerCallableUnit, fromLine, fromColumn,
						toLine, fromColumn), originalExpression, fromLine, // �킴��fromColumn�ɂ��Ă���
				fromColumn, toLine, toColumn);
		final LocalVariableUsageInfo dummyVariableUsage = LocalVariableUsageInfo
				.getInstance(dummyVariable, true, false, outerCallableUnit,
						fromLine, fromColumn, toLine, toColumn);

		final CFGNode<?> newNode = nodeFactory
				.makeNormalNode(dummyVariableDeclarationStatement);
		dissolvedNodeList.add(newNode);
		dissolvedVariableUsageList.add(dummyVariableUsage);
	}

	/**
	 * ���������m�[�h�Ȃ��C���̏ꏊ�ɓ����
	 * 
	 * @param dissolvedNodeList
	 */
	protected final void makeEdges(
			final LinkedList<CFGNode<?>> dissolvedNodeList) {

		// ���������m�[�h���Ȃ�
		for (int i = 1; i < dissolvedNodeList.size(); i++) {
			final CFGNode<?> fromNode = dissolvedNodeList.get(i - 1);
			final CFGNode<?> toNode = dissolvedNodeList.get(i);
			final CFGEdge edge = new CFGNormalEdge(fromNode, toNode);
			fromNode.addForwardEdge(edge);
			toNode.addBackwardEdge(edge);
		}

		// ���̏ꏊ�ɓ����
		{
			final CFGNode<?> firstNode = dissolvedNodeList.getFirst();
			final CFGNode<?> lastNode = dissolvedNodeList.getLast();
			for (final CFGEdge backwardEdge : this.getBackwardEdges()) {
				final CFGNode<?> backwardNode = backwardEdge.getFromNode();
				if (!this.equals(backwardNode)) {
					final CFGEdge newBackwardEdge = backwardEdge
							.replaceToNode(firstNode);
					backwardNode.addForwardEdge(newBackwardEdge);
					firstNode.addBackwardEdge(newBackwardEdge);
				} else {
					final CFGEdge newBackwardEdge = backwardEdge
							.replaceBothNodes(lastNode, firstNode);
					lastNode.addForwardEdge(newBackwardEdge);
					firstNode.addBackwardEdge(newBackwardEdge);
				}
			}
			for (final CFGEdge forwardEdge : this.getForwardEdges()) {
				final CFGNode<?> forwardNode = forwardEdge.getToNode();
				if (!this.equals(forwardNode)) {
					final CFGEdge newForwardEdge = forwardEdge
							.replaceFromNode(lastNode);
					forwardNode.addBackwardEdge(newForwardEdge);
					lastNode.addForwardEdge(newForwardEdge);
				} else {
					final CFGEdge newForwardEdge = forwardEdge
							.replaceBothNodes(lastNode, firstNode);
					lastNode.addForwardEdge(newForwardEdge);
					firstNode.addBackwardEdge(newForwardEdge);
				}
			}
		}
	}

	/**
	 * �����ŗ^����ꂽ�m�[�h�Q����CFG���\�z���ĕԂ��D
	 * 
	 * @param nodeFactory
	 * @param dissolvedNodeList
	 * @return
	 */
	protected final CFG makeCFG(final ICFGNodeFactory nodeFactory,
			LinkedList<CFGNode<?>> dissolvedNodeList) {

		final SimpleCFG cfg = new SimpleCFG(nodeFactory);

		// enterNode��ݒ�
		{
			final CFGNode<?> firstNode = dissolvedNodeList.getFirst();
			final CFG dissolvedCFG = firstNode.dissolve(nodeFactory);
			if (null != dissolvedCFG) {
				cfg.setEnterNode(dissolvedCFG.getEnterNode());
				cfg.addNodes(dissolvedCFG.getAllNodes());
			} else {
				cfg.setEnterNode(firstNode);
				cfg.addNode(firstNode);
			}
		}

		// exitNode��ݒ�
		{
			final CFGNode<?> lastNode = dissolvedNodeList.getLast();
			final CFG dissolvedCFG = lastNode.dissolve(nodeFactory);
			if (null != dissolvedCFG) {
				cfg.addExitNodes(dissolvedCFG.getExitNodes());
				cfg.addNodes(dissolvedCFG.getAllNodes());
			} else {
				cfg.addExitNode(lastNode);
				cfg.addNode(lastNode);
			}
		}

		// nodes��ݒ�
		for (int index = 1; index < dissolvedNodeList.size() - 1; index++) {
			final CFGNode<?> node = dissolvedNodeList.get(index);
			final CFG dissolvedCFG = node.dissolve(nodeFactory);
			if (null != dissolvedCFG) {
				cfg.addNodes(dissolvedCFG.getAllNodes());
			} else {
				cfg.addNode(node);
			}
		}

		return cfg;
	}

	protected final VariableDeclarationStatementInfo makeVariableDeclarationStatement(
			final LocalSpaceInfo ownerSpace, final ExpressionInfo expression) {

		final int fromLine = expression.getFromLine();
		final int fromColumn = expression.getFromColumn();
		final int toLine = expression.getToLine();
		final int toColumn = expression.getToColumn();

		final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
				: ownerSpace.getOuterCallableUnit();

		// �킴��fromColumn�ɂ��Ă���
		final LocalVariableInfo dummyVariable = new LocalVariableInfo(
				Collections.<ModifierInfo> emptySet(), CFGNode
						.getDummyVariableName(), expression.getType(),
				ownerSpace, fromLine, fromColumn, toLine, fromColumn);

		// �킴��fromColumn�ɂ��Ă���
		final LocalVariableUsageInfo variableUsage = LocalVariableUsageInfo
				.getInstance(dummyVariable, false, true, outerCallableUnit,
						fromLine, fromColumn, toLine, fromColumn);

		final VariableDeclarationStatementInfo statement = new VariableDeclarationStatementInfo(
				ownerSpace, variableUsage, expression, fromLine, fromColumn,
				toLine, toColumn);

		return statement;
	}
}
