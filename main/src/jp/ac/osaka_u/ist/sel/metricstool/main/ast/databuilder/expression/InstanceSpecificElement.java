package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NullTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;

public class InstanceSpecificElement implements ExpressionElement{
    
    public static final InstanceSpecificElement THIS = new InstanceSpecificElement(null);
    public static final InstanceSpecificElement NULL = new InstanceSpecificElement(NullTypeInfo.getInstance());
    
    private InstanceSpecificElement(UnresolvedTypeInfo type){
        this.type = type;
    }
    
    public static UnresolvedTypeInfo getThisInstanceType(BuildDataManager buildManager){
        return buildManager.getCurrentClass();
    }

    public UnresolvedTypeInfo getType() {
        return type;
    }
    
    private final UnresolvedTypeInfo type;
}
