package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableUsageInfo;

/**
 * @author kou-tngt, t-miyake
 *
 */
public class FieldOrMethodElement implements IdentifierElement{

    public FieldOrMethodElement(UnresolvedEntityUsageInfo ownerUsage, String name){
        this.ownerUsage = ownerUsage;
        this.name = name;
    }
    
    public String getName() {
        return name;
    }
    
    public UnresolvedEntityUsageInfo getOwnerUsage() {
        return ownerUsage;
    }
    
    public UnresolvedTypeInfo getType() {
        return null;
    }
    
    /**
     * ‚±‚ÌƒNƒ‰ƒX‚ªŠi”[‚·‚é
     */
    public UnresolvedEntityUsageInfo getUsage() {
        return null;
    }
    
    public String[] getQualifiedName() {
        throw new UnsupportedOperationException();
    }
    
    public UnresolvedVariableUsageInfo resolveAsAssignmetedVariable(BuildDataManager buildDataManager) {
        UnresolvedFieldUsageInfo usage = new UnresolvedFieldUsageInfo(buildDataManager.getAllAvaliableNames(),ownerUsage,name, false);
        buildDataManager.addFieldAssignment(usage);
        return usage;
    }

    public IdentifierElement resolveAsCalledMethod(BuildDataManager buildDataManager) {
        return this;
    }

    public UnresolvedVariableUsageInfo resolveAsReferencedVariable(BuildDataManager buildDataManager) {
        UnresolvedFieldUsageInfo usage = new UnresolvedFieldUsageInfo(buildDataManager.getAllAvaliableNames(),ownerUsage,name, true);
        buildDataManager.addFieldReference(usage);
        return usage;
    }
    
    public UnresolvedEntityUsageInfo resolveReferencedEntityIfPossible(BuildDataManager buildDataManager) {
        throw new UnsupportedOperationException();
    }
    
    private final UnresolvedEntityUsageInfo ownerUsage;
    private final String name;
    
    
}
