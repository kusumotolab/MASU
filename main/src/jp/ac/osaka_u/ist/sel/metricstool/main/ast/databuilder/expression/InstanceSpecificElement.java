package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedNullUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


public class InstanceSpecificElement extends ExpressionElement {

    public static final InstanceSpecificElement THIS = new InstanceSpecificElement();

    public static final InstanceSpecificElement NULL = new InstanceSpecificElement(null , new UnresolvedNullUsageInfo());

    private InstanceSpecificElement(UnresolvedTypeInfo type, UnresolvedEntityUsageInfo usage) {
        // TODO 0‚Å‚È‚¢‚Ì‚ð‚¢‚ê‚é‚×‚«?
        super(usage);
        this.type = type;
    }

    private InstanceSpecificElement() {
        super();
        this.type = null;
    }
    
    public static UnresolvedClassReferenceInfo getThisInstanceType(BuildDataManager buildManager) {
        return buildManager.getCurrentClass().getClassReference();
    }

    public UnresolvedTypeInfo getType() {
        return type;
    }

    private final UnresolvedTypeInfo type;

}
