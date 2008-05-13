package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableUsageInfo;


public abstract class IdentifierElement extends ExpressionElement {

    public IdentifierElement(final String name, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);
        
        if(null == name) {
            throw new IllegalArgumentException("name is null.");
        }
        
        this.name = name;
        
        this.ownerUsage = null;
        
    }

    public String getName() {
        return this.name;
    }

    public String[] getQualifiedName() {
        return this.qualifiedName;
    }

    public UnresolvedEntityUsageInfo<? extends EntityUsageInfo> getOwnerUsage() {
        return this.ownerUsage;
    }

    public abstract UnresolvedVariableUsageInfo<? extends VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> resolveAsReferencedVariable(
            BuildDataManager buildDataManager);

    public abstract UnresolvedVariableUsageInfo<? extends VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> resolveAsAssignmetedVariable(
            BuildDataManager buildDataManager);

    public abstract IdentifierElement resolveAsCalledMethod(BuildDataManager buildDataManager);

    public abstract UnresolvedEntityUsageInfo<? extends EntityUsageInfo> resolveReferencedEntityIfPossible(
            BuildDataManager buildDataManager);

    protected final String name;
    
    protected String[] qualifiedName;
    
    protected UnresolvedEntityUsageInfo<? extends EntityUsageInfo> ownerUsage;


}
