package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsage;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldUsage;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;

public class CompoundIdentifierElement implements IdentifierElement{
    
    public CompoundIdentifierElement(IdentifierElement owner,String name){
        this.name = name;
        this.owner = owner;
        
        String[] ownerName = owner.getQualifiedName();
        String[] thisName = new String[ownerName.length+1];
        System.arraycopy(ownerName, 0, thisName, 0, ownerName.length);
        thisName[thisName.length-1] = name;
        qualifiedName = thisName;
    }
    
    public String[] getQualifiedName(){
        return qualifiedName;
    }
    
    public String getName(){
        return this.name;
    }
    
    public ExpressionElement getOwner(){
        return owner;
    }
    
    public UnresolvedTypeInfo getType(){
        return null;
    }
    
    public UnresolvedTypeInfo getOwnerType() {
        return ownerType;
    }

    public UnresolvedTypeInfo resolveAsAssignmetedVariable(BuildDataManager buildDataManager) {
        this.ownerType = resolveOwner(buildDataManager);
        UnresolvedFieldUsage fieldUsage = new UnresolvedFieldUsage(buildDataManager.getAvailableNameSpaceSet(),ownerType,name);
        buildDataManager.addFieldAssignment(fieldUsage);
        return fieldUsage;
    }

    public IdentifierElement resolveAsCalledMethod(BuildDataManager buildDataManager) {
        this.ownerType = resolveOwner(buildDataManager);
        return this;
    }

    public UnresolvedTypeInfo resolveAsReferencedVariable(BuildDataManager buildDataManager) {
        this.ownerType = resolveOwner(buildDataManager);
        UnresolvedFieldUsage fieldUsage = new UnresolvedFieldUsage(buildDataManager.getAvailableNameSpaceSet(),ownerType,name);
        buildDataManager.addFieldReference(fieldUsage);
        return fieldUsage;
    }
    
    protected UnresolvedEntityUsage resolveOwner(BuildDataManager buildDataManager){
        return new UnresolvedEntityUsage(buildDataManager.getAvailableNameSpaceSet(),owner.getQualifiedName());
    }
    
    private final String name;
    private final String[] qualifiedName;
    private final IdentifierElement owner;
    private UnresolvedTypeInfo ownerType;
}
