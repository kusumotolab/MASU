package jp.ac.osaka_u.ist.sel.metricstool.cfg;

import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;

public class CFGUtility {

	/**
	 * このメソッド呼び出しが，オブジェクトの状態を変更しているかを返す． 現在のところ，変更しているのは下記のいずれかの条件を満たすとき 1.
	 * フィールドに対して代入処理を行っている． 2. フィールドに張り付いたメソッド呼び出しがオブジェクトの状態を変更している．
	 * 
	 * @return　変更しているときはtrue, 変更していない場合はfalse．
	 */
	static public boolean stateChange(final MethodInfo method) {

		// フィールドに対して代入処理があるかどうかを調べる
		for (final VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>> variableUsage : method
				.getVariableUsages()) {
			final VariableInfo<?> variable = variableUsage.getUsedVariable();
			if (variable instanceof FieldInfo) {
				return true;
			}
		}

		// メソッド呼び出しについて，オブジェクトの内容が変化しているかを調べる
		final Set<MethodInfo> checkedMethods = new HashSet<MethodInfo>();
		checkedMethods.add(method);
		for (final CallInfo<? extends CallableUnitInfo> call : method
				.getCalls()) {
			if (call instanceof MethodCallInfo) {
				final MethodCallInfo methodCall = (MethodCallInfo) call;
				final ExpressionInfo qualifier = methodCall
						.getQualifierExpression();
				if (qualifier instanceof VariableUsageInfo<?>) {
					if (stateChange(methodCall.getCallee(), checkedMethods)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	static private boolean stateChange(final MethodInfo method,
			final Set<MethodInfo> checkedMethods) {

		if (checkedMethods.contains(method)) {
			return false;
		} else {
			checkedMethods.add(method);
		}

		// フィールドに対して代入処理があるかどうかを調べる
		for (final VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>> variableUsage : method
				.getVariableUsages()) {
			final VariableInfo<?> variable = variableUsage.getUsedVariable();
			if (variable instanceof FieldInfo) {
				return true;
			}
		}

		// メソッド呼び出しについて，オブジェクトの内容が変化しているかを調べる
		for (final CallInfo<? extends CallableUnitInfo> call : method
				.getCalls()) {
			if (call instanceof MethodCallInfo) {
				final MethodCallInfo methodCall = (MethodCallInfo) call;
				final ExpressionInfo qualifier = methodCall
						.getQualifierExpression();
				if (qualifier instanceof VariableUsageInfo<?>) {
					if (stateChange(methodCall.getCallee(), checkedMethods)) {
						return true;
					}
				}
			}
		}

		return false;
	}
}
