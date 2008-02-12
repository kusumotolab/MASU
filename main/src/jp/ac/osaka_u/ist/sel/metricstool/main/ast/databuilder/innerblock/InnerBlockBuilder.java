package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock;

import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.CompoundDataBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.InnerBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.InnerBlockStateManager.INNER_BLOCK_STATE_CHANGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBlockInfo;

public abstract class InnerBlockBuilder extends CompoundDataBuilder<UnresolvedBlockInfo> {

	protected InnerBlockBuilder(final BuildDataManager targetDataManager, final InnerBlockStateManager innerBlockStateManager){
	    
	    if(null == targetDataManager) {
	        throw new IllegalArgumentException("targetDataManager is null.");
	    }
	    
		this.buildManager = targetDataManager;
		this.blockStateManager = innerBlockStateManager;
		addStateManager(blockStateManager);
	}
	
	@Override
    public void stateChangend(final StateChangeEvent<AstVisitEvent> event) {
        StateChangeEventType type = event.getType();
        
        if (type.equals(INNER_BLOCK_STATE_CHANGE.ENTER_BLOCK_DEF)) {
            startBlockDefinition(event.getTrigger());
        } else if (type.equals(INNER_BLOCK_STATE_CHANGE.EXIT_BLOCK_DEF)) {
            endBlockDefinition();
        } else if (type.equals(INNER_BLOCK_STATE_CHANGE.ENTER_CLAUSE)) {
            
        } else if (type.equals(INNER_BLOCK_STATE_CHANGE.EXIT_CLAUSE)) {
            
        } else if (type.equals(INNER_BLOCK_STATE_CHANGE.ENTER_BLOCK_SCOPE)) {

        } else if(type.equals(INNER_BLOCK_STATE_CHANGE.EXIT_BLOCK_SCOPE)) {
            
        }
    }
    
    protected void endBlockDefinition(){
        UnresolvedBlockInfo buildBlock = buildingBlockStack.pop();
        
        if (null != buildManager && null != buildBlock){
        	registBuiltData(buildBlock);
            buildManager.endInnerBlockDefinition();
        }
    }
    
    protected void startBlockDefinition(AstVisitEvent triggerEvent){
        UnresolvedBlockInfo newBlock = createUnresolvedBlockInfo();
        
        this.buildingBlockStack.push(newBlock);
        
        newBlock.setFromLine(triggerEvent.getStartLine());
        newBlock.setFromColumn(triggerEvent.getStartColumn());
        newBlock.setToLine(triggerEvent.getEndLine());
        newBlock.setToColumn(triggerEvent.getEndColumn());
        buildManager.startInnerBlockDefinition(newBlock);
    }
    
    protected abstract UnresolvedBlockInfo createUnresolvedBlockInfo();
        
    protected Stack<UnresolvedBlockInfo> buildingBlockStack = new Stack<UnresolvedBlockInfo>();
    
    protected final BuildDataManager buildManager;
    
    
    
    protected final InnerBlockStateManager blockStateManager;
}
