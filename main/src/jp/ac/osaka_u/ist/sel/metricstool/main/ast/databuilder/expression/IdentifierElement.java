package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableUsageInfo;


public abstract class IdentifierElement implements ExpressionElement {

    public IdentifierElement(final String name, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        
        if(null == name) {
            throw new IllegalArgumentException("name is null.");
        }
        
        this.name = name;
        
        this.ownerUsage = null;
        
        this.fromLine = fromLine;
        this.fromColumn = fromColumn;
        this.toLine = toLine;
        this.toColumn = toColumn;
    }

    public String getName() {
        return this.name;
    }

    public String[] getQualifiedName() {
        return this.qualifiedName;
    }

    public UnresolvedEntityUsageInfo getOwnerUsage() {
        return this.ownerUsage;
    }

    public abstract UnresolvedVariableUsageInfo resolveAsReferencedVariable(
            BuildDataManager buildDataManager);

    public abstract UnresolvedVariableUsageInfo resolveAsAssignmetedVariable(
            BuildDataManager buildDataManager);

    public abstract IdentifierElement resolveAsCalledMethod(BuildDataManager buildDataManager);

    public abstract UnresolvedEntityUsageInfo resolveReferencedEntityIfPossible(
            BuildDataManager buildDataManager);

    public final int getFromLine() {
        return this.fromLine;
    }
    
    public final int getFromColumn() {
        return this.fromColumn;
    }
    
    public final int getToLine() {
        return this.toLine;
    }
    
    public final int getToColumn() {
        return this.toColumn;
    }
    
    protected final String name;
    
    protected String[] qualifiedName;
    
    protected UnresolvedEntityUsageInfo ownerUsage;

    protected final int fromLine;

    protected final int fromColumn;

    protected final int toLine;

    protected final int toColumn;
}
