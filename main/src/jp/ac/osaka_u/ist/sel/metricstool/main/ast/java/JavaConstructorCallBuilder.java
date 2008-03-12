package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ConstructorCallBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.TypeElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.UsageElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.AvailableNamespaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


public class JavaConstructorCallBuilder extends ConstructorCallBuilder {

    public JavaConstructorCallBuilder(final ExpressionElementManager expressionManager,
            final BuildDataManager buildDataManager) {
        super(expressionManager, buildDataManager);
        this.buildDataManager = buildDataManager;
    }

    @Override
    protected void afterExited(final AstVisitEvent event) {
        super.afterExited(event);

        final AstToken token = event.getToken();
        if (token.equals(JavaAstToken.CONSTRUCTOR_CALL)) {
            buildInnerConstructorCall(buildDataManager.getCurrentClass());
        } else if (token.equals(JavaAstToken.SUPER_CONSTRUCTOR_CALL)) {
            buildSuperConstructorCall(buildDataManager.getCurrentClass().getSuperClasses()
                    .iterator().next());
        }
    }

    @Override
    protected void buildNewConstructorCall() {
        ExpressionElement[] elements = getAvailableElements();

        assert (elements.length > 0) : "Illegal state: constructor element not found.";

        assert (elements[0] instanceof TypeElement) : "Illegal state: constructor owner is not type.";
        TypeElement typeElement = (TypeElement) elements[0];
        if (isJavaArrayInstantiation(elements)) {
            //配列のnew文はこっちで処理する
            UnresolvedArrayTypeInfo arrayType = resolveArrayElement(typeElement.getType(), elements);
            UnresolvedConstructorCallInfo constructorCall = new UnresolvedConstructorCallInfo(arrayType);
            pushElement(UsageElement.getInstance(constructorCall));
        } else {
            //それ以外は普通に処理する
            super.buildNewConstructorCall();
        }
    }

    protected void buildInnerConstructorCall(final UnresolvedClassInfo currentClass) {
        final ExpressionElement[] elements = getAvailableElements();
        
        final Set<AvailableNamespaceInfo> namespaces = new HashSet<AvailableNamespaceInfo>();
        final AvailableNamespaceInfo namespace = new AvailableNamespaceInfo(currentClass.getFullQualifiedName(), false);
        namespaces.add(namespace);
        final UnresolvedClassTypeInfo referenceType = new UnresolvedClassTypeInfo(namespaces, currentClass.getFullQualifiedName());

        final UnresolvedConstructorCallInfo constructorCall = new UnresolvedConstructorCallInfo(referenceType);
        resolveParameters(constructorCall, elements, 0);
        pushElement(UsageElement.getInstance(constructorCall));
        buildDataManager.addMethodCall(constructorCall);
    }

    protected void buildSuperConstructorCall(final UnresolvedClassTypeInfo superClass) {
        final ExpressionElement[] elements = getAvailableElements();

        int argStartIndex = 0;

        //String[] superClassReferenceName = superClass.getFullReferenceName();
        final String[] superClassReferenceName = superClass.getReferenceName();
        final String className = superClassReferenceName[superClassReferenceName.length - 1];

        if (elements.length > 0 && elements[0] instanceof TypeElement) {
            //苦し紛れの特別処理
            //elementsの1個目がUnresolvedReferenceTypeInfoでありかつsuperClassのアウタークラスであるなら
            //それはOuterClass.this.super()という呼び出し形式であるとみなす

            final UnresolvedTypeInfo type = ((TypeElement) elements[0]).getType();
            if (type instanceof UnresolvedClassTypeInfo) {
                // TODO UnresolvedReferenceTypeにすべきかも 要テスト
                final String[] firstElementReference = ((UnresolvedClassTypeInfo) type).getReferenceName();
                        //.getFullReferenceName();
                if (firstElementReference.length < superClassReferenceName.length) {
                    boolean match = true;
                    for (int i = 0; i < firstElementReference.length; i++) {
                        if (!firstElementReference[i].equals(superClassReferenceName[i])) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        argStartIndex = 1;
                    }
                }
            }
        }

        assert (null != className) : "Illegal state: unexpected ownerClass type.";

        final UnresolvedConstructorCallInfo constructorCall = new UnresolvedConstructorCallInfo(superClass);
        resolveParameters(constructorCall, elements, argStartIndex);
        pushElement(UsageElement.getInstance(constructorCall));
        buildDataManager.addMethodCall(constructorCall);

    }

    protected boolean isJavaArrayInstantiation(final ExpressionElement[] elements) {
        for (ExpressionElement element : elements) {
            if (element.equals(JavaArrayInstantiationElement.getInstance())) {
                return true;
            }
        }
        return false;
    }

    protected UnresolvedArrayTypeInfo resolveArrayElement(final UnresolvedTypeInfo type,
            final ExpressionElement[] elements) {
        int i = 1;
        int dimension = 0;
        while (i < elements.length
                && elements[i].equals(JavaArrayInstantiationElement.getInstance())) {
            dimension++;
            i++;
        }

        if (dimension > 0) {
            return UnresolvedArrayTypeInfo.getType(type, dimension);
        } else {
            return null;
        }
    }

    @Override
    protected boolean isTriggerToken(final AstToken token) {
        return super.isTriggerToken(token) || token.equals(JavaAstToken.CONSTRUCTOR_CALL)
                || token.equals(JavaAstToken.SUPER_CONSTRUCTOR_CALL);
    }

    private final BuildDataManager buildDataManager;
}
