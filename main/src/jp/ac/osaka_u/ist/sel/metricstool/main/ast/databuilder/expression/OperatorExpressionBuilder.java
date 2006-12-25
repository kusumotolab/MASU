package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.OperatorToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;

public class OperatorExpressionBuilder extends ExpressionBuilder{

    public OperatorExpressionBuilder(ExpressionElementManager expressionManager,BuildDataManager buildManager) {
        super(expressionManager);
        this.buildDataManager = buildManager;
    }

    @Override
    protected void afterExited(AstVisitEvent event) {
        AstToken token = event.getToken();
        if (token instanceof OperatorToken){
            buildOperatorElement((OperatorToken)token);
        }
    }
    
    protected void buildOperatorElement(OperatorToken token){
        int term = token.getTermCount();
        boolean assignmentLeft = token.isAssignmentOperator();
        boolean referenceLeft = token.isLeftTermIsReferencee();
        UnresolvedTypeInfo type = token.getSpecifiedResultType();
        
        ExpressionElement[] elements = getAvailableElements();
        
        assert (term > 0 && term == elements.length) : "Illegal state: unexpected element count.";
        
        UnresolvedTypeInfo leftTermType = null;
        
        if (elements[0] instanceof IdentifierElement){
            IdentifierElement leftElement = (IdentifierElement)elements[0];
            if (referenceLeft){
                leftTermType = leftElement.resolveAsReferencedVariable(buildDataManager);
            }
            
            if (assignmentLeft){
                leftTermType = leftElement.resolveAsAssignmetedVariable(buildDataManager);
            }
        }
        
        for(int i=1; i < term; i++){
            if (elements[i] instanceof IdentifierElement){
                ((IdentifierElement)elements[i]).resolveAsReferencedVariable(buildDataManager);
            }
        }
        
        UnresolvedTypeInfo resultType = null;
        if (null != type){
            resultType = type;
        } else if (token.equals(OperatorToken.ARRAY)){
            UnresolvedTypeInfo ownerType;
            if (elements[0] instanceof IdentifierElement){
                ownerType = ((IdentifierElement)elements[0]).resolveAsReferencedVariable(buildDataManager);
            } else {
                ownerType = elements[0].getType();
            }
            resultType = new UnresolvedArrayElement(ownerType);
        } else if (null != leftTermType){
            resultType = leftTermType;  
        } else{
            resultType = elements[0].getType();
        }
        
        pushElement(new TypeElement(resultType));
    }

    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token instanceof OperatorToken;
    }
    
    private final BuildDataManager buildDataManager;

}
