package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedNullUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


public class InstanceSpecificElement implements ExpressionElement {

    public static final InstanceSpecificElement THIS = new InstanceSpecificElement(null, null);

    public static final InstanceSpecificElement NULL = new InstanceSpecificElement(null , new UnresolvedNullUsageInfo());

    private InstanceSpecificElement(UnresolvedTypeInfo type, UnresolvedEntityUsageInfo usage) {
        this.type = type;
        this.usage = usage;
    }

    public static UnresolvedClassReferenceInfo getThisInstanceType(BuildDataManager buildManager) {
        return buildManager.getCurrentClass().getClassReference();
    }

    public UnresolvedTypeInfo getType() {
        return type;
    }

    public UnresolvedEntityUsageInfo getUsage() {
        return usage;
    }

    private final UnresolvedTypeInfo type;

    private final UnresolvedEntityUsageInfo usage;
}
