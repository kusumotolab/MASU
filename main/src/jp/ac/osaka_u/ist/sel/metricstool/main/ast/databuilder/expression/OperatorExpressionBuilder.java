package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.OperatorToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayElementUsage;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


public class OperatorExpressionBuilder extends ExpressionBuilder {

    public OperatorExpressionBuilder(final ExpressionElementManager expressionManager,
            final BuildDataManager buildManager) {
        super(expressionManager);
        this.buildDataManager = buildManager;
    }

    @Override
    protected void afterExited(final AstVisitEvent event) {
        final AstToken token = event.getToken();
        if (token instanceof OperatorToken) {
            this.buildOperatorElement((OperatorToken) token);
        }
    }

    protected void buildOperatorElement(final OperatorToken token) {
        final int term = token.getTermCount();
        final boolean assignmentLeft = token.isAssignmentOperator();
        final boolean referenceLeft = token.isLeftTermIsReferencee();
        final UnresolvedTypeInfo type = token.getSpecifiedResultType();
        final int[] typeSpecifiedTermIndexes = token.getTypeSpecifiedTermIndexes();

        final ExpressionElement[] elements = this.getAvailableElements();

        assert (term > 0 && term == elements.length) : "Illegal state: unexpected element count.";

        final UnresolvedTypeInfo[] termTypes = new UnresolvedTypeInfo[elements.length];

        if (elements[0] instanceof IdentifierElement) {
            final IdentifierElement leftElement = (IdentifierElement) elements[0];
            if (referenceLeft) {
                termTypes[0] = leftElement.resolveAsReferencedVariable(this.buildDataManager);
            }

            if (assignmentLeft) {
                termTypes[0] = leftElement.resolveAsAssignmetedVariable(this.buildDataManager);
            }
        } else {
            termTypes[0] = elements[0].getType();
        }

        for (int i = 1; i < term; i++) {
            if (elements[i] instanceof IdentifierElement) {
                termTypes[i] = ((IdentifierElement) elements[i])
                        .resolveAsReferencedVariable(this.buildDataManager);
            } else {
                termTypes[i] = elements[i].getType();
            }
        }

        UnresolvedTypeInfo resultType = null;
        if (null != type) {
            resultType = type;
        } else if (token.equals(OperatorToken.ARRAY)) {
            UnresolvedTypeInfo ownerType;
            if (elements[0] instanceof IdentifierElement) {
                ownerType = ((IdentifierElement) elements[0])
                        .resolveAsReferencedVariable(this.buildDataManager);
            } else {
                ownerType = elements[0].getType();
            }
            resultType = new UnresolvedArrayElementUsage(ownerType);
        } else {
            for (int i = 0; i < typeSpecifiedTermIndexes.length; i++) {
                resultType = termTypes[typeSpecifiedTermIndexes[i]];
                if (null != resultType){
                    break;
                }
            }
        }
        
        assert (null != resultType) : "Illegal state: operation resultType was not decided.";

        this.pushElement(TypeElement.getInstance(resultType));
    }

    @Override
    protected boolean isTriggerToken(final AstToken token) {
        return token instanceof OperatorToken;
    }

    private final BuildDataManager buildDataManager;
 
}
