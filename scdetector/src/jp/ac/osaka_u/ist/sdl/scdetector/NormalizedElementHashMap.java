package jp.ac.osaka_u.ist.sdl.scdetector;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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

    public void addStatement(final LocalSpaceInfo localSpace) {

        for (final StatementInfo statement : localSpace.getStatements()) {

            if (statement instanceof SingleStatementInfo) {

                this.addHash((SingleStatementInfo) statement);

            } else if (statement instanceof BlockInfo) {

                // ConditionalBlockInfo　であるならば，その条件文をハッシュ化する
                if (statement instanceof ConditionalBlockInfo) {

                    this.addHash((ConditionalBlockInfo) statement);
                }

                this.addStatement((BlockInfo) statement);
            }
        }
    }

    public int getHash(final StatementInfo element) {
        return this.statementHashMap.get(element);
    }

    private void addHash(final SingleStatementInfo statement) {

        if (null == statement) {
            throw new IllegalArgumentException();
        }

        final int hash = Conversion.getNormalizedString(statement).hashCode();

        List<ExecutableElementInfo> elements = this.get(hash);
        if (null == elements) {
            elements = new LinkedList<ExecutableElementInfo>();
            this.put(hash, elements);
        }
        elements.add(statement);
        this.statementHashMap.put(statement, hash);
    }

    private void addHash(final ConditionalBlockInfo block) {

        if (null == block) {
            throw new IllegalArgumentException();
        }

        {
            final ConditionInfo condition = block.getConditionalClause().getCondition();
            final int hash = Conversion.getNormalizedString(condition).hashCode();

            List<ExecutableElementInfo> elements = this.get(hash);
            if (null == elements) {
                elements = new ArrayList<ExecutableElementInfo>();
                this.put(hash, elements);
            }
            elements.add(condition);
            this.statementHashMap.put(condition, hash);
        }

        if (block instanceof ForBlockInfo) {
            final ForBlockInfo forBlock = (ForBlockInfo) block;
            final Set<ConditionInfo> initializers = forBlock.getInitializerExpressions();
            for (final ConditionInfo initializer : initializers) {
                final int hash = Conversion.getNormalizedString(initializer).hashCode();

                List<ExecutableElementInfo> elements = this.get(hash);
                if (null == elements) {
                    elements = new ArrayList<ExecutableElementInfo>();
                    this.put(hash, elements);
                }
            }

            final Set<ExpressionInfo> iterators = forBlock.getIteratorExpressions();
            for (final ExpressionInfo iterator : iterators) {
                final int hash = Conversion.getNormalizedString(iterator).hashCode();

                List<ExecutableElementInfo> elements = this.get(hash);
                if (null == elements) {
                    elements = new ArrayList<ExecutableElementInfo>();
                    this.put(hash, elements);
                }
            }
        }
    }

    public NormalizedElementHashMap() {
        super();
        this.statementHashMap = new HashMap<ExecutableElementInfo, Integer>();
    }

    private HashMap<ExecutableElementInfo, Integer> statementHashMap;
}
