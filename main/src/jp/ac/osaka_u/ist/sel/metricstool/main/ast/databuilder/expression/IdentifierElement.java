package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableUsageInfo;

public interface IdentifierElement extends ExpressionElement{
    public String[] getQualifiedName();
    public String getName();
    public UnresolvedEntityUsageInfo getOwnerUsage();
    
    public UnresolvedVariableUsageInfo resolveAsReferencedVariable(BuildDataManager buildDataManager);
    public UnresolvedVariableUsageInfo resolveAsAssignmetedVariable(BuildDataManager buildDataManager);
    public IdentifierElement resolveAsCalledMethod(BuildDataManager buildDataManager);
    
    public UnresolvedEntityUsageInfo resolveReferencedEntityIfPossible(BuildDataManager buildDataManager);
}
