package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.BuiltinTypeToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.ConstantToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassImportStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLiteralUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeParameterTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedUnitInfo;


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

        final UnresolvedUnitInfo<? extends UnitInfo> outerUnit = this.buildDataManager
                .getCurrentUnit();
        assert outerUnit != null : "outer unit does not be stored";

        final int fromLine = event.getStartLine();
        final int fromColumn = event.getStartColumn();
        final int toLine = event.getEndLine();
        final int toColumn = event.getEndColumn();

        if (token.isTypeDescription()) {
            this.buildType(outerUnit, fromLine, fromColumn, toLine, toColumn);
        } else if (token.isArrayDeclarator()) {
            this.buildArrayType(outerUnit, fromLine, fromColumn, toLine, toColumn);
        } else if (token.isTypeArgument()) {
            buildTypeArgument();
        } else if (token.isTypeWildcard()) {
            buildTypeWildCard(outerUnit, fromLine, fromColumn, toLine, toColumn);
        } else if (token instanceof BuiltinTypeToken) {
            this.buildBuiltinType((BuiltinTypeToken) token, outerUnit);
        } else if (token instanceof ConstantToken) {
            this.buildConstantElement((ConstantToken) token, outerUnit, fromLine, fromColumn,
                    toLine, toColumn);
        }
    }

    protected void buildArrayType(final UnresolvedUnitInfo<? extends UnitInfo> outerUnit,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        final ExpressionElement[] elements = this.getAvailableElements();

        assert (elements.length > 0) : "Illegal state: type description was not found.";

        TypeElement typeElement = null;
        if (elements.length > 0) {
            if (elements[0] instanceof IdentifierElement) {
                final UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> referenceType = this
                        .buildReferenceType(elements);
                typeElement = new TypeElement(UnresolvedArrayTypeInfo.getType(referenceType, 1),
                        outerUnit, fromLine, fromColumn, toLine, toColumn);
            } else if (elements[0] instanceof TypeElement) {
                typeElement = ((TypeElement) elements[0]).getArrayDimensionInclementedInstance(
                        outerUnit, fromLine, fromColumn, toLine, toColumn);
            }
        }

        if (null != typeElement) {
            this.pushElement(typeElement);
        }
    }

    protected void buildType(final UnresolvedUnitInfo<? extends UnitInfo> outerUnit,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        final ExpressionElement[] elements = this.getAvailableElements();

        assert (elements.length > 0) : "Illegal state: type description was not found.";

        if (elements.length > 0) {
            if (elements[0] instanceof IdentifierElement) {
                this.pushElement(new TypeElement(this.buildReferenceType(elements), outerUnit,
                        fromLine, fromColumn, toLine, toColumn));
            } else if (elements[0] instanceof TypeElement) {
                assert (elements.length == 1) : "Illegal state: unexpected type arguments.";
                this.pushElement(elements[0]);
            }
        }
    }

    /**
     * 型引数を表す式要素を構築する．
     */
    protected void buildTypeArgument() {
        //利用できる全要素を取得
        final ExpressionElement[] elements = this.getAvailableElements();

        assert (elements.length > 0) : "Illegal state: type arguments were not created.";

        assert (elements.length == 1) : "Illegal state: too many type arguments.";

        if (elements.length > 0) {
            ExpressionElement element = elements[elements.length - 1];

            assert (element instanceof TypeElement) : "Illegal state: unspecified type argument.";

            if (element instanceof TypeElement) {
                //一番最後が型要素だったら型引数要素を作成
                TypeArgumentElement argument = new TypeArgumentElement(
                        ((TypeElement) element).getType());
                //それ以外の要素を全部もとに戻す．
                int size = elements.length - 1;
                for (int i = 0; i < size; i++) {
                    pushElement(elements[i]);
                }
                //最後に型引数要素を登録する
                pushElement(argument);
            }
        }
    }

    protected void buildTypeWildCard(final UnresolvedUnitInfo<? extends UnitInfo> outerUnit,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        final UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> upperBounds = getTypeUpperBounds();

        assert (null != upperBounds);

        pushElement(new TypeElement(upperBounds, outerUnit, fromLine, fromColumn, toLine, toColumn));
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

        //一応元に戻してみる
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

        UnresolvedTypeParameterTypeInfo typeParameter = null;
        if (typeName.length == 1) {
            typeParameter = this.buildDataManager.getTypeParameterType(typeName[0]);
        }

        if (null != typeParameter) {
            return typeParameter;
        }

        //TODO 型パラメータに型引数が付く言語があったらそれを登録する仕組みを作る必要があるかも

        UnresolvedClassTypeInfo resultType = new UnresolvedClassTypeInfo(
                UnresolvedClassImportStatementInfo.getClassImportStatements(this.buildDataManager
                        .getAllAvaliableNames()), typeName);

        for (int i = 1; i < elements.length; i++) {
            assert (elements[i] instanceof TypeArgumentElement) : "Illegal state: type argument was unexpected type";
            TypeArgumentElement typeArugument = (TypeArgumentElement) elements[i];

            // TODO C#などは参照型以でも型引数を指定できるので、その対処が必要かも           
            assert typeArugument.getType() instanceof UnresolvedReferenceTypeInfo : "Illegal state: type argument was not reference type.";
            resultType.addTypeArgument((UnresolvedReferenceTypeInfo<?>) typeArugument.getType());
        }

        return resultType;
    }

    protected void buildBuiltinType(final BuiltinTypeToken token,
            final UnresolvedUnitInfo<?> outerUnit) {
        this.pushElement(TypeElement.getBuiltinTypeElement(token, outerUnit));
    }

    protected void buildConstantElement(final ConstantToken token,
            final UnresolvedUnitInfo<? extends UnitInfo> outerUnit, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        final UnresolvedLiteralUsageInfo literal = new UnresolvedLiteralUsageInfo(token.toString(),
                token.getType());
        literal.setOuterUnit(outerUnit);
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
