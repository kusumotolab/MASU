package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMemberCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMethodCallInfo;

public class MethodCallElement implements ExpressionElement{
    public MethodCallElement(UnresolvedMemberCallInfo methodCall){
        this.methodCall = methodCall;
    }
    
    public UnresolvedMethodCallInfo getType(){
        return this.methodCall;
    }
    
    private final UnresolvedMemberCallInfo methodCall;
}
