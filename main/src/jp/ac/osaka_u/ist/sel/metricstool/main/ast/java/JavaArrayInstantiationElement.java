package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;

public class JavaArrayInstantiationElement implements ExpressionElement{

    private final static JavaArrayInstantiationElement INSTANCE = new JavaArrayInstantiationElement();
    
    public static JavaArrayInstantiationElement getInstance(){
        return INSTANCE;
    }
    
    public UnresolvedEntityUsageInfo getUsage() {
        return null;
    }
    
    private JavaArrayInstantiationElement(){}
}
