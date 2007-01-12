package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;

public interface IdentifierElement extends ExpressionElement{
    public String[] getQualifiedName();
    public String getName();
    public UnresolvedTypeInfo getOwnerType();
    
    public UnresolvedTypeInfo resolveAsReferencedVariable(BuildDataManager buildDataManager);
    public UnresolvedTypeInfo resolveAsAssignmetedVariable(BuildDataManager buildDataManager);
    public IdentifierElement resolveAsCalledMethod(BuildDataManager buildDataManager);
    
    public UnresolvedTypeInfo resolveReferencedEntityIfPossible(BuildDataManager buildDataManager);
}
