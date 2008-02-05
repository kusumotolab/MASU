package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock;

import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.InnerBlockStateManager.INNER_BLOCK_STATE_CHANGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedConditionalClauseInfo;

public abstract class ConditionalBlockBuilder extends InnerBlockBuilder {

    protected ConditionalBlockBuilder(BuildDataManager targetDataManager){
        super(targetDataManager);
    }

    @Override
    public void stateChangend(final StateChangeEvent<AstVisitEvent> event) {
        super.stateChangend(event);
        StateChangeEventType type = event.getType();
        
        if (type.equals(INNER_BLOCK_STATE_CHANGE.ENTER_CLAUSE)) {
            startConditionalClause(event.getTrigger());
        } else if (type.equals(INNER_BLOCK_STATE_CHANGE.EXIT_CLAUSE)) {
            endConditionalClause();
        }
    }
    
    private void startConditionalClause(AstVisitEvent triggerEvent) {
        UnresolvedConditionalClauseInfo conditionalClause = new UnresolvedConditionalClauseInfo();
        
        this.buildingClauseStack.push(conditionalClause);
        
        conditionalClause.setFromLine(triggerEvent.getStartLine());
        conditionalClause.setFromColumn(triggerEvent.getStartColumn());
        conditionalClause.setToLine(triggerEvent.getEndLine());
        conditionalClause.setToColumn(triggerEvent.getEndColumn());
        
        this.buildManager.startConditionalClause(conditionalClause);
    }
    
    private void endConditionalClause() {
        UnresolvedConditionalClauseInfo buildClause = this.buildingClauseStack.pop();
        
        if (null != buildClause){
            this.buildManager.endClassDefinition();
        }
    }
    
    private final Stack<UnresolvedConditionalClauseInfo> buildingClauseStack = new Stack<UnresolvedConditionalClauseInfo>();
}
