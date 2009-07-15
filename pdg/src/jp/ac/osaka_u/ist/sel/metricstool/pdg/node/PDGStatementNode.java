package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


/**
 * PDG上で単文を表すノード
 * 
 * @author t-miyake, higo
 *
 */
public class PDGStatementNode extends PDGNormalNode<SingleStatementInfo> {

    /**
     * @param statement
     */
    public PDGStatementNode(final SingleStatementInfo statement) {

        if (null == statement) {
            throw new IllegalArgumentException();
        }

        this.core = statement;
        this.text = statement.getText() + " <" + statement.getFromLine() + ">";
    }

    @Override
    public final SortedSet<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        final SortedSet<VariableInfo<?>> definedVariables = new TreeSet<VariableInfo<?>>();
        definedVariables.addAll(VariableUsageInfo.getUsedVariables(VariableUsageInfo
                .getAssignments(this.getCore().getVariableUsages())));
        return definedVariables;
    }

    @Override
    public final SortedSet<VariableInfo<? extends UnitInfo>> getReferencedVariables() {
        final SortedSet<VariableInfo<?>> referencedVariables = new TreeSet<VariableInfo<?>>();
        referencedVariables.addAll(VariableUsageInfo.getUsedVariables(VariableUsageInfo
                .getReferencees(this.getCore().getVariableUsages())));
        return referencedVariables;
    }
}
