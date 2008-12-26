package jp.ac.osaka_u.ist.sdl.scdetector;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;


public class NormalizedElementHashMap extends HashMap<Integer, List<ExecutableElementInfo>> {

    public void makeHash(final LocalSpaceInfo localSpace) {

        for (final StatementInfo statement : localSpace.getStatements()) {

            if (statement instanceof SingleStatementInfo) {

                final String normalizedStatement = Conversion
                        .getNormalizedString((SingleStatementInfo) statement);
                final int hash = normalizedStatement.hashCode();

                List<ExecutableElementInfo> statements = INSTANCE.get(hash);
                if (null == statements) {
                    statements = new ArrayList<ExecutableElementInfo>();
                    INSTANCE.put(hash, statements);
                }
                statements.add(statement);
                this.elementHashMap.put(statement, hash);

            } else if (statement instanceof BlockInfo) {

                // ConditionalBlockInfo　であるならば，その条件文をハッシュ化する
                if (statement instanceof ConditionalBlockInfo) {
                    final ConditionInfo condition = ((ConditionalBlockInfo) statement)
                            .getConditionalClause().getCondition();
                    final String normalizedCondition = Conversion.getNormalizedString(condition);
                    final int hash = normalizedCondition.hashCode();

                    List<ExecutableElementInfo> statements = INSTANCE.get(hash);
                    if (null == statements) {
                        statements = new ArrayList<ExecutableElementInfo>();
                        INSTANCE.put(hash, statements);
                    }
                    statements.add(condition);
                    this.elementHashMap.put(condition, hash);
                }

                INSTANCE.makeHash((BlockInfo) statement);
            }
        }
    }

    public int getHash(final ExecutableElementInfo element) {
        return this.elementHashMap.get(element);
    }

    private NormalizedElementHashMap() {
        super();
        this.elementHashMap = new HashMap<ExecutableElementInfo, Integer>();
    }

    public static final NormalizedElementHashMap INSTANCE = new NormalizedElementHashMap();

    private HashMap<ExecutableElementInfo, Integer> elementHashMap;
}
