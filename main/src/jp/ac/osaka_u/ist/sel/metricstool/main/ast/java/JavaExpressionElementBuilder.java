package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.TypeElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;

public class JavaExpressionElementBuilder extends ExpressionBuilder{

    /**
     * @param expressionManager
     */
    public JavaExpressionElementBuilder(ExpressionElementManager expressionManager,BuildDataManager buildManager) {
        super(expressionManager);
        
        if (null == buildManager){
            throw new NullPointerException("buildManager is null.");
        }
        this.buildManager = buildManager;
    }

    @Override
    protected void afterExited(AstVisitEvent event) {
        AstToken token = event.getToken();
        if (token.equals(JavaAstToken.SUPER)){
            UnresolvedClassInfo classInfo = buildManager.getCurrentClass();
            UnresolvedTypeInfo superClassType = classInfo.getSuperClasses().iterator().next();
            pushElement(new TypeElement(superClassType));
        } else if (token.equals(JavaAstToken.CLASS)){
            pushElement(JavaExpressionElement.CLASS);
        }
    }

    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.equals(JavaAstToken.SUPER) || token.equals(JavaAstToken.CLASS);
    }
    
    private final BuildDataManager buildManager;

}
