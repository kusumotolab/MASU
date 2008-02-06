package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.CallableUnitStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.MethodParameterStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ModifiersDefinitionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.NameStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.TypeDescriptionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.TypeParameterStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.VariableDefinitionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.CallableUnitStateManager.CALLABLE_UNIT_STATE_CHANGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCallableUnitInfo;

public abstract class CallableUnitBuilder<T extends UnresolvedCallableUnitInfo<?>> extends CompoundDataBuilder<T>{
    
    protected CallableUnitBuilder(BuildDataManager buildDataManager, CallableUnitStateManager stateManager, ModifiersInterpriter interpriter) {
       this(buildDataManager, stateManager, interpriter, new ModifiersBuilder(),new TypeBuilder(buildDataManager),
               new NameBuilder(),new MethodParameterBuilder(buildDataManager,interpriter));
    }
    
    protected CallableUnitBuilder(BuildDataManager targetDataManager, CallableUnitStateManager stateManager, ModifiersInterpriter interpriter,
            ModifiersBuilder modifiersBuilder,TypeBuilder typeBuilder,
            NameBuilder nameBuilder,MethodParameterBuilder parameterbuilder){
        
        this.buildManager = targetDataManager;
        this.stateManager = stateManager;
        this.modifiersInterpriter = interpriter;
        this.modifierBuilder = modifiersBuilder;
        this.typeBuilder = typeBuilder;
        this.nameBuilder = nameBuilder;
        this.methodParameterBuilder = parameterbuilder;
        
        addInnerBuilder(modifiersBuilder);
        addInnerBuilder(typeBuilder);
        addInnerBuilder(nameBuilder);
        addInnerBuilder(parameterbuilder);
        
        modifiersBuilder.deactivate();
        typeBuilder.deactivate();
        nameBuilder.deactivate();
        parameterbuilder.deactivate();
        
        addStateManager(this.stateManager);
        addStateManager(this.parameterStateManager);
        addStateManager(this.typeStateManager);
        addStateManager(this.typeParameterStateManager);
        addStateManager(new ModifiersDefinitionStateManager());
        addStateManager(new NameStateManager());
    }
    
    
    @Override
    public void stateChangend(final StateChangeEvent<AstVisitEvent> event) {
        StateChangeEventType type = event.getType();
        
        if (type.equals(CALLABLE_UNIT_STATE_CHANGE.ENTER_DEF)){
            startUnitDefinition(event.getTrigger());
        } else if (type.equals(CALLABLE_UNIT_STATE_CHANGE.EXIT_DEF)){
            endUnitDefinition();
        } else if (type.equals(CALLABLE_UNIT_STATE_CHANGE.ENTER_BLOCK)){
            if (null != buildManager){
                buildManager.enterMethodBlock();
            }
        } else if (isActive() && stateManager.isInPreDeclaration()){
            if (!parameterStateManager.isInDefinition()){
                if (type.equals(ModifiersDefinitionStateManager.MODIFIERS_STATE.ENTER_MODIFIERS_DEF)){
                    if (null != modifierBuilder){
                        modifierBuilder.activate();
                    }
                } else if (type.equals(ModifiersDefinitionStateManager.MODIFIERS_STATE.EXIT_MODIFIERS_DEF)){
                    if (null != modifierBuilder){
                        modifierBuilder.deactivate();
                        registModifiers();
                        modifierBuilder.clearBuiltData();
                    }
                } else if (type.equals(NameStateManager.NAME_STATE.ENTER_NAME)){
                    if (null != nameBuilder){
                        nameBuilder.activate();
                    }
                } else if (type.equals(NameStateManager.NAME_STATE.EXIT_NAME)){
                    if (null != nameBuilder){
                        nameBuilder.deactivate();
                        registName();
                        nameBuilder.clearBuiltData();
                    }
                } else if (!typeParameterStateManager.isInTypeParameterDefinition()){
                    if (type.equals(TypeDescriptionStateManager.TYPE_STATE.ENTER_TYPE)){
                        if (null != typeBuilder){
                            typeBuilder.activate();
                        }
                    } else if (type.equals(TypeDescriptionStateManager.TYPE_STATE.EXIT_TYPE)){
                        if (null != typeBuilder && !typeStateManager.isEntered()){
                            typeBuilder.deactivate();
                            registType();
                            typeBuilder.clearBuiltData();
                        }
                    }
                }
            }
            
            if (type.equals(VariableDefinitionStateManager.VARIABLE_STATE.ENTER_VARIABLE_DEF)){
                if (null != methodParameterBuilder){
                    methodParameterBuilder.activate();
                }
            } else if (type.equals(VariableDefinitionStateManager.VARIABLE_STATE.EXIT_VARIABLE_DEF)){
                if (null != methodParameterBuilder){
                    methodParameterBuilder.deactivate();
                }
            }
        }
    }
    
    private void endUnitDefinition(){
        T builtMethod = buildingUnitStack.pop();
        registBuiltData(builtMethod);
        
        if (null != buildManager){
            buildManager.endMethodDefinition();
        }
    }
    
    private void registModifiers(){
        if (!buildingUnitStack.isEmpty()){
            T buildingMethod = buildingUnitStack.peek();
            ModifierInfo[] modifiers = modifierBuilder.popLastBuiltData();
            for(ModifierInfo modifier : modifiers){
                buildingMethod.addModifier(modifier);
            }
            
            if (null != this.modifiersInterpriter){
                modifiersInterpriter.interprit(modifiers,buildingMethod,buildingMethod);
            }
        }
    }
    
    protected abstract void registType();
    
    protected abstract void registName();
    
    private void startUnitDefinition(AstVisitEvent triggerEvent){
        T callableUnit = createUnresolvedCallableUnitInfo();
        
        this.buildingUnitStack.push(callableUnit);
        callableUnit.setFromLine(triggerEvent.getStartLine());
        callableUnit.setFromColumn(triggerEvent.getStartColumn());
        callableUnit.setToLine(triggerEvent.getEndLine());
        callableUnit.setToColumn(triggerEvent.getEndColumn());
        
        if (null != buildManager){
            buildManager.startMethodDefinition(callableUnit);
        }
    }
    
    protected abstract T createUnresolvedCallableUnitInfo();
    
    protected Stack<T> buildingUnitStack = new Stack<T>();
    
    protected final ModifiersInterpriter modifiersInterpriter;
    
    protected final BuildDataManager buildManager;
    
    protected final TypeBuilder typeBuilder;
    protected final ModifiersBuilder modifierBuilder;
    protected final NameBuilder nameBuilder;
    protected final MethodParameterBuilder methodParameterBuilder;
    
    protected final CallableUnitStateManager stateManager;
    protected final MethodParameterStateManager parameterStateManager = new MethodParameterStateManager();
    protected final TypeDescriptionStateManager typeStateManager = new TypeDescriptionStateManager();
    protected final TypeParameterStateManager typeParameterStateManager = new TypeParameterStateManager();
}
