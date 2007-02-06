package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.CompoundIdentifierBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.FieldOrMethodElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.IdentifierElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.InstanceSpecificElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.TypeElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.AvailableNamespaceInfoSet;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsage;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;

public class JavaCompoundIdentifierBuilder extends CompoundIdentifierBuilder{
    public JavaCompoundIdentifierBuilder(ExpressionElementManager expressionManager, BuildDataManager buildManager) {
        super(expressionManager, buildManager);
        this.buildDataManager = buildManager;
    }
    
    @Override
    protected void buildCompoundIdentifierElement() {
        ExpressionElement[] elements = getAvailableElements();
        
        assert(2 == elements.length) : "Illega state: two element must be usable.";
        
        ExpressionElement left = elements[0];
        ExpressionElement right = elements[1];
        if (right.equals(JavaExpressionElement.CLASS)){
            pushElement(new TypeElement(JAVA_LANG_CLASS));
        } else if (right.equals(InstanceSpecificElement.THIS)){
            UnresolvedClassInfo classInfo = getSpecifiedOuterClass((IdentifierElement)left);
            
            if (classInfo != null){
                pushElement(new TypeElement(classInfo));
            }
        } else if (left.equals(JavaExpressionElement.SUPER)){
            if (right instanceof IdentifierElement){
                UnresolvedClassInfo classInfo = buildDataManager.getCurrentClass();
                UnresolvedTypeInfo superClassType = classInfo.getSuperClasses().iterator().next();
                pushElement(new FieldOrMethodElement(superClassType,((IdentifierElement)right).getName()));
            }
        } else if (right.equals(JavaExpressionElement.SUPER)){
            UnresolvedClassInfo classInfo = null;
            if (left instanceof IdentifierElement){
                classInfo = getSpecifiedOuterClass((IdentifierElement)left);
            } else if (left instanceof TypeElement && left.getType() instanceof UnresolvedClassInfo) {
                classInfo = (UnresolvedClassInfo)left.getType();
            } else{
                assert(false) : "Illegal state: current class  is illegal type.";
            }
            
            UnresolvedTypeInfo superClassType = classInfo.getSuperClasses().iterator().next();
            if (classInfo != null){
                pushElement(new TypeElement(superClassType));
            }
        } else {
            super.buildCompoundIdentifierElement();
        }
    }
    
    private UnresolvedClassInfo getSpecifiedOuterClass(IdentifierElement identifier){
        String name = identifier.getName();
        UnresolvedClassInfo classInfo = buildDataManager.getCurrentClass();
        while(null != classInfo && !name.equals(classInfo.getClassName())){
            classInfo = classInfo.getOuterClass();
        }
        
        assert(null != classInfo) : "Illegal state: unknown class was specified.";
        
        return classInfo;
    }
    
    private final static UnresolvedEntityUsage JAVA_LANG_CLASS =
        new UnresolvedEntityUsage(new AvailableNamespaceInfoSet(),
                new String[]{"java","lang","Class"});
    
    private final BuildDataManager buildDataManager;
    
}
