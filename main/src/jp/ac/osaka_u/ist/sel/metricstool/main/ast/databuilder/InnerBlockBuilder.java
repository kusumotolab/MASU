package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;

import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.InnerBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.InnerBlockStateManager.INNER_BLOCK_STATE_CHANGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.SpecificBlockToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCatchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedDefaultEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFinallyBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedIfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedSwitchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTryBlockInfo;

public class InnerBlockBuilder extends CompoundDataBuilder<UnresolvedBlockInfo> {

	public InnerBlockBuilder(BuildDataManager targetDataManager){
		this.buildManager = targetDataManager;
		
		addStateManager(blockStateManager);
	}
	
    public void stateChangend(final StateChangeEvent<AstVisitEvent> event) {
        StateChangeEventType type = event.getType();
        
        if (type.equals(INNER_BLOCK_STATE_CHANGE.ENTER_BLOCK_DEF)) {
            startBlockDefinition(event.getTrigger());
        } else if (type.equals(INNER_BLOCK_STATE_CHANGE.EXIT_BLOCK_DEF)) {
            endBlockDefinition();
        } else if (type.equals(INNER_BLOCK_STATE_CHANGE.ENTER_BLOCK_SCOPE)) {
            if (null != buildManager){
            	
            }
        }
    }
    
    private void endBlockDefinition(){
        UnresolvedBlockInfo buildBlock = buildingBlockStack.pop();
        
        if (null != buildManager && null != buildBlock){
        	registBuiltData(buildBlock);
            buildManager.endInnerBlockDefinition();
        }
    }
    
    private void startBlockDefinition(AstVisitEvent triggerEvent){
    	AstToken token = triggerEvent.getToken();
        UnresolvedBlockInfo newBlock = null;
        
        if(token instanceof SpecificBlockToken){
        	SpecificBlockToken blockDefinitionToken = (SpecificBlockToken) token;
        	
        	if (token.equals(SpecificBlockToken.CATCH_BLOCK)) {
        		
        		assert buildingBlockStack.peek() instanceof UnresolvedTryBlockInfo : "Illegal state:";
        		
    			UnresolvedTryBlockInfo tryInfo = (UnresolvedTryBlockInfo) buildingBlockStack.peek();
    			newBlock = new UnresolvedCatchBlockInfo(tryInfo);
        	} else if (token.equals(SpecificBlockToken.FINALLY_BLOCK)) {
        		
        		assert buildingBlockStack.peek() instanceof UnresolvedTryBlockInfo : "Illegal state:";
        		
    			UnresolvedTryBlockInfo tryInfo = (UnresolvedTryBlockInfo) buildingBlockStack.peek();
    			newBlock = new UnresolvedFinallyBlockInfo(tryInfo);
        	} else if (token.equals(SpecificBlockToken.ELSE_BLOCK)) {
        		
        		assert buildingBlockStack.peek() instanceof UnresolvedIfBlockInfo : "Illegal state:";
        		
    			UnresolvedIfBlockInfo ifInfo = (UnresolvedIfBlockInfo) buildingBlockStack.peek();
    			newBlock = new UnresolvedElseBlockInfo(ifInfo);
        	} else if (token.equals(SpecificBlockToken.CASE_ENTRY)) {
        		
        		assert buildingBlockStack.peek() instanceof UnresolvedSwitchBlockInfo : "Illegal state:";
        		
        		UnresolvedSwitchBlockInfo switchInfo = (UnresolvedSwitchBlockInfo) buildingBlockStack.peek();
    			newBlock = new UnresolvedCaseEntryInfo(switchInfo);
        	} else if (token.equals(SpecificBlockToken.DEFAULT_ENTRY)) {
        		
        		assert buildingBlockStack.peek() instanceof UnresolvedSwitchBlockInfo : "Illegal state:";
        		
        		UnresolvedSwitchBlockInfo switchInfo = (UnresolvedSwitchBlockInfo) buildingBlockStack.peek();
    			newBlock = new UnresolvedDefaultEntryInfo(switchInfo);
        	} else {
        		newBlock = blockDefinitionToken.createUnresolvedInfo();
        	}
        }
        
        startBlockDefinition(newBlock, triggerEvent);
    }
    
    private void startBlockDefinition(UnresolvedBlockInfo newBlock, AstVisitEvent triggerEvent){
    	this.buildingBlockStack.push(newBlock);
        
        if (null != buildManager && null != newBlock){
        	newBlock.setFromLine(triggerEvent.getStartLine());
            newBlock.setFromColumn(triggerEvent.getStartColumn());
            newBlock.setToLine(triggerEvent.getEndLine());
            newBlock.setToColumn(triggerEvent.getEndColumn());
            buildManager.startInnerBlockDefinition(newBlock);
        }
    }

    private Stack<UnresolvedBlockInfo> buildingBlockStack = new Stack<UnresolvedBlockInfo>();
    
    private final BuildDataManager buildManager;
    
    private final InnerBlockStateManager blockStateManager = new InnerBlockStateManager();
}
