package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMethodCall;

public class MethodCallElement implements ExpressionElement{
    public MethodCallElement(UnresolvedMethodCall methodCall){
        this.methodCall = methodCall;
    }
    
    public UnresolvedMethodCall getType(){
        return this.methodCall;
    }
    
    private final UnresolvedMethodCall methodCall;
}
