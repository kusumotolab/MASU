package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedNullUsageInfo;


public class InstanceSpecificElement extends ExpressionElement {

    private enum SPECIFIC_ELEMENT_TYPE {
        NULL, THIS
    }

    private InstanceSpecificElement(final UnresolvedEntityUsageInfo<? extends EntityUsageInfo> usage, final SPECIFIC_ELEMENT_TYPE elementType) {
        // TODO 0でないのをいれるべき?
        super(usage);
        
        this.elementType = elementType;
    }

    public static InstanceSpecificElement getThisInstanceType(BuildDataManager buildManager,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        
        final UnresolvedClassReferenceInfo thisInstance = buildManager.getCurrentClass()
                .getClassReference();
        thisInstance.setFromLine(fromLine);
        thisInstance.setFromColumn(fromColumn);
        thisInstance.setToLine(toLine);
        thisInstance.setToColumn(toColumn);
        
        return new InstanceSpecificElement(thisInstance, SPECIFIC_ELEMENT_TYPE.THIS);
    }
    
    public static InstanceSpecificElement getNullElement(final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        final UnresolvedNullUsageInfo nullUsage = new UnresolvedNullUsageInfo();
        nullUsage.setFromLine(fromLine);
        nullUsage.setFromColumn(fromColumn);
        nullUsage.setToLine(toLine);
        nullUsage.setToColumn(toColumn);
        
        return new InstanceSpecificElement(nullUsage, SPECIFIC_ELEMENT_TYPE.NULL);
    }
    
    public final boolean isNullElement() {
        return this.elementType.equals(SPECIFIC_ELEMENT_TYPE.NULL);
    }
    
    public final boolean isThisInstance() {
        return this.elementType.equals(SPECIFIC_ELEMENT_TYPE.THIS);
    }

    private final SPECIFIC_ELEMENT_TYPE elementType;
}
