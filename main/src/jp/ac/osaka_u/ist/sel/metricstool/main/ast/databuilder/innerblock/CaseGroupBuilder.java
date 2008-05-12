package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.DeclaredBlockStateManager.DeclaredBlockState;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.CaseGroupStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.CaseGroupStateManager.CASE_ENTRY_STATE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.CaseGroupStateManager.CASE_GROUP_STATE_CHANGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.InnerBlockStateManager.INNER_BLOCK_STATE_CHANGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedDefaultEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedSwitchBlockInfo;

public class CaseGroupBuilder extends InnerBlockBuilder<CaseEntryInfo, UnresolvedCaseEntryInfo> {

    public CaseGroupBuilder(final BuildDataManager targetDataManager){
        super(targetDataManager, new CaseGroupStateManager());
    }
    
    @Override
    public void stateChangend(final StateChangeEvent<AstVisitEvent> event) {
        final StateChangeEventType type = event.getType();
        
        if (type.equals(CASE_GROUP_STATE_CHANGE.ENTER_ENTRY_DEF)) {
            this.buildBlockDefinition(event.getTrigger());
        } else if (type.equals(CASE_GROUP_STATE_CHANGE.EXIT_ENTRY_DEF)) {
            this.endBlockDefinition();
        } else if (type.equals(INNER_BLOCK_STATE_CHANGE.ENTER_BLOCK_SCOPE)) {
            this.startBlockDefinition(this.getLastBuildData());
        } else if (type.equals(INNER_BLOCK_STATE_CHANGE.EXIT_BLOCK_SCOPE)) {
            this.endBlockDefinition();
        } else if (type.equals(CASE_GROUP_STATE_CHANGE.ENTER_BREAK_STATEMENT)) {
            if(this.buildManager.getCurrentUnit().equals(this.buildingBlockStack.peek())) {
               this.buildingBlockStack.peek().setHasBreak(true); 
            }
        }
    }
    
    @Override
    protected UnresolvedCaseEntryInfo createUnresolvedBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        if(outerSpace instanceof UnresolvedSwitchBlockInfo) {
            final UnresolvedSwitchBlockInfo ownerSwitch = (UnresolvedSwitchBlockInfo) outerSpace;
            
            final DeclaredBlockState currentState = this.blockStateManager.getState();
            if(currentState.equals(CASE_ENTRY_STATE.CASE_DEF)) {
                return new UnresolvedCaseEntryInfo(ownerSwitch);
            } else if(currentState.equals(CASE_ENTRY_STATE.DEFAULT_DEF)) {
                return new UnresolvedDefaultEntryInfo(ownerSwitch);
            }
        }
        
        assert false : "Illegal state : incorrect block structure";
        return null;
        
    }
    

}
