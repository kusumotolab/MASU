package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.InstanceToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

public class InstanceElementBuilder extends ExpressionBuilder{

    public InstanceElementBuilder(ExpressionElementManager expressionManager) {
        super(expressionManager);
    }

    @Override
    protected void afterExited(AstVisitEvent event) {
        AstToken token = event.getToken();
        
        if (token.equals(InstanceToken.THIS)){
            pushElement(InstanceSpecificElement.THIS);
        } else if (token.equals(InstanceToken.NULL)){
            pushElement(InstanceSpecificElement.NULL);
        }
    }

    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.equals(InstanceToken.THIS) || token.equals(InstanceToken.NULL);
    }
}
