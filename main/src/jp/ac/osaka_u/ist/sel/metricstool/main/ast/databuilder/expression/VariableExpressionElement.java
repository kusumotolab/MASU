package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableInfo;

public class VariableExpressionElement implements ExpressionElement{

    public VariableExpressionElement(UnresolvedVariableInfo var){
        this.var = var;
    }
    
    public UnresolvedTypeInfo getType() {
        return var.getType();
    }

    private final UnresolvedVariableInfo var ;
}
