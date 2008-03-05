package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedReferenceTypeInfo;

public class MethodCallBuilder extends ExpressionBuilder{

    /**
     * @param expressionManager
     */
    public MethodCallBuilder(ExpressionElementManager expressionManager, BuildDataManager buildDataManager) {
        super(expressionManager);
        this.buildDataManager = buildDataManager;
    }

    @Override
    protected void afterExited(AstVisitEvent event){
        AstToken token = event.getToken();
        if (token.isMethodCall()){
            buildMethodCall(event);
        }
    }
    
    protected void buildMethodCall(final AstVisitEvent event){
        ExpressionElement[] elements = getAvailableElements();
        
        if (elements.length > 0){
            if (elements[0] instanceof IdentifierElement){
                IdentifierElement callee = (IdentifierElement)elements[0];
                
                callee = callee.resolveAsCalledMethod(this.buildDataManager);
                
                final UnresolvedMethodCallInfo methodCall = new UnresolvedMethodCallInfo(callee.getOwnerUsage(),callee.getName());
                // 開始位置はメソッド名の出現位置
                methodCall.setFromLine(callee.getFromLine());
                methodCall.setFromColumn(callee.getFromColumn());
                // 終了位置はメソッド呼び出し式の終了位置
                methodCall.setToLine(event.getEndLine());
                methodCall.setToColumn(event.getEndColumn());
                
                for(int i=1; i < elements.length; i++){
                    ExpressionElement argment = elements[i];
                    if (argment instanceof IdentifierElement){
                        methodCall.addParameter(((IdentifierElement)argment).resolveAsReferencedVariable(this.buildDataManager));
                    } else if (argment.equals(InstanceSpecificElement.THIS)){
                        methodCall.addParameter(InstanceSpecificElement.getThisInstanceType(this.buildDataManager));
                    } else if (argment instanceof TypeArgumentElement) {
                    	// TODO C#などの場合は型引数に参照型以外も指定できるので対処が必要かも
                        TypeArgumentElement typeArgument = (TypeArgumentElement) argment;
                    	assert typeArgument.getType() instanceof UnresolvedReferenceTypeInfo : "type argument was not reference type.";
                        methodCall.addTypeArgument((UnresolvedReferenceTypeInfo) typeArgument.getType());
                    } else {
                        methodCall.addParameter(argment.getUsage());
                    }
                }
                
                pushElement(UsageElement.getInstance(methodCall));
                this.buildDataManager.addMethodCall(methodCall);
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
