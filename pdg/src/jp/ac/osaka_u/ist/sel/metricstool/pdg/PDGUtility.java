package jp.ac.osaka_u.ist.sel.metricstool.pdg;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;

public class PDGUtility {

	static public ConditionalBlockInfo getOwnerConditionalBlock(
			final ConditionInfo condition) {

		final LocalSpaceInfo outerSpace = condition.getOwnerSpace();
		for (final StatementInfo statement : outerSpace.getStatements()) {

			if (statement instanceof ConditionalBlockInfo) {
				final ConditionInfo target = ((ConditionalBlockInfo) statement)
						.getConditionalClause().getCondition();
				if (condition.equals(target)) {
					return (ConditionalBlockInfo) statement;
				}
			}
		}

		throw new IllegalStateException();
	}
}
