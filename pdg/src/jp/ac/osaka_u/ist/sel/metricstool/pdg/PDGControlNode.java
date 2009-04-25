package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


/**
 * PDG上で制御ノードを表すクラス
 * 
 * @author t-miyake
 *
 */
public class PDGControlNode extends PDGNode<ConditionInfo> {

    public PDGControlNode(final ConditionInfo condition) {

        if (null == condition) {
            throw new IllegalArgumentException();
        }

        this.core = condition;
        this.text = condition.getText() + " <" + condition.getFromLine() + ">";
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

    /**
     * この制御ノードに制御されるノードを追加
     * @param controlledNode 制御されるノード
     */
    public void addControlDependingNode(final PDGNode<?> controlledNode) {
        if (null == controlledNode) {
            throw new IllegalArgumentException();
        }

        final ControlDependenceEdge controlFlow = new ControlDependenceEdge(this, controlledNode);
        this.addFowardEdge(controlFlow);
        controlledNode.addBackwardEdge(controlFlow);
    }

}
