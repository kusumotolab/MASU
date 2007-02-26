package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.BuiltinTypeToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.ConstantToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedReferenceTypeInfo;


/**
 * @author kou-tngt
 *
 */
public class TypeElementBuilder extends ExpressionBuilder {

    /**
     * @param expressionManager
     */
    public TypeElementBuilder(final ExpressionElementManager expressionManager,
            final BuildDataManager buildManager) {
        super(expressionManager);

        if (null == buildManager) {
            throw new NullPointerException("buildManager is null.");
        }

        this.buildManager = buildManager;
    }

    @Override
    protected void afterExited(final AstVisitEvent event) {
        final AstToken token = event.getToken();
        if (token.isTypeDescription()) {
            this.buildType();
        } else if (token.isArrayDeclarator()) {
            this.buildArrayType();
        } else if (token instanceof BuiltinTypeToken) {
            this.buildBuiltinType((BuiltinTypeToken) token);
        } else if (token instanceof ConstantToken) {
            this.buildConstantElement((ConstantToken) token);
        }
    }

    protected void buildArrayType() {
        final ExpressionElement[] elements = this.getAvailableElements();

        assert (elements.length == 1) : "Illegal state: type description was not found.";

        TypeElement typeElement = null;
        if (elements.length == 1) {
            if (elements[0] instanceof IdentifierElement) {
                final UnresolvedReferenceTypeInfo referenceType = this.buildReferenceType((IdentifierElement) elements[0]);
                typeElement = TypeElement.getInstance(UnresolvedArrayTypeInfo.getType(
                        referenceType, 1));
            } else if (elements[0] instanceof TypeElement) {
                typeElement = ((TypeElement) elements[0]).getArrayDimensionInclementedInstance();
            }
        }

        if (null != typeElement) {
            this.pushElement(typeElement);
        }
    }

    protected void buildType() {
        final ExpressionElement[] elements = this.getAvailableElements();

        assert (elements.length == 1) : "Illegal state: type description was not found.";

        if (elements.length == 1) {
            if (elements[0] instanceof IdentifierElement) {
                this.pushElement(TypeElement
                        .getInstance(this.buildReferenceType((IdentifierElement) elements[0])));
            } else if (elements[0] instanceof TypeElement) {
                this.pushElement(elements[0]);
            }
        }
    }

    protected UnresolvedReferenceTypeInfo buildReferenceType(final IdentifierElement element) {
        final String[] typeName = element.getQualifiedName();
        final String[] trueTypeName = this.buildManager.resolveAliase(typeName);
        return new UnresolvedReferenceTypeInfo(this.buildManager.getAvailableNameSpaceSet(),
                trueTypeName);
    }

    protected void buildBuiltinType(final BuiltinTypeToken token) {
        this.pushElement(TypeElement.getInstance(token.getType()));
    }

    protected void buildConstantElement(final ConstantToken token) {
        this.pushElement(TypeElement.getInstance(token.getType()));
    }

    @Override
    protected boolean isTriggerToken(final AstToken token) {
        return token.isBuiltinType() || token.isTypeDescription()
                || token.isConstant() || token.isArrayDeclarator();
    }

    private final BuildDataManager buildManager;

}
