package jp.ac.osaka_u.ist.sdl.scdetector;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


public class VariableHashMap extends HashMap<VariableInfo<?>, Set<ExecutableElementInfo>> {

    public void makeHash(final LocalSpaceInfo localSpace) {

        for (final StatementInfo statement : localSpace.getStatements()) {

            if (statement instanceof SingleStatementInfo) {

                final Set<VariableUsageInfo<?>> variableUsages = ((SingleStatementInfo) statement)
                        .getVariableUsages();
                for (final VariableUsageInfo<?> variableUsage : variableUsages) {
                    if (Configuration.INSTANCE.getR() || variableUsage.isAssignment()) {
                        final VariableInfo<?> usedVariable = variableUsage.getUsedVariable();
                        if (!(usedVariable instanceof FieldInfo)) {
                            Set<ExecutableElementInfo> statements = INSTANCE.get(usedVariable);
                            if (null == statements) {
                                statements = new HashSet<ExecutableElementInfo>();
                                INSTANCE.put(usedVariable, statements);
                            }
                            statements.add(statement);
                        }
                    }
                }

            } else if (statement instanceof BlockInfo) {

                if (statement instanceof ConditionalBlockInfo) {
                    final ConditionInfo condition = ((ConditionalBlockInfo) statement)
                            .getConditionalClause().getCondition();
                    final Set<VariableUsageInfo<?>> variableUsages = condition.getVariableUsages();
                    for (final VariableUsageInfo<?> variableUsage : variableUsages) {
                        final VariableInfo<?> usedVariable = variableUsage.getUsedVariable();
                        if (!(usedVariable instanceof FieldInfo)) {
                            Set<ExecutableElementInfo> statements = INSTANCE.get(usedVariable);
                            if (null == statements) {
                                statements = new HashSet<ExecutableElementInfo>();
                                INSTANCE.put(usedVariable, statements);
                            }
                            statements.add(condition);
                        }
                    }
                }

                INSTANCE.makeHash((BlockInfo) statement);
            }
        }
    }

    private VariableHashMap() {
        super();
    }

    public static final VariableHashMap INSTANCE = new VariableHashMap();
}
