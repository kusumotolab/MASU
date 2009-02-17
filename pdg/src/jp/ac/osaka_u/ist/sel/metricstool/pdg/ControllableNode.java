package jp.ac.osaka_u.ist.sel.metricstool.pdg;

import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;

public abstract class ControllableNode<T extends StatementInfo> extends PDGNode<T> {

    private final SortedSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> references;
   
    public ControllableNode(T core) {
        super(core);
        
        this.references = this.extractVariableReference(core);
    }
    
    protected abstract SortedSet<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> extractVariableReference(T core);
    
    @Override
    public boolean isDefine(final VariableInfo<? extends UnitInfo> variable) {
        return this.getDefinedVariables().contains(variable);
    }
    
    @Override
    public boolean isReferenace(final VariableInfo<? extends UnitInfo> variable) {
        for (final VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>> usage : this.references) {
            if (usage.getUsedVariable().equals(variable)) {
                return true;
            }
        }
        return false;
    }
    
}
