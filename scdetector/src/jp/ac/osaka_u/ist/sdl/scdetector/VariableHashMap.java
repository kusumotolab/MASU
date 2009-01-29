package jp.ac.osaka_u.ist.sdl.scdetector;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


public class VariableHashMap extends HashMap<VariableInfo<?>, Set<ExecutableElementInfo>> {

    public void makeHash(final LocalSpaceInfo localSpace) {

        for (final StatementInfo statement : localSpace.getStatements()) {

            if (statement instanceof SingleStatementInfo) {

                this.makeVariableHashMap(statement);

            } else if (statement instanceof BlockInfo) {

                if (statement instanceof ConditionalBlockInfo) {

                    // 条件付きブロック文の条件を登録
                    {
                        final ConditionInfo condition = ((ConditionalBlockInfo) statement)
                                .getConditionalClause().getCondition();
                        this.makeVariableHashMap(condition);
                    }

                    // For文の場合は初期化式，繰り返し式を登録
                    if (statement instanceof ForBlockInfo) {

                        final SortedSet<ConditionInfo> initializerExpressions = ((ForBlockInfo) statement)
                                .getInitializerExpressions();
                        for (final ConditionInfo initializerExpression : initializerExpressions) {
                            this.makeVariableHashMap(initializerExpression);
                        }

                        final SortedSet<ExpressionInfo> iteratorExpressions = ((ForBlockInfo) statement)
                                .getIteratorExpressions();
                        for (final ConditionInfo iteratorExpression : iteratorExpressions) {
                            this.makeVariableHashMap(iteratorExpression);
                        }
                    }
                }

                INSTANCE.makeHash((BlockInfo) statement);
            }
        }
    }

    /**
     * 引数で与えられたExecutableElementInfoで使用されている変数と，ExecutableElementInfoとのマップを作成する
     * 
     * @param element
     */
    private void makeVariableHashMap(final ExecutableElementInfo element) {

        if (element instanceof BlockInfo) {
            throw new IllegalArgumentException();
        }

        final Set<VariableUsageInfo<?>> variableUsages = element.getVariableUsages();
        for (final VariableUsageInfo<?> variableUsage : variableUsages) {
            if (Configuration.INSTANCE.getR() || variableUsage.isAssignment()) {
                final VariableInfo<?> usedVariable = variableUsage.getUsedVariable();
                if (!(usedVariable instanceof FieldInfo)) {
                    Set<ExecutableElementInfo> elements = INSTANCE.get(usedVariable);
                    if (null == elements) {
                        elements = new HashSet<ExecutableElementInfo>();
                        INSTANCE.put(usedVariable, elements);
                    }
                    elements.add(element);
                }
            }
        }
    }

    private VariableHashMap() {
        super();
    }

    public static final VariableHashMap INSTANCE = new VariableHashMap();
}
