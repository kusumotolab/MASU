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
        return this.expressionAnalyzeStack.pop();
    }
    
    public void reset(){
        expressionAnalyzeStack.clear();
    }
    
    private final Stack<ExpressionElement> expressionAnalyzeStack = new Stack<ExpressionElement>();
}
