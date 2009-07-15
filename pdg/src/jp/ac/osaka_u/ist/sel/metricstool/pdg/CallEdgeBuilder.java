package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CatchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.DoBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FinallyBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LabelInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SimpleBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SwitchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SynchronizedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TryBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.WhileBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;


public class CallEdgeBuilder {

    public CallEdgeBuilder(final InterProceduralPDG pdg) {
        this.pdg = pdg;
    }

    public void addCallEdges() {

        final CallableUnitInfo unit = this.pdg.getMethodInfo();
        for (final StatementInfo statement : unit.getStatements()) {
            this.addCallEdges(statement);
        }
    }

    private void addCallEdges(final StatementInfo statement) {

        if (statement instanceof CatchBlockInfo) {
            this.addCallEdges((CatchBlockInfo) statement);
        } else if (statement instanceof DoBlockInfo) {
            this.addCallEdges((DoBlockInfo) statement);
        } else if (statement instanceof ElseBlockInfo) {
            this.addCallEdges((ElseBlockInfo) statement);
        } else if (statement instanceof FinallyBlockInfo) {
            this.addCallEdges((FinallyBlockInfo) statement);
        } else if (statement instanceof ForBlockInfo) {
            this.addCallEdges((ForBlockInfo) statement);
        } else if (statement instanceof ForeachBlockInfo) {
            this.addCallEdges((ForeachBlockInfo) statement);
        } else if (statement instanceof IfBlockInfo) {
            this.addCallEdges((IfBlockInfo) statement);
        } else if (statement instanceof SimpleBlockInfo) {
            this.addCallEdges((SimpleBlockInfo) statement);
        } else if (statement instanceof SwitchBlockInfo) {
            this.addCallEdges((SwitchBlockInfo) statement);
        } else if (statement instanceof SynchronizedBlockInfo) {
            this.addCallEdges((SynchronizedBlockInfo) statement);
        } else if (statement instanceof TryBlockInfo) {
            this.addCallEdges((TryBlockInfo) statement);
        } else if (statement instanceof WhileBlockInfo) {
            this.addCallEdges((WhileBlockInfo) statement);
        } else if (statement instanceof CaseEntryInfo) {
            this.addCallEdges((CaseEntryInfo) statement);
        } else if (statement instanceof LabelInfo) {
            this.addCallEdges((LabelInfo) statement);
        } else if (statement instanceof SingleStatementInfo) {
            this.addCallEdges((SingleStatementInfo) statement);
        } else if (statement instanceof ConditionInfo) {
            this.addCallEdges((ConditionInfo) statement);
        } else {
            throw new IllegalStateException();
        }
    }

    private void addCallEdges(final CatchBlockInfo catchBlock) {
        for (final StatementInfo statement : catchBlock.getStatements()) {
            this.addCallEdges(statement);
        }
    }

    private void addCallEdges(final DoBlockInfo doBlock) {
        for (final StatementInfo statement : doBlock.getStatements()) {
            this.addCallEdges(statement);
        }
    }

    private void addCallEdges(final ElseBlockInfo elseBlock) {
        for (final StatementInfo statement : elseBlock.getStatements()) {
            this.addCallEdges(statement);
        }
    }

    private void addCallEdges(final FinallyBlockInfo finallyBlock) {
        for (final StatementInfo statement : finallyBlock.getStatements()) {
            this.addCallEdges(statement);
        }
    }

    private void addCallEdges(final ForBlockInfo forBlock) {
        for (final ConditionInfo expression : forBlock.getInitializerExpressions()) {
            this.addCallEdges(expression);
        }
        this.addCallEdges(forBlock.getConditionalClause().getCondition());
        for (final ExpressionInfo expression : forBlock.getIteratorExpressions()) {
            this.addCallEdges(expression);
        }
        for (final StatementInfo statement : forBlock.getStatements()) {
            this.addCallEdges(statement);
        }
    }

