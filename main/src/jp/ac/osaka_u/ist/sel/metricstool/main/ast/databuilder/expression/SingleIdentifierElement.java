package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedParameterUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableUsageInfo;


public class SingleIdentifierElement extends IdentifierElement {

    public SingleIdentifierElement(final String name, UnresolvedEntityUsageInfo ownerUsage,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(name, fromLine, fromColumn, toLine, toColumn);
        
        this.qualifiedName = new String[] { name };
        this.ownerUsage = ownerUsage;

    }

    public UnresolvedTypeInfo getType() {
        return null;
    }

    public UnresolvedEntityUsageInfo getUsage() {
        return null;
    }

    private UnresolvedVariableUsageInfo resolveAsVariableUsage(BuildDataManager buildDataManager,
            UnresolvedVariableInfo usedVariable, boolean reference) {
        UnresolvedVariableUsageInfo usage;
        if (null == usedVariable || usedVariable instanceof UnresolvedFieldInfo) {
            //変数がみつからないので多分どこかのフィールド or 見つかった変数がフィールドだった
            usage = new UnresolvedFieldUsageInfo(buildDataManager.getAllAvaliableNames(),
                    ownerUsage, name, reference, this.fromLine, this.fromColumn, this.toLine,
                    this.toColumn);
        } else if (usedVariable instanceof UnresolvedParameterInfo) {
            UnresolvedParameterInfo parameter = (UnresolvedParameterInfo) usedVariable;
            usage = new UnresolvedParameterUsageInfo(parameter, reference, this.fromLine,
                    this.fromColumn, this.toLine, this.toColumn);
        } else {
            assert (usedVariable instanceof UnresolvedLocalVariableInfo) : "Illegal state: unexpected VariableInfo";
            UnresolvedLocalVariableInfo localVariable = (UnresolvedLocalVariableInfo) usedVariable;
            usage = new UnresolvedLocalVariableUsageInfo(localVariable, reference, this.fromLine,
                    this.fromColumn, this.toLine, this.toColumn);
        }

        buildDataManager.addVariableUsage(usage);
        return usage;
    }

    @Override
    public UnresolvedVariableUsageInfo resolveAsAssignmetedVariable(
            BuildDataManager buildDataManager) {
        UnresolvedVariableInfo variable = buildDataManager.getCurrentScopeVariable(this.name);

        return resolveAsVariableUsage(buildDataManager, variable, false);
    }

    @Override
    public IdentifierElement resolveAsCalledMethod(BuildDataManager buildDataManager) {
        //特に何もしない
        return this;
    }

    @Override
    public UnresolvedVariableUsageInfo resolveAsReferencedVariable(BuildDataManager buildDataManager) {
        UnresolvedVariableInfo variable = buildDataManager.getCurrentScopeVariable(this.name);

        return resolveAsVariableUsage(buildDataManager, variable, true);
    }

    @Override
    public UnresolvedEntityUsageInfo resolveReferencedEntityIfPossible(
            BuildDataManager buildDataManager) {
        UnresolvedVariableInfo variable = buildDataManager.getCurrentScopeVariable(this.name);
        if (null != variable) {
            return resolveAsVariableUsage(buildDataManager, variable, true);
        } else {
            return null;
        }
    }

}
