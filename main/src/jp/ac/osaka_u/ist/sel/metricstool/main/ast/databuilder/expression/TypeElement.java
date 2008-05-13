package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import java.util.Map;
import java.util.WeakHashMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLiteralUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


/**
 * @author kou-tngt
 *
 */
public class TypeElement extends ExpressionElement {

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

    private TypeElement(final UnresolvedTypeInfo type) {
        super();

        if (null == type) {
            throw new NullPointerException("type is null.");
        }
        this.type = type;

        if (this.type instanceof PrimitiveTypeInfo) {
            this.usage = new UnresolvedLiteralUsageInfo("", (PrimitiveTypeInfo) this.type);
        }
    }
    
    public TypeElement(final String literal, final PrimitiveTypeInfo type) {
        if(null == literal || null == type) {
            throw new IllegalArgumentException();
        }
        
        this.type = type;
        this.usage = new UnresolvedLiteralUsageInfo(literal, type);
    }

    private final UnresolvedTypeInfo type;

    private static final Map<UnresolvedTypeInfo, TypeElement> instanceMap = new WeakHashMap<UnresolvedTypeInfo, TypeElement>();
}
