package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassReferenceInfo;


public class SingleIdentifierBuilder extends ExpressionBuilder {

    public SingleIdentifierBuilder(final ExpressionElementManager expressionManager,
            final BuildDataManager buildDataManager) {
        super(expressionManager, buildDataManager);
    }

    @Override
    protected void afterExited(AstVisitEvent event) {
        AstToken token = event.getToken();
        if (token.isIdentifier()) {
            //            AvailableNamespaceInfoSet nameSpaceset = buildDataManager.getAllAvaliableNames();
            //            UnresolvedReferenceTypeInfo unresolvedReference = new UnresolvedReferenceTypeInfo(nameSpaceset,new String[]{token.toString()});
            UnresolvedClassReferenceInfo currentClassReference = buildDataManager.getCurrentClass()
                    .getClassReference();

            pushElement(new SingleIdentifierElement(token.toString(), currentClassReference, event
                    .getStartLine(), event.getStartColumn(), event.getEndLine(), event
                    .getEndColumn()));
        }
    }

    @Override
    protected boolean isTriggerToken(AstToken token) {
        return token.isIdentifier();
    }
}
