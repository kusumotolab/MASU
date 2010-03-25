package jp.ac.osaka_u.ist.sel.metricstool.pdg;

import java.util.HashMap;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.ICFGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CatchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.DoBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FinallyBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SimpleBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SwitchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SynchronizedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TryBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.WhileBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGCallDependenceEdge;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;

public class InterProceduralPDG extends IntraProceduralPDG {

	public static final Map<CallableUnitInfo, InterProceduralPDG> PDG_MAP = new HashMap<CallableUnitInfo, InterProceduralPDG>();

	/**
	 * PDGを生成する
	 * 
	 * @param unit
	 *            pdgを生成するユニット
	 * @param pdgNodeFactory
	 *            PDGのノード生成に用いるファクトリ
	 * @param cfgNodeFactory
	 *            CFGのノード生成に用いるファクトリ
	 * @param buildDataDependency
	 *            Data Dependencyを生成するか？
	 * @param buildControlDependencey
	 *            Control Dependencyを生成するか？
	 * @param buildExecutionDependency
	 *            Execution Dependencyを生成するか？
	 */
	public InterProceduralPDG(final CallableUnitInfo unit,
			final IPDGNodeFactory pdgNodeFactory,
			final ICFGNodeFactory cfgNodeFactory,
			final boolean buildDataDependency,
			final boolean buildControlDependencey,
			final boolean buildExecutionDependency) {

		super(unit, pdgNodeFactory, cfgNodeFactory, buildDataDependency,
				buildControlDependencey, buildExecutionDependency);
		PDG_MAP.put(unit, this);
	}

	public InterProceduralPDG(final CallableUnitInfo unit,
			final IPDGNodeFactory pdgNodeFactory,
			final ICFGNodeFactory cfgNodeFactory) {
		super(unit, pdgNodeFactory, cfgNodeFactory);
		PDG_MAP.put(unit, this);
	}

	public InterProceduralPDG(final CallableUnitInfo unit) {
		super(unit);
		PDG_MAP.put(unit, this);
	}

	public InterProceduralPDG(final CallableUnitInfo unit,
			final boolean buildDataDependency,
			final boolean buildControlDependencey,
			final boolean buildExecutionDependency) {
		super(unit, buildDataDependency, buildControlDependencey,
				buildExecutionDependency);
		PDG_MAP.put(unit, this);
	}

	public void addCallEdges() {

		final CallableUnitInfo unit = this.getMethodInfo();
		for (final StatementInfo statement : unit.getStatements()) {

			if (statement instanceof BlockInfo) {
				this.addCallEdges(statement);
			}

			else if (statement instanceof SingleStatementInfo) {
				this.addCallEdges(statement);
			}
		}
	}

	private void addCallEdges(final StatementInfo statement) {
		throw new IllegalStateException();
	}

	private void addCallEdges(final CatchBlockInfo catchBlock) {
		for (final StatementInfo statement : catchBlock.getStatements()) {

			if (statement instanceof BlockInfo) {
				this.addCallEdges(statement);
			}

			else if (statement instanceof SingleStatementInfo) {
				this.addCallEdges(statement);
			}
		}
	}

	private void addCallEdges(final DoBlockInfo doBlock) {
		for (final StatementInfo statement : doBlock.getStatements()) {

			if (statement instanceof BlockInfo) {
				this.addCallEdges(statement);
			}

			else if (statement instanceof SingleStatementInfo) {
				this.addCallEdges(statement);
			}
		}
	}

	private void addCallEdges(final ElseBlockInfo elseBlock) {
		for (final StatementInfo statement : elseBlock.getStatements()) {

			if (statement instanceof BlockInfo) {
				this.addCallEdges(statement);
			}

			else if (statement instanceof SingleStatementInfo) {
				this.addCallEdges(statement);
			}
		}
	}

	private void addCallEdges(final FinallyBlockInfo finallyBlock) {
		for (final StatementInfo statement : finallyBlock.getStatements()) {

			if (statement instanceof BlockInfo) {
				this.addCallEdges(statement);
			}

			else if (statement instanceof SingleStatementInfo) {
				this.addCallEdges(statement);
			}
		}
	}

