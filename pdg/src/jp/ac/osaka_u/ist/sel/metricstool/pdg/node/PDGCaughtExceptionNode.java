package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CaughtExceptionDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


public class PDGCaughtExceptionNode extends PDGNormalNode<CaughtExceptionDeclarationStatementInfo> {

    public PDGCaughtExceptionNode(final CaughtExceptionDeclarationStatementInfo exception) {

        if (null == exception) {
            throw new IllegalArgumentException();
        }

        this.core = exception;
        this.text = exception.getText() + " <" + exception.getFromLine() + ">";
    }

    @Override
    public SortedSet<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        final SortedSet<VariableInfo<? extends UnitInfo>> definedVariables = new TreeSet<VariableInfo<? extends UnitInfo>>();
        definedVariables.addAll(this.core.getDefinedVariables());
        return Collections.unmodifiableSortedSet(definedVariables);
    }

    @Override
    public SortedSet getReferencedVariables() {
        final SortedSet<VariableInfo<? extends UnitInfo>> referencedVariables = new TreeSet<VariableInfo<? extends UnitInfo>>();
        referencedVariables.addAll(VariableUsageInfo.getUsedVariables(VariableUsageInfo
                .getReferencees(this.getCore().getVariableUsages())));
        return referencedVariables;
    }
}
