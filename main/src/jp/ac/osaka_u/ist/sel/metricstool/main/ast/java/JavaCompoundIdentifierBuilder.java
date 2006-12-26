package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.CompoundIdentifierBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.IdentifierElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.InstanceSpecificElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.TypeElement;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;

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
        if (elements[1].equals(JavaExpressionElement.CLASS)){
            pushElement(new TypeElement(classType));
        } else if (right.equals(InstanceSpecificElement.THIS)){
            
            IdentifierElement leftIdentifier = (IdentifierElement)left;
            String name = leftIdentifier.getName();
            
            UnresolvedClassInfo classInfo = buildDataManager.getCurrentClass();
            while(null != classInfo && !name.equals(classInfo.getClassName())){
                classInfo = classInfo.getOuterClass();
            }
            
            if (classInfo != null){
                pushElement(new TypeElement(classInfo));
            } else {
                assert(false) : "Illegal state: unknown class was specified by this.";
            }
        } else {
            super.buildCompoundIdentifierElement();
        }
    }
    
    private final static UnresolvedClassInfo classType = new UnresolvedClassInfo();
    
    static {
        classType.setClassName("Class");
        classType.setNamespace(new String[]{"java", "lang"});
        classType.setPublicVisible(true);
        classType.setInheritanceVisible(true);
        classType.setNamespaceVisible(true);
    }
    
    private final BuildDataManager buildDataManager;
    
}