	private void addCallEdges(final ForBlockInfo forBlock) {

		for (final ConditionInfo expression : forBlock
				.getInitializerExpressions()) {
			this.addCallEdges(expression);
		}

		this.addCallEdges(forBlock.getConditionalClause().getCondition());

		for (final ExpressionInfo expression : forBlock
				.getIteratorExpressions()) {
			this.addCallEdges(expression);
		}

		for (final StatementInfo statement : forBlock.getStatements()) {

			if (statement instanceof BlockInfo) {
				this.addCallEdges(statement);
			}

			else if (statement instanceof SingleStatementInfo) {
				this.addCallEdges(statement);
			}
		}
	}

	private void addCallEdges(final IfBlockInfo ifBlock) {

		this.addCallEdges(ifBlock.getConditionalClause().getCondition());
		for (final StatementInfo statement : ifBlock.getStatements()) {

			if (statement instanceof BlockInfo) {
				this.addCallEdges(statement);
			}

			else if (statement instanceof SingleStatementInfo) {
				this.addCallEdges(statement);
			}
		}
	}

	private void addCallEdges(final SimpleBlockInfo simpleBlock) {
		for (final StatementInfo statement : simpleBlock.getStatements()) {

			if (statement instanceof BlockInfo) {
				this.addCallEdges(statement);
			}

			else if (statement instanceof SingleStatementInfo) {
				this.addCallEdges(statement);
			}
		}
	}

	private void addCallEdges(final SwitchBlockInfo switchBlock) {
		this.addCallEdges(switchBlock.getConditionalClause().getCondition());
		for (final StatementInfo statement : switchBlock.getStatements()) {

			if (statement instanceof BlockInfo) {
				this.addCallEdges(statement);
			}

			else if (statement instanceof SingleStatementInfo) {
				this.addCallEdges(statement);
			}
		}
	}

	private void addCallEdges(final SynchronizedBlockInfo synchronizedBlock) {
		this.addCallEdges(synchronizedBlock.getSynchronizedExpression());
		for (final StatementInfo statement : synchronizedBlock.getStatements()) {

			if (statement instanceof BlockInfo) {
				this.addCallEdges(statement);
			}

			else if (statement instanceof SingleStatementInfo) {
				this.addCallEdges(statement);
			}
		}
	}

	private void addCallEdges(final TryBlockInfo tryBlock) {
		for (final StatementInfo statement : tryBlock.getStatements()) {

			if (statement instanceof BlockInfo) {
				this.addCallEdges(statement);
			}

			else if (statement instanceof SingleStatementInfo) {
				this.addCallEdges(statement);
			}
		}
	}

	private void addCallEdges(final WhileBlockInfo whileBlock) {
		this.addCallEdges(whileBlock.getConditionalClause().getCondition());
		for (final StatementInfo statement : whileBlock.getStatements()) {

			if (statement instanceof BlockInfo) {
				this.addCallEdges(statement);
			}

			else if (statement instanceof SingleStatementInfo) {
				this.addCallEdges(statement);
			}
		}
	}

	private void addCallEdges(final SingleStatementInfo statement) {
		for (final CallInfo<?> call : statement.getCalls()) {
			final CallableUnitInfo callee = call.getCallee();
			final IntraProceduralPDG calleePDG = PDG_MAP.get(callee);
			assert null != calleePDG : "Illegal State!";

			final PDGNode<?> fromNode = this.nodeFactory.getNode(statement);
			final PDGNode<?> toNode = calleePDG.getMethodEnterNode();
			final PDGCallDependenceEdge edge = new PDGCallDependenceEdge(
					fromNode, toNode, call);
		}
	}

	private void addCallEdges(final ConditionInfo condition) {
		for (final CallInfo<?> call : condition.getCalls()) {
			final CallableUnitInfo callee = call.getCallee();
			final IntraProceduralPDG calleePDG = PDG_MAP.get(callee);
			assert null != calleePDG : "Illegal State!";

			final PDGNode<?> fromNode = this.nodeFactory.getNode(condition);
			final PDGNode<?> toNode = calleePDG.getMethodEnterNode();
			final PDGCallDependenceEdge edge = new PDGCallDependenceEdge(
					fromNode, toNode, call);
		}
	}
}
