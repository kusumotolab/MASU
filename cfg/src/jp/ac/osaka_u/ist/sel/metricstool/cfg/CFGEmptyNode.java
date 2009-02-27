package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;


final class CFGEmptyNode extends CFGNode<ExecutableElementInfo> {

    CFGEmptyNode(final ExecutableElementInfo statement) {
        super(statement);
        this.text = statement.getText() + "<" + statement.getFromLine() + ">";
    }

    @Override
    public void addForwardNode(final CFGNode<? extends ExecutableElementInfo> forwardNode) {
        for (final CFGNode<? extends ExecutableElementInfo> backward : this.getBackwardNodes()) {
            backward.addForwardNode(forwardNode);
        }
    }

    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        return new HashSet<VariableInfo<? extends UnitInfo>>();
    }

    @Override
    public Set<VariableInfo<? extends UnitInfo>> getUsedVariables() {
        return new HashSet<VariableInfo<? extends UnitInfo>>();
    }

}
