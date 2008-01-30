package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsageInfo;

public interface ExpressionElement {
    
    public UnresolvedEntityUsageInfo getUsage();

}
