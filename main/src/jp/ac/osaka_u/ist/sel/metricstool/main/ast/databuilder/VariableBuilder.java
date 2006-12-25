package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.NameStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ModifiersDefinitionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.TypeDescriptionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.VariableDefinitionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableInfo;

/**
 * 変数データを構築する抽象クラス.
 * 
 * @author kou-tngt
 *
 * @param <T> 構築する変数の型
 */
public abstract class VariableBuilder<T extends UnresolvedVariableInfo> extends CompoundDataBuilder<T>{
    public VariableBuilder(BuildDataManager buildDataManager,VariableDefinitionStateManager stateManager){
        this(stateManager,new ModifiersBuilder(),new TypeBuilder(buildDataManager),new NameBuilder());
    }
    
    public VariableBuilder(VariableDefinitionStateManager stateManager, ModifiersBuilder modifiersBuilder, TypeBuilder typeBuilder, NameBuilder nameBuilder) {
        
        if (null == stateManager){
            throw new NullPointerException("stateManager is null.");
        }
        
        this.stateManager = stateManager;
        addStateManager(stateManager);
        
        addStateManager(new ModifiersDefinitionStateManager());
        addStateManager(new TypeDescriptionStateManager());
        addStateManager(new NameStateManager());
        
        
        this.modifiersBuilder = modifiersBuilder;
        this.typeBuilder = typeBuilder;
        this.nameBuilder = nameBuilder;
        
        addInnerBuilder(modifiersBuilder);
        addInnerBuilder(typeBuilder);
        addInnerBuilder(nameBuilder);
        
        modifiersBuilder.deactivate();
        typeBuilder.deactivate();
        nameBuilder.deactivate();
    }

    @Override
    public void stateChangend(StateChangeEvent<AstVisitEvent> event){
        StateChangeEventType eventType = event.getType();
        if (eventType.equals(VariableDefinitionStateManager.VARIABLE_STATE.EXIT_VARIABLE_DEF)){
            AstVisitEvent trigger = event.getTrigger();
            endVariableBuild(trigger.getStartLine(),trigger.getStartColumn(),trigger.getEndLine(),trigger.getEndColumn());
        } else if (isActive() && stateManager.isInDefinition()){
            if (eventType.equals(ModifiersDefinitionStateManager.MODIFIERS_STATE.ENTER_MODIFIERS_DEF)){
                if (null != modifiersBuilder){
                    modifiersBuilder.activate();
                }
            } else if (eventType.equals(ModifiersDefinitionStateManager.MODIFIERS_STATE.EXIT_MODIFIERS_DEF)){
                if (null != modifiersBuilder){
                    modifiersBuilder.deactivate();
                }
            } else if (eventType.equals(TypeDescriptionStateManager.TYPE_STATE.ENTER_TYPE)){
                if (null != typeBuilder){
                    typeBuilder.activate();
                }
            } else if (eventType.equals(TypeDescriptionStateManager.TYPE_STATE.EXIT_TYPE)){
                if (null != typeBuilder){
                    typeBuilder.deactivate();
                }
            } else if (eventType.equals(NameStateManager.NAME_STATE.ENTER_NAME)){
                if (null != nameBuilder){
                    nameBuilder.activate();
                }
            } else if (eventType.equals(NameStateManager.NAME_STATE.EXIT_NAME)){
                if (null != nameBuilder){
                    nameBuilder.deactivate();
                }
            }
        } 
    }
    
    protected abstract T buildVariable(String[] name, UnresolvedTypeInfo type,ModifierInfo[] modifiers);
    
    private void endVariableBuild(int startLine,int startColumn,int endLine, int endColumn){
        T variable = buildVariable(getName(), getType(), getModifiers());
        variable.setFromLine(startLine);
        variable.setFromColumn(startColumn);
        variable.setToLine(endLine);
        variable.setToColumn(endColumn);
        registBuiltData(variable);
    }
    
    private String[] getName(){
        if (null != nameBuilder){
            return nameBuilder.popLastBuiltData();
        } else {
            return EMPTY_NAME;
        }
    }
    
    private ModifierInfo[] getModifiers(){
        if (null != modifiersBuilder){
            return modifiersBuilder.popLastBuiltData();
        } else {
            return EMPTY_MODIFIERS;
        }
    }
    
    private UnresolvedTypeInfo getType(){
        if (null != typeBuilder){
            return typeBuilder.popLastBuiltData();
        } else {
            return UNKNOWN_TYPE;
        }
    }
    
    private final VariableDefinitionStateManager stateManager;
    
    private final ModifiersBuilder modifiersBuilder;
    private final TypeBuilder typeBuilder;
    private final NameBuilder nameBuilder;
    
    private static final String[] EMPTY_NAME = new String[0];
    private static final ModifierInfo[] EMPTY_MODIFIERS = new ModifierInfo[0];
    private static final UnresolvedTypeInfo UNKNOWN_TYPE = PrimitiveTypeInfo.UNKNOWN;
}
