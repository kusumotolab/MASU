package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedReferenceTypeInfo;

public class ConstructorCallBuilder extends ExpressionBuilder{

    public ConstructorCallBuilder(ExpressionElementManager expressionManager, BuildDataManager buildManager) {
        super(expressionManager);
        this.buildManager = buildManager;
    }

    @Override
    protected void afterExited(AstVisitEvent event) {
        AstToken token = event.getToken();

        if (token.isInstantiation()){
            buildNewConstructorCall();
        }
        
    }
    
    protected void buildNewConstructorCall(){
        ExpressionElement[] elements = getAvailableElements();
        
        assert(elements.length > 0) : "Illegal state: constructor element not found.";
        assert(elements[0] instanceof TypeElement) : "Illegal state: constructor owner is not type.";

        if (elements.length > 0 && elements[0] instanceof TypeElement){
            // TODO 配列のnew文の対処をすべき
            TypeElement type = (TypeElement) elements[0];
            UnresolvedReferenceTypeInfo referenceType = (UnresolvedReferenceTypeInfo)type.getType();
            //String[] name = type.getFullReferenceName();
            
            UnresolvedConstructorCallInfo constructorCall = new UnresolvedConstructorCallInfo(referenceType);
            resolveParameters(constructorCall, elements,1);
            pushElement(UsageElement.getInstance(constructorCall));
            buildManager.addMethodCall(constructorCall);
        }
    }
    
    protected void resolveParameters(UnresolvedConstructorCallInfo constructorCall,ExpressionElement[] elements, int startIndex){
        for(int i=startIndex; i < elements.length; i++){
            ExpressionElement element = elements[i];
            if (element instanceof IdentifierElement){
                constructorCall.addParameter(((IdentifierElement)element).resolveAsReferencedVariable(buildManager));
            } else if (element instanceof TypeArgumentElement) {
                TypeArgumentElement typeArgument = (TypeArgumentElement) element;
                
                // TODO C# などの場合はプリミティブ型も型引数に指定可能
            	assert typeArgument.getType() instanceof UnresolvedReferenceTypeInfo : "Illegal state; type argument was not reference type.";
                constructorCall.addTypeArgument((UnresolvedReferenceTypeInfo) typeArgument.getType());
            } else {
                constructorCall.addParameter(element.getUsage());
            }
        }
    }
    
    

    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.isInstantiation();
    }
    
    private final BuildDataManager buildManager;

}
