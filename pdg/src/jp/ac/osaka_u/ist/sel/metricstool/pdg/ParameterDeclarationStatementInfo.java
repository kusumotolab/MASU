package jp.ac.osaka_u.ist.sel.metricstool.pdg;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;

/**
 * PDGParameterNodeで用いるためのみに作成したクラス
 * 
 * @author higo
 * 
 */
public class ParameterDeclarationStatementInfo extends SingleStatementInfo {

	/**
	 * 宣言されているパラメータ，位置情報を与えて初期化
	 * 
	 * @param parameter
	 *            宣言されているパラメータ
	 * @param fromLine
	 *            開始行
	 * @param fromColumn
	 *            開始列
	 * @param toLine
	 *            終了行
	 * @param toColumn
	 *            終了列
	 */
	public ParameterDeclarationStatementInfo(final ParameterInfo parameter,
			final int fromLine, final int fromColumn, final int toLine,
			final int toColumn) {

		super(parameter.getDefinitionUnit(), fromLine, fromColumn, toLine,
				toColumn);
		this.parameter = parameter;
	}

	@Override
	public String getText() {

		final StringBuilder text = new StringBuilder();
		final TypeInfo type = this.getParameter().getType();
		text.append(type.getTypeName());

		text.append(" ");

		text.append(this.getParameter().getName());

		return text.toString();
	}

	@Override
	public Set<CallInfo<?>> getCalls() {
		return CallInfo.EmptySet;
	}

	@Override
	public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
		final Set<VariableInfo<? extends UnitInfo>> variables = new HashSet<VariableInfo<? extends UnitInfo>>();
		variables.add(this.getParameter());
		return Collections.unmodifiableSet(variables);
	}

	@Override
	public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
		return VariableUsageInfo.EmptySet;
	}

	@Override
	public Set<ReferenceTypeInfo> getThrownExceptions() {
		return Collections.unmodifiableSet(new HashSet<ReferenceTypeInfo>());
	}

	public ParameterInfo getParameter() {
		return this.parameter;
	}

	private final ParameterInfo parameter;
}
