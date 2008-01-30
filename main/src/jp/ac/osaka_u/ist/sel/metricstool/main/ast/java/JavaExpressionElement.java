package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsageInfo;

public class JavaExpressionElement implements ExpressionElement{

    public static final JavaExpressionElement CLASS = new JavaExpressionElement();
    
    public static final JavaExpressionElement SUPER = new JavaExpressionElement();
    
    public UnresolvedEntityUsageInfo getUsage() {
        return null;
    }
    
}
