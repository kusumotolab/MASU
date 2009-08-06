package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.BuiltinTypeToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.ConstantToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLiteralUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeParameterInfo;


/**
 * @author kou-tngt
 *
 */
public abstract class TypeElementBuilder extends ExpressionBuilder {

    /**
     * @param expressionManager
     */
    public TypeElementBuilder(final ExpressionElementManager expressionManager,
            final BuildDataManager buildDataManager) {
        super(expressionManager, buildDataManager);
    }

    @Override
    protected void afterExited(final AstVisitEvent event) {
        final AstToken token = event.getToken();

        final int fromLine = event.getStartLine();
        final int fromColumn = event.getStartColumn();
        final int toLine = event.getEndLine();
        final int toColumn = event.getEndColumn();

        if (token.isTypeDescription()) {
            this.buildType(fromLine, fromColumn, toLine, toColumn);
        } else if (token.isArrayDeclarator()) {
            this.buildArrayType(fromLine, fromColumn, toLine, toColumn);
        } else if (token.isTypeArgument()) {
            buildTypeArgument();
        } else if (token.isTypeWildcard()) {
            buildTypeWildCard(fromLine, fromColumn, toLine, toColumn);
        } else if (token instanceof BuiltinTypeToken) {
            this.buildBuiltinType((BuiltinTypeToken) token);
        } else if (token instanceof ConstantToken) {
            this
                    .buildConstantElement((ConstantToken) token, fromLine, fromColumn, toLine,
                            toColumn);
        }
    }

    protected void buildArrayType(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        final ExpressionElement[] elements = this.getAvailableElements();

        assert (elements.length > 0) : "Illegal state: type description was not found.";

        TypeElement typeElement = null;
        if (elements.length > 0) {
            if (elements[0] instanceof IdentifierElement) {
                final UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> referenceType = this
                        .buildReferenceType(elements);
                typeElement = new TypeElement(UnresolvedArrayTypeInfo.getType(referenceType, 1),
                        fromLine, fromColumn, toLine, toColumn);
            } else if (elements[0] instanceof TypeElement) {
                typeElement = ((TypeElement) elements[0]).getArrayDimensionInclementedInstance(
                        fromLine, fromColumn, toLine, toColumn);
            }
        }

        if (null != typeElement) {
            this.pushElement(typeElement);
        }
    }

    protected void buildType(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        final ExpressionElement[] elements = this.getAvailableElements();

        assert (elements.length > 0) : "Illegal state: type description was not found.";

        if (elements.length > 0) {
            if (elements[0] instanceof IdentifierElement) {
                this.pushElement(new TypeElement(this.buildReferenceType(elements), fromLine,
                        fromColumn, toLine, toColumn));
            } else if (elements[0] instanceof TypeElement) {
                assert (elements.length == 1) : "Illegal state: unexpected type arguments.";
                this.pushElement(elements[0]);
            }
        }
    }

    /**
     * Œ^ˆø”‚ğ•\‚·®—v‘f‚ğ\’z‚·‚éD
     */
    protected void buildTypeArgument() {
        //—˜—p‚Å‚«‚é‘S—v‘f‚ğæ“¾
        final ExpressionElement[] elements = this.getAvailableElements();

        assert (elements.length > 0) : "Illegal state: type arguments were not created.";

        assert (elements.length == 1) : "Illegal state: too many type arguments.";

        if (elements.length > 0) {
            ExpressionElement element = elements[elements.length - 1];

            assert (element instanceof TypeElement) : "Illegal state: unspecified type argument.";

            if (element instanceof TypeElement) {
                //ˆê”ÔÅŒã‚ªŒ^—v‘f‚¾‚Á‚½‚çŒ^ˆø”—v‘f‚ğì¬
                TypeArgumentElement argument = new TypeArgumentElement(((TypeElement) element)
                        .getType());
                //‚»‚êˆÈŠO‚Ì—v‘f‚ğ‘S•”‚à‚Æ‚É–ß‚·D
                int size = elements.length - 1;
                for (int i = 0; i < size; i++) {
                    pushElement(elements[i]);
                }
                //ÅŒã‚ÉŒ^ˆø”—v‘f‚ğ“o˜^‚·‚é
                pushElement(argument);
            }
        }
    }

