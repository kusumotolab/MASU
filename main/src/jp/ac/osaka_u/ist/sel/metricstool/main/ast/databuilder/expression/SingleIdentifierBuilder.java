package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

public class SingleIdentifierBuilder extends ExpressionBuilder{

    public SingleIdentifierBuilder(ExpressionElementManager expressionManager,BuildDataManager buildDataManager) {
        super(expressionManager);
        
        this.buildDataManager = buildDataManager;
    }

    @Override
    protected void afterExited(AstVisitEvent event) {
        AstToken token = event.getToken();
        if (token.isIdentifier()){
            pushElement(new SingleIdentifierElement(token.toString(),buildDataManager.getCurrentClass()));
        }
    }

    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.isIdentifier();
    }
    
    private final BuildDataManager buildDataManager;

}
