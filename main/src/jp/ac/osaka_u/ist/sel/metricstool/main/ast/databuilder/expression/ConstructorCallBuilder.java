package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedReferenceTypeInfo;


public class ConstructorCallBuilder extends ExpressionBuilder {

    public ConstructorCallBuilder(final ExpressionElementManager expressionManager,
            final BuildDataManager buildDataManager) {
        super(expressionManager, buildDataManager);
    }

    @Override
    protected void afterExited(final AstVisitEvent event) {
        final AstToken token = event.getToken();

        if (token.isInstantiation()) {
            buildNewConstructorCall(event.getStartLine(), event.getStartColumn(), event
                    .getEndLine(), event.getEndColumn());
        }

    }

    protected void buildNewConstructorCall(final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {
        final ExpressionElement[] elements = getAvailableElements();

        assert (elements.length > 0) : "Illegal state: constructor element not found.";
        assert (elements[0] instanceof TypeElement) : "Illegal state: constructor owner is not type.";

        if (elements.length > 0 && elements[0] instanceof TypeElement) {
            // TODO 配列のnew文の対処をすべき
            final TypeElement type = (TypeElement) elements[0];
            final UnresolvedReferenceTypeInfo referenceType = (UnresolvedReferenceTypeInfo) type
                    .getType();
            //String[] name = type.getFullReferenceName();

            final UnresolvedConstructorCallInfo constructorCall = new UnresolvedConstructorCallInfo(
                    referenceType);
            constructorCall.setFromLine(fromLine);
            constructorCall.setFromColumn(fromColumn);
            constructorCall.setToLine(toLine);
            constructorCall.setToColumn(toColumn);

            resolveParameters(constructorCall, elements, 1);
            pushElement(UsageElement.getInstance(constructorCall));
            this.buildDataManager.addMethodCall(constructorCall);
        }
    }

    protected void resolveParameters(final UnresolvedConstructorCallInfo constructorCall,
            final ExpressionElement[] elements, final int startIndex) {
        for (int i = startIndex; i < elements.length; i++) {
            final ExpressionElement element = elements[i];
            if (element instanceof IdentifierElement) {
                constructorCall.addArgument(((IdentifierElement) element)
                        .resolveAsReferencedVariable(this.buildDataManager));
            } else if (element instanceof TypeArgumentElement) {
                TypeArgumentElement typeArgument = (TypeArgumentElement) element;

                // TODO C# などの場合はプリミティブ型も型引数に指定可能
                assert typeArgument.getType() instanceof UnresolvedReferenceTypeInfo : "Illegal state; type argument was not reference type.";
                constructorCall.addTypeArgument((UnresolvedReferenceTypeInfo) typeArgument
                        .getType());
            } else {
                constructorCall.addArgument(element.getUsage());
            }
        }
    }

    @Override
    protected boolean isTriggerToken(final AstToken token) {
        return token.isInstantiation();
    }

}
