package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldUsage;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;

public class FieldOrMethodElement implements IdentifierElement{

    public FieldOrMethodElement(UnresolvedTypeInfo ownerType, String name){
        this.ownerType = ownerType;
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public UnresolvedTypeInfo getOwnerType() {
        return ownerType;
    }
    
    public UnresolvedTypeInfo getType() {
        return null;
    }
    
    public String[] getQualifiedName() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public UnresolvedTypeInfo resolveAsAssignmetedVariable(BuildDataManager buildDataManager) {
        UnresolvedFieldUsage usage = new UnresolvedFieldUsage(buildDataManager.getAvailableNameSpaceSet(),ownerType,name);
        buildDataManager.addFieldAssignment(usage);
        return usage;
    }

    public IdentifierElement resolveAsCalledMethod(BuildDataManager buildDataManager) {
        return this;
    }

    public UnresolvedTypeInfo resolveAsReferencedVariable(BuildDataManager buildDataManager) {
        UnresolvedFieldUsage usage = new UnresolvedFieldUsage(buildDataManager.getAvailableNameSpaceSet(),ownerType,name);
        buildDataManager.addFieldReference(usage);
        return usage;
    }
    
    private final UnresolvedTypeInfo ownerType;
    private final String name;
}
