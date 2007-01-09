package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import java.util.Map;
import java.util.WeakHashMap;

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
    
    private final UnresolvedTypeInfo type;
    
    private static final Map<UnresolvedTypeInfo, TypeElement> instanceMap = new WeakHashMap<UnresolvedTypeInfo,TypeElement>();
}
