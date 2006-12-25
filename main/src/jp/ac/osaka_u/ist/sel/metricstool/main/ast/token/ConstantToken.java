package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;

/**
 * @author kou-tngt
 *
 */
public class ConstantToken extends AstTokenAdapter{
    
    /**
     * @param text
     */
    public ConstantToken(String text, UnresolvedTypeInfo type) {
        super(text);
        
        this.type = type;
    }
    
    public UnresolvedTypeInfo getType(){
        return type;
    }
    
    private final UnresolvedTypeInfo type;

}
