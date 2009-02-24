package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;


public class PDGParameterNode extends PDGNode<ParameterInfo> {

    public PDGParameterNode(final ParameterInfo parameter) {
        super(parameter);
        
        this.text = parameter.getType().getTypeName() + " " + parameter.getName();
    }

    @Override
    protected Set<VariableInfo<? extends UnitInfo>> extractDefinedVariables(ParameterInfo core) {
        final Set<VariableInfo<? extends UnitInfo>> definedVariables = new HashSet<VariableInfo<? extends UnitInfo>>();
        definedVariables.add(core);
        return definedVariables;
    }
    
    @Override
    public boolean isDefine(VariableInfo<? extends UnitInfo> variable) {
        return this.getCore().equals(variable);
    }
    
    @Override
    public boolean isReferenace(VariableInfo<? extends UnitInfo> variable) {
        return false;
    }
}
