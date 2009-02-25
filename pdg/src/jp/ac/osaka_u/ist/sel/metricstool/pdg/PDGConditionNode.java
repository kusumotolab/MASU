package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


public class PDGConditionNode extends PDGNode<ConditionInfo> {

    public PDGConditionNode(final ConditionInfo condition) {
        super(condition);

        this.text = condition.getText() + "<" + condition.getFromLine() + ">";
    }

    @Override
    protected Set<VariableInfo<? extends UnitInfo>> extractDefinedVariables(final ConditionInfo core) {
        return VariableUsageInfo.getUsedVariables(VariableUsageInfo.getAssignments(core
                .getVariableUsages()));
    }

    @Override
    public boolean isDefine(VariableInfo<? extends UnitInfo> variable) {
        return VariableUsageInfo.getUsedVariables(
                VariableUsageInfo.getAssignments(this.getCore().getVariableUsages())).contains(
                variable);
    }

    @Override
    public boolean isReferenace(VariableInfo<? extends UnitInfo> variable) {
        return VariableUsageInfo.getUsedVariables(
                VariableUsageInfo.getReferencees(this.getCore().getVariableUsages())).contains(
                variable);
    }

}
