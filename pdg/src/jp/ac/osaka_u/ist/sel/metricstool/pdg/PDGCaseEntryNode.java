package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


/**
 * 
 * @author higo
 *
 */
public class PDGCaseEntryNode extends PDGNormalNode<CaseEntryInfo> {

    /**
     * 
     * @param entry
     */
    public PDGCaseEntryNode(final CaseEntryInfo entry) {

        if (null == entry) {
            throw new IllegalArgumentException();
        }

        this.core = entry;
        this.text = entry.getText() + " <" + entry.getFromLine() + ">";
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
