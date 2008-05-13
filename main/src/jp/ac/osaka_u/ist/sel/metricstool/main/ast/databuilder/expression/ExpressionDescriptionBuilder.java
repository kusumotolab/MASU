package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableUsageInfo;


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
        super(expressionManager, buildDataManager);
    }

    @Override
    protected void afterExited(final AstVisitEvent event) {
        final ExpressionElement[] elements = this.getAvailableElements();

        assert (elements.length == 1) : "Illegal state: too many elements were remained.";

        if (elements.length == 1) {
            if (elements[0] instanceof IdentifierElement) {
                final UnresolvedVariableUsageInfo<? extends VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> variableUsage = ((IdentifierElement) elements[0])
                        .resolveAsReferencedVariable(this.buildDataManager);
                this.pushElement(UsageElement.getInstance(variableUsage));
            } else {
                this.pushElement(elements[0]);
            }
        }
    }

    @Override
    protected boolean isTriggerToken(final AstToken token) {
        return token.isExpression();
    }

}
