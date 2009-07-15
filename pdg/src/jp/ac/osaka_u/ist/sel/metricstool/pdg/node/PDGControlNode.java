package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.edge.PDGControlDependenceEdge;


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
    public SortedSet<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        final SortedSet<VariableInfo<?>> definedVariables = new TreeSet<VariableInfo<?>>();
        definedVariables.addAll(VariableUsageInfo.getUsedVariables(VariableUsageInfo
                .getAssignments(this.getCore().getVariableUsages())));
        return definedVariables;
    }

    @Override
    public SortedSet<VariableInfo<? extends UnitInfo>> getReferencedVariables() {
        final SortedSet<VariableInfo<?>> referencedVariables = new TreeSet<VariableInfo<?>>();
        referencedVariables.addAll(VariableUsageInfo.getUsedVariables(VariableUsageInfo
                .getReferencees(this.getCore().getVariableUsages())));
        return referencedVariables;
    }

    /**
     * この制御ノードに制御されるノードを追加
     * @param controlledNode 制御されるノード
     */
    public void addControlDependingNode(final PDGNode<?> controlledNode,
            final boolean trueDependence) {
        if (null == controlledNode) {
            throw new IllegalArgumentException();
        }

        final PDGControlDependenceEdge controlFlow = new PDGControlDependenceEdge(this,
                controlledNode, trueDependence);
        this.addForwardEdge(controlFlow);
        controlledNode.addBackwardEdge(controlFlow);
    }

}
