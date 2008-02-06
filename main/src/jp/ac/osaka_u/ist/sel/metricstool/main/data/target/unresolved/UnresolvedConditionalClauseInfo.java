package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;


public class UnresolvedConditionalClauseInfo extends
        UnresolvedLocalSpaceInfo<LocalSpaceInfo> {

    @Override
    public boolean alreadyResolved() {
        // TODO 自動生成されたメソッド・スタブ
        return false;
    }

    @Override
    public LocalSpaceInfo getResolvedUnit() {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

    @Override
    public LocalSpaceInfo resolveUnit(TargetClassInfo usingClass, CallableUnitInfo usingMethod,
            ClassInfoManager classInfoManager, FieldInfoManager fieldInfoManager,
            MethodInfoManager methodInfoManager) {
        // TODO 自動生成されたメソッド・スタブ
        return null;
    }

}
