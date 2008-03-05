package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElement;

public class JavaArrayInstantiationElement extends ExpressionElement{

    private final static JavaArrayInstantiationElement INSTANCE = new JavaArrayInstantiationElement();
    
    public static JavaArrayInstantiationElement getInstance(){
        return INSTANCE;
    }
    
    private JavaArrayInstantiationElement(){}
}