    protected void buildTypeWildCard(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        final UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> upperBounds = getTypeUpperBounds();

        assert (null != upperBounds);

        pushElement(new TypeElement(upperBounds, fromLine, fromColumn, toLine, toColumn));
    }

    protected UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> getTypeUpperBounds() {
        final ExpressionElement[] elements = this.getAvailableElements();

        UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> resultType = null;

        if (elements.length > 0) {

            assert (elements.length == 1) : "Illegal state: too many type upper bounds.";

            ExpressionElement element = elements[elements.length - 1];

            assert (element instanceof TypeElement) : "Illegal state: upper bounds type was not type element.";

            if (element instanceof TypeElement) {
                final TypeElement typeElement = (TypeElement) element;

                assert typeElement.getType() instanceof UnresolvedReferenceTypeInfo : "Illegal state: upper bounds type was not reference type.";
                if (typeElement.getType() instanceof UnresolvedReferenceTypeInfo) {
                    resultType = (UnresolvedReferenceTypeInfo<?>) typeElement.getType();
                }
            }
        }

        //ˆê‰Œ³‚É–ß‚µ‚Ä‚İ‚é
        int size = elements.length - 1;
        for (int i = 0; i < size; i++) {
            pushElement(elements[i]);
        }

        if (null == resultType) {
            resultType = this.getDefaultTypeUpperBound();
        }

        return resultType;
    }

    protected abstract UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> getDefaultTypeUpperBound();

    protected UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> buildReferenceType(
            final ExpressionElement[] elements) {
        assert (elements.length > 0);
        assert (elements[0] instanceof IdentifierElement);

        IdentifierElement element = (IdentifierElement) elements[0];
        final String[] typeName = element.getQualifiedName();

        UnresolvedTypeParameterInfo typeParameter = null;
        if (typeName.length == 1) {
            typeParameter = this.buildDataManager.getTypeParameter(typeName[0]);
        }

        if (null != typeParameter) {
            return typeParameter;
        }

        //TODO Œ^ƒpƒ‰ƒ[ƒ^‚ÉŒ^ˆø”‚ª•t‚­Œ¾Œê‚ª‚ ‚Á‚½‚ç‚»‚ê‚ğ“o˜^‚·‚éd‘g‚İ‚ğì‚é•K—v‚ª‚ ‚é‚©‚à

        UnresolvedClassTypeInfo resultType = new UnresolvedClassTypeInfo(
                UnresolvedClassImportStatementInfo.getClassImportStatements(this.buildDataManager
                        .getAllAvaliableNames()), typeName);

        for (int i = 1; i < elements.length; i++) {
            assert (elements[i] instanceof TypeArgumentElement) : "Illegal state: type argument was unexpected type";
            TypeArgumentElement typeArugument = (TypeArgumentElement) elements[i];

            // TODO C#‚È‚Ç‚ÍQÆŒ^ˆÈ‚Å‚àŒ^ˆø”‚ğw’è‚Å‚«‚é‚Ì‚ÅA‚»‚Ì‘Îˆ‚ª•K—v‚©‚à           
            assert typeArugument.getType() instanceof UnresolvedReferenceTypeInfo : "Illegal state: type argument was not reference type.";
            resultType.addTypeArgument((UnresolvedReferenceTypeInfo<?>) typeArugument.getType());
        }

        return resultType;
    }

    protected void buildBuiltinType(final BuiltinTypeToken token) {
        this.pushElement(TypeElement.getBuiltinTypeElement(token));
    }

    protected void buildConstantElement(final ConstantToken token, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        final UnresolvedLiteralUsageInfo literal = new UnresolvedLiteralUsageInfo(token.toString(),
                token.getType());
        literal.setFromLine(fromLine);
        literal.setFromColumn(fromColumn);
        literal.setToLine(toLine);
        literal.setToColumn(toColumn);

        this.pushElement(new UsageElement(literal));
    }

    @Override
    protected boolean isTriggerToken(final AstToken token) {
        return token.isBuiltinType() || token.isTypeDescription() || token.isConstant()
                || token.isArrayDeclarator() || token.isTypeArgument() || token.isTypeWildcard();
    }

}
