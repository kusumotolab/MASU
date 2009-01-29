package jp.ac.osaka_u.ist.sdl.scdetector;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;


public class NormalizedElementHashMap extends HashMap<Integer, List<ExecutableElementInfo>> {

    public void makeHash(final LocalSpaceInfo localSpace) {

        for (final StatementInfo statement : localSpace.getStatements()) {

            if (statement instanceof SingleStatementInfo) {

                this.makeNormalizedElementHashMap(statement);

            } else if (statement instanceof BlockInfo) {

                // ConditionalBlockInfo　であるならば，その条件文をハッシュ化する
                if (statement instanceof ConditionalBlockInfo) {

                    {
                        final ConditionInfo condition = ((ConditionalBlockInfo) statement)
                                .getConditionalClause().getCondition();
                        this.makeNormalizedElementHashMap(condition);
                    }

                    if (statement instanceof ForBlockInfo) {
                        final SortedSet<ConditionInfo> initializerExpressions = ((ForBlockInfo) statement)
                                .getInitializerExpressions();
                        for (final ConditionInfo initializerExpression : initializerExpressions) {
                            this.makeNormalizedElementHashMap(initializerExpression);
                        }

                        final SortedSet<ExpressionInfo> iteratorExpressions = ((ForBlockInfo) statement)
                                .getIteratorExpressions();
                        for (final ExpressionInfo iteratorExpression : iteratorExpressions) {
                            this.makeNormalizedElementHashMap(iteratorExpression);
                        }
                    }
                }

                INSTANCE.makeHash((BlockInfo) statement);
            }
        }
    }

    public int getHash(final ExecutableElementInfo element) {
        return this.elementHashMap.get(element);
    }

    private void makeNormalizedElementHashMap(final ExecutableElementInfo element) {

        final String normalizedCondition;
        if (element instanceof SingleStatementInfo) {
            normalizedCondition = Conversion.getNormalizedString((SingleStatementInfo) element);
        } else if (element instanceof ConditionInfo) {
            normalizedCondition = Conversion.getNormalizedString((ConditionInfo) element);
        } else {
            throw new IllegalArgumentException();
        }
        final int hash = normalizedCondition.hashCode();

        List<ExecutableElementInfo> statements = INSTANCE.get(hash);
        if (null == statements) {
            statements = new ArrayList<ExecutableElementInfo>();
            INSTANCE.put(hash, statements);
        }
        statements.add(element);
        this.elementHashMap.put(element, hash);
    }

    private NormalizedElementHashMap() {
        super();
        this.elementHashMap = new HashMap<ExecutableElementInfo, Integer>();
    }

    public static final NormalizedElementHashMap INSTANCE = new NormalizedElementHashMap();

    private HashMap<ExecutableElementInfo, Integer> elementHashMap;
}
