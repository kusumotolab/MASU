package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.MethodStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMethodInfo;

public class MethodBuilder extends CallableUnitBuilder<UnresolvedMethodInfo>{
    
    public MethodBuilder(BuildDataManager targetDataManager,ModifiersInterpriter interpriter,
            ModifiersBuilder modifiersBuilder,TypeBuilder typeBuilder,
            NameBuilder nameBuilder,MethodParameterBuilder parameterbuilder){
        
        super(targetDataManager, new MethodStateManager(), interpriter, modifiersBuilder, typeBuilder, nameBuilder, parameterbuilder);
        
    }
       
    @Override
    protected void registType(){
        if (!buildingUnitStack.isEmpty()){
            UnresolvedMethodInfo buildingMethod = buildingUnitStack.peek();
            buildingMethod.setReturnType(typeBuilder.popLastBuiltData());
        }
    }
    
    @Override
    protected void registName(){
        if (!buildingUnitStack.isEmpty()){
            UnresolvedMethodInfo buildingMethod = buildingUnitStack.peek();
            String[] name = nameBuilder.popLastBuiltData();
            if (name.length > 0){
                buildingMethod.setMethodName(name[0]);
            }
        }
    }
    
    @Override
    protected UnresolvedMethodInfo startUnitDefinition(AstVisitEvent triggerEvent) {
        UnresolvedMethodInfo method = super.startUnitDefinition(triggerEvent);
        method.getOwnerClass().addDefinedMethod(method);
        return method;
    }

    @Override
    protected UnresolvedMethodInfo createUnresolvedCallableUnitInfo() {
        return new UnresolvedMethodInfo(this.buildManager.getCurrentClass());
    }
        
}
