package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


/**
 * CFGのConditionInfoのノードを表すクラス
 * @author higo
 *
 */
public class CFGConditionNode extends CFGNode<ConditionInfo> {

    /**
     * 生成するノードに対応するConditionInfoを与えて初期化
     * @param condition 生成するノードに対応するConditionInfo
     */
    public CFGConditionNode(final ConditionInfo condition) {
        super(condition);
        this.text = condition.getText() + "<" + condition.getFromLine() + ">";
    }

    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        return VariableUsageInfo.getUsedVariables(VariableUsageInfo.getAssignments(this.getCore()
                .getVariableUsages()));
    }

    @Override
    public Set<VariableInfo<? extends UnitInfo>> getUsedVariables() {
        return VariableUsageInfo.getUsedVariables(VariableUsageInfo.getReferencees(this.getCore()
                .getVariableUsages()));
    }

}
