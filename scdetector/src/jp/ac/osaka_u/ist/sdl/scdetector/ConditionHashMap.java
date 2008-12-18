package jp.ac.osaka_u.ist.sdl.scdetector;


import java.util.HashMap;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalClauseInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;


public class ConditionHashMap extends HashMap<ConditionInfo, ConditionalBlockInfo> {

    public void makeHash(final LocalSpaceInfo localSpace) {

        // ConditionalBlockInfoの子クラスであれば，Mapにデータを追加
        if (localSpace instanceof ConditionalBlockInfo) {
            final ConditionalBlockInfo conditionalBlockInfo = ((ConditionalBlockInfo) localSpace);
            final ConditionalClauseInfo conditionalClauseInfo = conditionalBlockInfo
                    .getConditionalClause();
            final ConditionInfo condition = conditionalClauseInfo.getCondition();
            INSTANCE.put(condition, (ConditionalBlockInfo) localSpace);
        }

        final Set<StatementInfo> innerStatements = localSpace.getStatements();
        for (final StatementInfo innerStatement : innerStatements) {
            if (innerStatement instanceof LocalSpaceInfo) {
                INSTANCE.makeHash((LocalSpaceInfo) innerStatement);
            }
        }

    }

    private ConditionHashMap() {
        super();
    }

    public static final ConditionHashMap INSTANCE = new ConditionHashMap();
}
