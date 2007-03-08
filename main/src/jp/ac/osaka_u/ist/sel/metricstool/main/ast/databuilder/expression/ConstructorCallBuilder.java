package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMethodCall;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedSimpleTypeParameterUsage;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeParameterUsageInfo;

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
            UnresolvedReferenceTypeInfo type = (UnresolvedReferenceTypeInfo)elements[0].getType();
            String[] name = type.getFullReferenceName();
            
            UnresolvedMethodCall constructorCall = new UnresolvedMethodCall(type,name[name.length-1],true);
            resolveParameters(constructorCall, elements,1);
            pushElement(new MethodCallElement(constructorCall));
            buildManager.addMethodCall(constructorCall);
        }
    }
    
    protected void resolveParameters(UnresolvedMethodCall constructorCall,ExpressionElement[] elements, int startIndex){
        for(int i=startIndex; i < elements.length; i++){
            ExpressionElement element = elements[i];
            if (element instanceof IdentifierElement){
                constructorCall.addParameterType(((IdentifierElement)element).resolveAsReferencedVariable(buildManager));
            } else if (element.equals(InstanceSpecificElement.THIS)){
                constructorCall.addParameterType(InstanceSpecificElement.getThisInstanceType(buildManager));
            } else if (element instanceof TypeArgumentElement){
                constructorCall.addTypeParameterUsage(new UnresolvedSimpleTypeParameterUsage(element.getType()));
            } else {
                constructorCall.addParameterType(element.getType());
            }
        }
    }
    
    

    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.isInstantiation();
    }
    
    private final BuildDataManager buildManager;

}
