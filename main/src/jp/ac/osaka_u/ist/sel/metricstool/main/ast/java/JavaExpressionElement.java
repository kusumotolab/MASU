package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;

public class JavaExpressionElement implements ExpressionElement{

    public static final JavaExpressionElement CLASS = new JavaExpressionElement();
    
    public UnresolvedTypeInfo getType() {
        return null;
    }
}
