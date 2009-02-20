package jp.ac.osaka_u.ist.sdl.scdetector;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;


public class NormalizedStatementHashMap extends HashMap<Integer, List<StatementInfo>> {

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

        List<StatementInfo> statements = this.get(hash);
        if (null == statements) {
            statements = new LinkedList<StatementInfo>();
            this.put(hash, statements);
        }
        statements.add(statement);
        this.statementHashMap.put(statement, hash);
    }

    private void addHash(final ConditionalBlockInfo block) {

        if (null == block) {
            throw new IllegalArgumentException();
        }

        final ConditionInfo condition = block.getConditionalClause().getCondition();
        final int hash = Conversion.getNormalizedString(condition).hashCode();

        List<StatementInfo> statements = this.get(hash);
        if (null == statements) {
            statements = new ArrayList<StatementInfo>();
            this.put(hash, statements);
        }
        statements.add(block);
        this.statementHashMap.put(block, hash);
    }

    public NormalizedStatementHashMap() {
        super();
        this.statementHashMap = new HashMap<StatementInfo, Integer>();
    }

    private HashMap<StatementInfo, Integer> statementHashMap;
}
