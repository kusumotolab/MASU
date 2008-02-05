package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ConstructorStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedConstructorInfo;

public class ConstructorBuilder extends CallableUnitBuilder<UnresolvedConstructorInfo>{
    

    public ConstructorBuilder(BuildDataManager targetDataManager,ModifiersInterpriter interpriter,
            ModifiersBuilder modifiersBuilder,TypeBuilder typeBuilder,
            NameBuilder nameBuilder,MethodParameterBuilder parameterbuilder){
        
        super(targetDataManager, interpriter, modifiersBuilder, typeBuilder, nameBuilder, parameterbuilder);
        
        stateManager = new ConstructorStateManager();
    }
    
    @Override
    protected void registType(){
        
    }
    
    @Override
    protected void registName(){
        
    }

    @Override
    protected UnresolvedConstructorInfo createUnresolvedCallableUnitInfo() {
        return new UnresolvedConstructorInfo(buildManager.getCurrentClass());
    }
    
}
