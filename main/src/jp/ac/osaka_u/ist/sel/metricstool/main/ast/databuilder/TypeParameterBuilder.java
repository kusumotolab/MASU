package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.TypeParameterStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedExtendsTypeParameterUsage;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedSuperTypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeParameterInfo;

public class TypeParameterBuilder extends CompoundDataBuilder<UnresolvedTypeParameterInfo>{
    
    public TypeParameterBuilder(BuildDataManager buildDataManager){
        this(buildDataManager,new NameBuilder(),new TypeBuilder(buildDataManager));
    }
    
    public TypeParameterBuilder(BuildDataManager buildDataManager, NameBuilder nameBuilder, TypeBuilder typeBuilder){
        if (null == buildDataManager){
            throw new NullPointerException("buildDataManager is null.");
        }
        
        if (null == nameBuilder){
            throw new NullPointerException("nameBuilder is null.");
        }
        
        if (null == typeBuilder){
            throw new NullPointerException("typeBuilder is null.");
        }
        
        this.buildDataManager = buildDataManager;
        this.nameBuilder = nameBuilder;
        this.typeBuilder = typeBuilder;
        
        nameBuilder.deactivate();
        typeBuilder.deactivate();
        
        addInnerBuilder(nameBuilder);
        addInnerBuilder(typeBuilder);
        
        addStateManager(new TypeParameterStateManager());
    }
    
    @Override
    public void stateChangend(StateChangeEvent<AstVisitEvent> event) {
        StateChangeEventType type = event.getType();
        
        if (isActive()){
            if (type.equals(TypeParameterStateManager.TYPE_PARAMETER.ENTER_TYPE_PARAMETER_DEF)){
                nameBuilder.activate();
            } else if (type.equals(TypeParameterStateManager.TYPE_PARAMETER.EXIT_TYPE_PARAMETER_DEF)){
                nameBuilder.deactivate();
                typeBuilder.deactivate();
                buildTypeParameter();
                
                this.lowerBoundsType = null;
                this.upperBoundsType = null;
                nameBuilder.clearBuiltData();
                typeBuilder.clearBuiltData();
                
            } else if (type.equals(TypeParameterStateManager.TYPE_PARAMETER.ENTER_TYPE_LOWER_BOUNDS)){
                nameBuilder.deactivate();
                typeBuilder.activate();
            } else if (type.equals(TypeParameterStateManager.TYPE_PARAMETER.EXIT_TYPE_LOWER_BOUNDS)){
                nameBuilder.deactivate();
                typeBuilder.deactivate();
                this.lowerBoundsType = builtTypeBounds();
            } else if (type.equals(TypeParameterStateManager.TYPE_PARAMETER.ENTER_TYPE_UPPER_BOUNDS)){
                nameBuilder.deactivate();
                typeBuilder.activate();
            } else if (type.equals(TypeParameterStateManager.TYPE_PARAMETER.EXIT_TYPE_UPPER_BOUNDS)){
                nameBuilder.deactivate();
                typeBuilder.deactivate();
                this.upperBoundsType = builtTypeBounds();
            }
        }
    }
    
    protected void buildTypeParameter(){
        
        assert(nameBuilder.getBuiltDataCount() ==1);
        
        String[] name = nameBuilder.getLastBuildData();

        assert(name.length == 1);
        
        UnresolvedTypeInfo upperBounds = getUpperBounds();
        UnresolvedTypeInfo lowerBounds = getLowerBounds();
        
        UnresolvedTypeParameterInfo parameter = null;
        if (null ==lowerBounds){
            parameter = new UnresolvedTypeParameterInfo(name[0],upperBounds);
        } else {
            parameter = new UnresolvedSuperTypeParameterInfo(name[0],upperBounds,lowerBounds);
        }
        
        buildDataManager.addTypeParameger(parameter);
    }
    
    protected UnresolvedTypeInfo getUpperBounds(){
        return upperBoundsType;
    }
    
    protected UnresolvedTypeInfo getLowerBounds(){
        return lowerBoundsType;
    }
    
    protected UnresolvedTypeInfo builtTypeBounds(){
        return typeBuilder.getLastBuildData();
    }
    
    protected NameBuilder getNameBuilder(){
        return this.nameBuilder;
    }
    
    protected TypeBuilder getTypeBuilder(){
        return this.typeBuilder;
    }
    
    private final NameBuilder nameBuilder;
    private final TypeBuilder typeBuilder;
    private final BuildDataManager buildDataManager;
    
    private UnresolvedTypeInfo upperBoundsType;
    private UnresolvedTypeInfo lowerBoundsType;
    
}
