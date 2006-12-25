package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;

import java.util.ArrayList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ModifiersDefinitionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;

public class ModifiersBuilder extends StateDrivenDataBuilder<ModifierInfo[]>{
    public ModifiersBuilder(){
        addStateManager(this.stateMachine);
    }
    
    public void entered(AstVisitEvent event){
        super.entered(event);
        
        if (isActive() && stateMachine.isEntered()){
            AstToken token = event.getToken();
            if (token.isModifier()){
                modifiers.add(ModifierInfo.getModifierInfo(token.toString()));
            }
        }
    }
    
    @Override
    public void stateChangend(StateChangeEvent<AstVisitEvent> event) {
        if (isActive()){
            if (event.getType().equals(ModifiersDefinitionStateManager.MODIFIERS_STATE.EXIT_MODIFIERS_DEF)){
                registBuiltData(buildModifiers());
            }
        }
    }
    
    private ModifierInfo[] buildModifiers(){
        ModifierInfo[] result = new ModifierInfo[modifiers.size()];
        modifiers.toArray(result);
        modifiers.clear();
        return result;
    }
    
    private final List<ModifierInfo> modifiers = new ArrayList<ModifierInfo>();
    
    private final ModifiersDefinitionStateManager stateMachine = new ModifiersDefinitionStateManager();
}
