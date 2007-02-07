package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


/**
 * @author owner
 *
 */
public class ExpressionDescriptionBuilder extends ExpressionBuilder {

    /**
     * @param expressionManager
     */
    public ExpressionDescriptionBuilder(final ExpressionElementManager expressionManager,
            final BuildDataManager buildDataManager) {
        super(expressionManager);
        this.buildDataManager = buildDataManager;
    }

    @Override
    protected void afterExited(final AstVisitEvent event) {
        final ExpressionElement[] elements = this.getAvailableElements();

        assert (elements.length == 1) : "Illegal state: too many elements was remained.";

        if (elements[0] instanceof IdentifierElement) {
            final UnresolvedTypeInfo type = ((IdentifierElement) elements[0])
                    .resolveAsReferencedVariable(this.buildDataManager);
            this.pushElement(new TypeElement(type));
        } else {
            this.pushElement(elements[0]);
        }
    }

    @Override
    protected boolean isTriggerToken(final AstToken token) {
        return token.isExpression();
    }

    private final BuildDataManager buildDataManager;
}
