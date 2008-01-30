package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ConstructorCallBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.TypeElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.UsageElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.AvailableNamespaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.AvailableNamespaceInfoSet;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


public class JavaConstructorCallBuilder extends ConstructorCallBuilder {

    public JavaConstructorCallBuilder(ExpressionElementManager expressionManager,
            BuildDataManager buildDataManager) {
        super(expressionManager, buildDataManager);
        this.buildDataManager = buildDataManager;
    }

    @Override
    protected void afterExited(AstVisitEvent event) {
        super.afterExited(event);

        AstToken token = event.getToken();
        if (token.equals(JavaAstToken.CONSTRUCTOR_CALL)) {
            buildInnerConstructorCall(buildDataManager.getCurrentClass());
        } else if (token.equals(JavaAstToken.SUPER_CONSTRUCTOR_CALL)) {
            buildSuperConstructorCall(buildDataManager.getCurrentClass().getSuperClasses()
                    .iterator().next());
        }
    }

    protected void buildNewConstructorCall() {
        ExpressionElement[] elements = getAvailableElements();

        assert (elements.length > 0) : "Illegal state: constructor element not found.";

        assert (elements[0] instanceof TypeElement) : "Illegal state: constructor owner is not type.";
        TypeElement typeElement = (TypeElement) elements[0];
        if (isJavaArrayInstantiation(elements)) {
            //配列のnew文はこっちで処理する
            UnresolvedTypeInfo type = typeElement.getType();
            pushElement(TypeElement.getInstance(resolveArrayElement(type, elements)));
        } else {
            //それ以外は普通に処理する
            super.buildNewConstructorCall();
        }
    }

    protected void buildInnerConstructorCall(UnresolvedClassInfo currentClass) {
        ExpressionElement[] elements = getAvailableElements();
        
        AvailableNamespaceInfoSet namespaces = new AvailableNamespaceInfoSet();
        AvailableNamespaceInfo namespace = new AvailableNamespaceInfo(currentClass.getNamespace(), false);
        namespaces.add(namespace);
        UnresolvedReferenceTypeInfo referenceType = new UnresolvedReferenceTypeInfo(namespaces, currentClass.getFullQualifiedName());

        UnresolvedConstructorCallInfo constructorCall = new UnresolvedConstructorCallInfo(referenceType);
        resolveParameters(constructorCall, elements, 0);
        pushElement(UsageElement.getInstance(constructorCall));
        buildDataManager.addMethodCall(constructorCall);
    }

    protected void buildSuperConstructorCall(UnresolvedReferenceTypeInfo superClass) {
        ExpressionElement[] elements = getAvailableElements();

        int argStartIndex = 0;

        //String[] superClassReferenceName = superClass.getFullReferenceName();
        String[] superClassReferenceName = superClass.getReferenceName();
        String className = superClassReferenceName[superClassReferenceName.length - 1];

        if (elements.length > 0 && elements[0] instanceof TypeElement) {
            //苦し紛れの特別処理
            //elementsの1個目がUnresolvedReferenceTypeInfoでありかつsuperClassのアウタークラスであるなら
            //それはOuterClass.this.super()という呼び出し形式であるとみなす

            UnresolvedTypeInfo type = ((TypeElement) elements[0]).getType();
            if (type instanceof UnresolvedReferenceTypeInfo) {
                String[] firstElementReference = ((UnresolvedReferenceTypeInfo) type).getReferenceName();
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

        UnresolvedConstructorCallInfo constructorCall = new UnresolvedConstructorCallInfo(superClass);
        resolveParameters(constructorCall, elements, argStartIndex);
        pushElement(UsageElement.getInstance(constructorCall));
        buildDataManager.addMethodCall(constructorCall);

    }

    protected boolean isJavaArrayInstantiation(ExpressionElement[] elements) {
        for (ExpressionElement element : elements) {
            if (element.equals(JavaArrayInstantiationElement.getInstance())) {
                return true;
            }
        }
        return false;
    }

    protected UnresolvedArrayTypeInfo resolveArrayElement(UnresolvedTypeInfo type,
            ExpressionElement[] elements) {
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
    protected boolean isTriggerToken(AstToken token) {
        return super.isTriggerToken(token) || token.equals(JavaAstToken.CONSTRUCTOR_CALL)
                || token.equals(JavaAstToken.SUPER_CONSTRUCTOR_CALL);
    }

    private final BuildDataManager buildDataManager;
}
