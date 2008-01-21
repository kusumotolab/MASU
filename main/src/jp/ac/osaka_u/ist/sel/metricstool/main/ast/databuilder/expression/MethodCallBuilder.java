package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedSimpleTypeParameterUsageInfo;

public class MethodCallBuilder extends ExpressionBuilder{

    /**
     * @param expressionManager
     */
    public MethodCallBuilder(ExpressionElementManager expressionManager, BuildDataManager buildDataManager) {
        super(expressionManager);
        this.buildDataManager = buildDataManager;
    }

    protected void afterExited(AstVisitEvent event){
        AstToken token = event.getToken();
        if (token.isMethodCall()){
            buildMethodCall();
        }
    }
    
    protected void buildMethodCall(){
        ExpressionElement[] elements = getAvailableElements();
        
        if (elements.length > 0){
            if (elements[0] instanceof IdentifierElement){
                IdentifierElement callee = (IdentifierElement)elements[0];
                
                callee = callee.resolveAsCalledMethod(buildDataManager);
                
                UnresolvedMethodCallInfo methodCall = new UnresolvedMethodCallInfo(callee.getOwnerType(),callee.getName(),false);
                for(int i=1; i < elements.length; i++){
                    ExpressionElement argment = elements[i];
                    if (argment instanceof IdentifierElement){
                        methodCall.addParameterType(((IdentifierElement)argment).resolveAsReferencedVariable(buildDataManager));
                    } else if (argment.equals(InstanceSpecificElement.THIS)){
                        methodCall.addParameterType(InstanceSpecificElement.getThisInstanceType(buildDataManager));
                    } else if (argment instanceof TypeArgumentElement){
                        methodCall.addTypeParameterUsage(new UnresolvedSimpleTypeParameterUsage(argment.getType()));
                    } else {
                        methodCall.addParameterType(argment.getType());
                    }
                }
                
                pushElement(new MethodCallElement(methodCall));
                buildDataManager.addMethodCall(methodCall);
            }
            
        } else {
            assert(false) : "Illegal state: callee element was not found.";
        }
    }
    
    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.isMethodCall();
    }
    
    private final BuildDataManager buildDataManager;

}
