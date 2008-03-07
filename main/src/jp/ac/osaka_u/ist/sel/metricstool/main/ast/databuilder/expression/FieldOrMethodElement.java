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
public class FieldOrMethodElement extends IdentifierElement {

    public FieldOrMethodElement(UnresolvedEntityUsageInfo ownerUsage, String name,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        super(name, fromLine, fromColumn, toLine, toColumn);
        
        this.ownerUsage = ownerUsage;
    }

    public UnresolvedTypeInfo getType() {
        return null;
    }

    @Override
    public String[] getQualifiedName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public UnresolvedVariableUsageInfo resolveAsAssignmetedVariable(
            BuildDataManager buildDataManager) {
        UnresolvedFieldUsageInfo usage = new UnresolvedFieldUsageInfo(buildDataManager
                .getAllAvaliableNames(), this.ownerUsage, this.name, false, this.fromLine, this.fromColumn, this.toLine, this.toColumn);
        buildDataManager.addVariableUsage(usage);
        return usage;
    }

    @Override
    public IdentifierElement resolveAsCalledMethod(BuildDataManager buildDataManager) {
        return this;
    }

    @Override
    public UnresolvedVariableUsageInfo resolveAsReferencedVariable(BuildDataManager buildDataManager) {
        UnresolvedFieldUsageInfo usage = new UnresolvedFieldUsageInfo(buildDataManager
                .getAllAvaliableNames(), this.ownerUsage, this.name, true, this.fromLine, this.fromColumn, this.toLine, this.toColumn);
        buildDataManager.addVariableUsage(usage);
        return usage;
    }

    @Override
    public UnresolvedEntityUsageInfo resolveReferencedEntityIfPossible(
            BuildDataManager buildDataManager) {
        throw new UnsupportedOperationException();
    }

}
