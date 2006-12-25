package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.StateDrivenDataBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.ExpressionStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

public abstract class ExpressionBuilder extends StateDrivenDataBuilder<ExpressionElement>{

    public ExpressionBuilder(ExpressionElementManager expressionManager){
        this.expressionManager = expressionManager;
        
        this.addStateManager(this.expressionStateManger);
    }
    
    public final void entered(AstVisitEvent e) {
        super.entered(e);
        
        if (isActive() && isInExpression()){
            if (isTriggerToken(e.getToken())){
                expressionStackCountStack.push(expressionManager.getExpressionStackSize());
            }
        }
    }

    public void exited(AstVisitEvent e) {
        boolean isRelated = isActive() && isInExpression() && isTriggerToken(e.getToken());
        if (isRelated){
            assert (!expressionStackCountStack.isEmpty()) : "Illegal state: illegal stack size.";
            
            int availableElementCount = expressionManager.getExpressionStackSize()
                    - expressionStackCountStack.pop();
            
            assert (0 <= availableElementCount) : "Illegal state: illegal stack size.";
            
            elementBuffer.clear();
            
            for(int i=0; i< availableElementCount; i++){
                elementBuffer.add(0,expressionManager.popExpressionElement());
            }
        }
        
        super.exited(e);
        
        if (isRelated){
            afterExited(e);
        }
    }
    
    @Override
    public void stateChangend(StateChangeEvent<AstVisitEvent> event) {
        
    }
    
    protected abstract void afterExited(AstVisitEvent event);
    
    protected boolean isInExpression(){
        return expressionStateManger.inExpression();
    }
    
    protected abstract boolean isTriggerToken(AstToken token);
    
    
    protected final ExpressionElement[] getAvailableElements(){
        return elementBuffer.toArray(new ExpressionElement[elementBuffer.size()]);
    }
    
    protected final void pushElement(ExpressionElement element){
        expressionManager.pushExpressionElement(element);
    }
    
    private final ExpressionElementManager expressionManager;
    private final ExpressionStateManager expressionStateManger = new ExpressionStateManager();
    
    private Stack<Integer> expressionStackCountStack = new Stack<Integer>();
    
    private final List<ExpressionElement> elementBuffer = new LinkedList<ExpressionElement>();
}
