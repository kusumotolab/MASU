package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

/**
 * @author owner
 *
 */
public class ExpressionDescriptionBuilder extends ExpressionBuilder{

    
    /**
     * @param expressionManager
     */
    public ExpressionDescriptionBuilder(ExpressionElementManager expressionManager) {
        super(expressionManager);
    }
    
    
    protected void afterExited(AstVisitEvent event){
        ExpressionElement[] elements = getAvailableElements();
        
        assert(elements.length == 1) : "Illegal state: too many elements was remained.";
        
        pushElement(elements[0]);
    }

    @Override
    protected boolean isTriggerToken(AstToken token) {
       return token.isExpression();
    }

}
