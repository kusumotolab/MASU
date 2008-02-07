package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedUnknownUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableUsageInfo;


/**
 * 
 * @author kou-tngt, t-miyake
 *
 */
public class CompoundIdentifierElement implements IdentifierElement {

    public CompoundIdentifierElement(final IdentifierElement owner, final String name) {
        if (null == owner) {
            throw new NullPointerException("owner is null.");
        }
        if (null == name) {
            throw new NullPointerException("name is null.");
        }

        this.name = name;
        this.owner = owner;

        final String[] ownerName = owner.getQualifiedName();
        final String[] thisName = new String[ownerName.length + 1];
        System.arraycopy(ownerName, 0, thisName, 0, ownerName.length);
        thisName[thisName.length - 1] = name;
        this.qualifiedName = thisName;
    }

    public String[] getQualifiedName() {
        return this.qualifiedName;
    }

    public String getName() {
        return this.name;
    }

    public ExpressionElement getOwner() {
        return this.owner;
    }

    public UnresolvedTypeInfo getType() {
        return null;
    }

    public UnresolvedEntityUsageInfo getUsage() {
        return null;
    }

    public UnresolvedEntityUsageInfo getOwnerUsage() {
        return this.ownerType;
    }

    public UnresolvedVariableUsageInfo resolveAsAssignmetedVariable(
            final BuildDataManager buildDataManager) {
        this.ownerType = this.resolveOwner(buildDataManager);
        final UnresolvedFieldUsageInfo fieldUsage = new UnresolvedFieldUsageInfo(buildDataManager
                .getAllAvaliableNames(), this.ownerType, this.name, false);
        buildDataManager.addVariableUsage(fieldUsage);
        return fieldUsage;
    }

    public IdentifierElement resolveAsCalledMethod(final BuildDataManager buildDataManager) {
        this.ownerType = this.resolveOwner(buildDataManager);
        return this;
    }

    public UnresolvedVariableUsageInfo resolveAsReferencedVariable(
            final BuildDataManager buildDataManager) {
        this.ownerType = this.resolveOwner(buildDataManager);
        final UnresolvedFieldUsageInfo fieldUsage = new UnresolvedFieldUsageInfo(buildDataManager
                .getAllAvaliableNames(), this.ownerType, this.name, true);
        buildDataManager.addVariableUsage(fieldUsage);
        return fieldUsage;
    }

    public UnresolvedEntityUsageInfo resolveReferencedEntityIfPossible(
            final BuildDataManager buildDataManager) {
        this.ownerType = this.owner.resolveReferencedEntityIfPossible(buildDataManager);

        if (this.ownerType != null) {
            final UnresolvedFieldUsageInfo fieldUsage = new UnresolvedFieldUsageInfo(
                    buildDataManager.getAllAvaliableNames(), this.ownerType, this.name, true);
            buildDataManager.addVariableUsage(fieldUsage);
            return fieldUsage;
        }

        return null;
    }

    protected UnresolvedEntityUsageInfo resolveOwner(final BuildDataManager buildDataManager) {
        this.ownerType = this.owner.resolveReferencedEntityIfPossible(buildDataManager);

        return null != this.ownerType ? this.ownerType : new UnresolvedUnknownUsageInfo(
                buildDataManager.getAllAvaliableNames(), this.owner.getQualifiedName());
    }

    private final String name;

    private final String[] qualifiedName;

    private final IdentifierElement owner;

    private UnresolvedEntityUsageInfo ownerType;

}
