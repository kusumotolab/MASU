package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterUsageInfo;

public class CFGParameterOutNode extends CFGDataNode<ParameterUsageInfo> {

	public static CFGParameterOutNode getInstance(final ParameterInfo parameter) {

		if (null == parameter) {
			throw new IllegalArgumentException();
		}

		final int fromLine = parameter.getToLine();
		final int fromColumn = parameter.getFromColumn();
		final int toLine = parameter.getToLine();
		final int toColumn = parameter.getToColumn();
		final CallableUnitInfo ownerUnit = parameter.getDefinitionUnit();

		final ParameterUsageInfo usage = ParameterUsageInfo.getInstance(
				parameter, true, false, ownerUnit, fromLine, fromColumn,
				toLine, toColumn);

		return new CFGParameterOutNode(usage);
	}

	private CFGParameterOutNode(final ParameterUsageInfo parameterUsage) {
		super(parameterUsage);
	}

	@Override
	final ExpressionInfo getDissolvingTarget() {
		return null;
	}

	@Override
	ParameterUsageInfo makeNewElement(final LocalSpaceInfo ownerSpace,
			final int fromLine, final int fromColumn, final int toLine,
			final int toColumn, final ExpressionInfo... requiredExpressions) {
		return null;
	}

	@Override
	ParameterUsageInfo makeNewElement(final LocalSpaceInfo ownerSpace,
			final ExpressionInfo... requiredExpressions) {
		return null;
	}
}
