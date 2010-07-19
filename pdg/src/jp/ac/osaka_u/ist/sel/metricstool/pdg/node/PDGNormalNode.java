package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGNormalNode;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


/**
 * 制御ノード以外のノードを表す
 * 
 * @author higo
 *
 * @param <T>
 */
public abstract class PDGNormalNode<T extends CFGNormalNode<? extends ExecutableElementInfo>>
        extends PDGNode<T> {

    protected PDGNormalNode(final T node) {
        super(node);
    }

    @Override
    public SortedSet<VariableInfo<?>> getDefinedVariables() {
        final SortedSet<VariableInfo<?>> definedVariables = new TreeSet<VariableInfo<?>>();
        definedVariables.addAll(VariableUsageInfo.getUsedVariables(VariableUsageInfo
                .getAssignments(this.getCore().getVariableUsages())));
        return definedVariables;
    }

    @Override
    public SortedSet<VariableInfo<?>> getReferencedVariables() {
        final SortedSet<VariableInfo<?>> referencedVariables = new TreeSet<VariableInfo<?>>();
        referencedVariables.addAll(VariableUsageInfo.getUsedVariables(VariableUsageInfo
                .getReferencees(this.getCore().getVariableUsages())));
        return referencedVariables;
    }
}
