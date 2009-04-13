package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
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
