package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import java.util.Map;
import java.util.WeakHashMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedPrimitiveTypeUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


/**
 * @author kou-tngt
 *
 */
public class TypeElement implements ExpressionElement {

    public static synchronized TypeElement getInstance(final UnresolvedTypeInfo typeInfo) {
        TypeElement instance = instanceMap.get(typeInfo);
        if (null == instance) {
            instance = new TypeElement(typeInfo);
            instanceMap.put(typeInfo, instance);
        }
        return instance;
    }
    
    public TypeElement getArrayDimensionInclementedInstance() {
        UnresolvedTypeInfo newType = null;
        if (this.type instanceof UnresolvedArrayTypeInfo) {
            newType = ((UnresolvedArrayTypeInfo) this.type).getDimensionInclementedArrayType();
        } else {
            newType = UnresolvedArrayTypeInfo.getType(this.type, 1);
        }

        return getInstance(newType);
    }

    public UnresolvedTypeInfo getType() {
        return this.type;
    }
    
    public UnresolvedEntityUsageInfo getUsage() {
        if(this.type instanceof PrimitiveTypeInfo){
            return new UnresolvedPrimitiveTypeUsageInfo((PrimitiveTypeInfo) this.type);
        }
        return null;
    }

    private TypeElement(final UnresolvedTypeInfo type) {
        if (null == type) {
            throw new NullPointerException("type is null.");
        }
        this.type = type;
    }

    private final UnresolvedTypeInfo type;

    private static final Map<UnresolvedTypeInfo, TypeElement> instanceMap = new WeakHashMap<UnresolvedTypeInfo, TypeElement>();
}
