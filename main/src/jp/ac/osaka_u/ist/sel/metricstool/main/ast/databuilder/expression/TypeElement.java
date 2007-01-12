package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import java.util.Map;
import java.util.WeakHashMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;

/**
 * @author kou-tngt
 *
 */
public class TypeElement implements ExpressionElement{
    
    
    public TypeElement(UnresolvedTypeInfo type){
        if (null == type){
            throw new NullPointerException("type is null.");
        }
        this.type = type;
    }

    public UnresolvedTypeInfo getType() {
        return type;
    }
    
    public void arrayDimensionIncl(){
        if (type instanceof UnresolvedArrayTypeInfo){
            UnresolvedArrayTypeInfo arrayType = (UnresolvedArrayTypeInfo)type;
            arrayType.inclementDimension();
        } else {
            type = UnresolvedArrayTypeInfo.getType(type, 1);
        }
    }
    
    private UnresolvedTypeInfo type;
    
    private static final Map<UnresolvedTypeInfo, TypeElement> instanceMap = new WeakHashMap<UnresolvedTypeInfo,TypeElement>();
}
