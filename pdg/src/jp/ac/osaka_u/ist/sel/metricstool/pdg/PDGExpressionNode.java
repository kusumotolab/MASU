package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


public class PDGExpressionNode extends PDGNormalNode<ConditionInfo> {

    public PDGExpressionNode(final ConditionInfo expression) {

        if (null == expression) {
            throw new IllegalArgumentException();
        }

        this.core = expression;
        this.text = expression.getText() + " <" + expression.getFromLine() + ">";
    }

    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        return VariableUsageInfo.getUsedVariables(VariableUsageInfo.getAssignments(this.getCore()
                .getVariableUsages()));
    }

    @Override
    public Set<VariableInfo<? extends UnitInfo>> getReferencedVariables() {
        return VariableUsageInfo.getUsedVariables(VariableUsageInfo.getReferencees(this.getCore()
                .getVariableUsages()));
    }
}