    private void addCallEdges(final ForeachBlockInfo foreachBlock) {
        this.addCallEdges(foreachBlock.getIteratorExpression());
        for (final StatementInfo statement : foreachBlock.getStatements()) {
            this.addCallEdges(statement);
        }
    }

    private void addCallEdges(final IfBlockInfo ifBlock) {
        this.addCallEdges(ifBlock.getConditionalClause().getCondition());
        for (final StatementInfo statement : ifBlock.getStatements()) {
            this.addCallEdges(statement);
        }
    }

    private void addCallEdges(final SimpleBlockInfo simpleBlock) {
        for (final StatementInfo statement : simpleBlock.getStatements()) {
            this.addCallEdges(statement);
        }
    }

    private void addCallEdges(final SwitchBlockInfo switchBlock) {
        this.addCallEdges(switchBlock.getConditionalClause().getCondition());
        for (final StatementInfo statement : switchBlock.getStatements()) {
            this.addCallEdges(statement);
        }
    }

    private void addCallEdges(final SynchronizedBlockInfo synchronizedBlock) {
        this.addCallEdges(synchronizedBlock.getSynchronizedExpression());
        for (final StatementInfo statement : synchronizedBlock.getStatements()) {
            this.addCallEdges(statement);
        }
    }

    private void addCallEdges(final TryBlockInfo tryBlock) {
        for (final StatementInfo statement : tryBlock.getStatements()) {
            this.addCallEdges(statement);
        }
    }

    private void addCallEdges(final WhileBlockInfo whileBlock) {
        this.addCallEdges(whileBlock.getConditionalClause().getCondition());
        for (final StatementInfo statement : whileBlock.getStatements()) {
            this.addCallEdges(statement);
        }
    }

    private void addCallEdges(final CaseEntryInfo caseEntry) {
    }

    private void addCallEdges(final LabelInfo label) {
    }

    private void addCallEdges(final SingleStatementInfo statement) {
        for (final CallInfo<?> call : statement.getCalls()) {
            final CallableUnitInfo callee = call.getCallee();
            final Set<CallableUnitInfo> callees = new HashSet<CallableUnitInfo>();
            callees.add(callee);
            if (callee instanceof MethodInfo) {
                callees.addAll(((MethodInfo) callee).getOverriders());
            }
            for (final CallableUnitInfo unit : callees) {
                if (unit instanceof TargetMethodInfo || unit instanceof TargetConstructorInfo) {
                    if (((TargetClassInfo) unit.getOwnerClass()).isInterface()) {
                        continue;
                    }
                    final PDG pdg = InterProceduralPDG.PDG_MAP.get(unit);
                    assert null != pdg : "Illegal State!";

                    final PDGNode<?> fromNode = this.pdg.nodeFactory.getNode(statement);
                    for (final PDGNode<?> toNode : pdg.getEnterNodes()) {
                        fromNode.addCallDependingNode(toNode, call);
                    }
                }
            }
        }
    }

    private void addCallEdges(final ConditionInfo condition) {
        for (final CallInfo<?> call : condition.getCalls()) {
            final CallableUnitInfo callee = call.getCallee();
            final Set<CallableUnitInfo> callees = new HashSet<CallableUnitInfo>();
            callees.add(callee);
            if (callee instanceof MethodInfo) {
                callees.addAll(((MethodInfo) callee).getOverriders());
            }
            for (final CallableUnitInfo unit : callees) {
                if (unit instanceof TargetMethodInfo || unit instanceof TargetConstructorInfo) {
                    if (((TargetClassInfo) unit.getOwnerClass()).isInterface()) {
                        continue;
                    }
                    final PDG pdg = InterProceduralPDG.PDG_MAP.get(unit);
                    assert null != pdg : "Illegal State!";

                    final PDGNode<?> fromNode = this.pdg.nodeFactory.getNode(condition);
                    for (final PDGNode<?> toNode : pdg.getEnterNodes()) {
                        fromNode.addCallDependingNode(toNode, call);
                    }
                }
            }
        }
    }

    private final InterProceduralPDG pdg;
}
