package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;

public class UnresolvedConditionalSpaceInfo/*<ConditinalSpaceInfo>*/ extends UnresolvedLocalSpaceInfo {

	@Override
	public boolean alreadyResolved() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public UnitInfo getResolvedUnit() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public UnitInfo resolveUnit(TargetClassInfo usingClass,
			TargetMethodInfo usingMethod, ClassInfoManager classInfoManager,
			FieldInfoManager fieldInfoManager,
			MethodInfoManager methodInfoManager) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
