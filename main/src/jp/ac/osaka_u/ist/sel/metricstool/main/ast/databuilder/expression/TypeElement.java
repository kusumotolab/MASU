package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLiteralUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


/**
 * @author kou-tngt
 *
 */
public class TypeElement extends ExpressionElement {

    public TypeElement getArrayDimensionInclementedInstance() {
        UnresolvedTypeInfo newType = null;
        if (this.type instanceof UnresolvedArrayTypeInfo) {
            newType = ((UnresolvedArrayTypeInfo) this.type).getDimensionInclementedArrayType();
        } else {
            newType = UnresolvedArrayTypeInfo.getType(this.type, 1);
        }

        return new TypeElement(newType);
    }

    public UnresolvedTypeInfo getType() {
        return this.type;
    }

    public TypeElement(final UnresolvedTypeInfo type) {
        super();

        if (null == type) {
            throw new NullPointerException("type is null.");
        }
        this.type = type;

        if (this.type instanceof PrimitiveTypeInfo) {
            this.usage = new UnresolvedLiteralUsageInfo("", (PrimitiveTypeInfo) this.type);
        }
    }
    
    public TypeElement(final UnresolvedLiteralUsageInfo literal) {
        if(null == literal) {
            throw new IllegalArgumentException();
        }
        
        this.type = literal.getType();
        this.usage = literal;
    }

    private final UnresolvedTypeInfo type;


}
