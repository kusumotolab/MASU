package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import java.util.Stack;

public class ExpressionElementManager {
    public int getExpressionStackSize() {
        return this.expressionAnalyzeStack.size();
    }
    
    public void pushExpressionElement(final ExpressionElement element) {
        this.expressionAnalyzeStack.push(element);
    }

    public ExpressionElement popExpressionElement() {
        this.lastPoppedExpressinElement = this.expressionAnalyzeStack.pop();
        return this.lastPoppedExpressinElement;
    }
    
    public final ExpressionElement getLastPoppedExpressionElement() {
        return this.lastPoppedExpressinElement;
    }
    
    public final ExpressionElement getPeekExpressionElement() {
        return this.expressionAnalyzeStack.empty() ? null : this.expressionAnalyzeStack.peek();
    }
    
    public void reset(){
        expressionAnalyzeStack.clear();
        this.lastPoppedExpressinElement = null;
    }
    
    private ExpressionElement lastPoppedExpressinElement = null;
    
    private final Stack<ExpressionElement> expressionAnalyzeStack = new Stack<ExpressionElement>();
}
