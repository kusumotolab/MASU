package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.Set;

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
    public final Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        return VariableUsageInfo.getUsedVariables(VariableUsageInfo.getAssignments(this.getCore()
                .getVariableUsages()));
    }

    @Override
    public final Set<VariableInfo<? extends UnitInfo>> getReferencedVariables() {
        return VariableUsageInfo.getUsedVariables(VariableUsageInfo.getReferencees(this.getCore()
                .getVariableUsages()));
    }
}
