package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


/**
 * PDG上で単文を表すノード
 * 
 * @author t-miyake
 *
 */
public class PDGStatementNode extends ControllableNode<SingleStatementInfo> {

    public PDGStatementNode(final SingleStatementInfo statement) {
        super(statement);        
        this.text = statement.getText() + "<" + statement.getFromLine() + ">";
    }

    @Override
    protected SortedSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> extractVariableReference(
            SingleStatementInfo core) {
        final SortedSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> references = new TreeSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>>();

        for (VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>> usage : core
                .getVariableUsages()) {
            if (usage.isReference()) {
                references.add(usage);
            }
        }
        
        return references;
    }

    @Override
    protected Set<VariableInfo<? extends UnitInfo>> extractDefinedVariables(SingleStatementInfo core) {
        final Set<VariableInfo<? extends UnitInfo>> definedVariables = new HashSet<VariableInfo<? extends UnitInfo>>();

        for (VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>> usage : core
                .getVariableUsages()) {
            if (usage.isAssignment()) {
                definedVariables.add(usage.getUsedVariable());
            }
        }
        return definedVariables;
    }
}
