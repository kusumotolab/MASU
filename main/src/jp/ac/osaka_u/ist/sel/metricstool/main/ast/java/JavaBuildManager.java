package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;

import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DefaultBuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.AvailableNamespaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.AvailableNamespaceInfoSet;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedReferenceTypeInfo;

public class JavaBuildManager extends DefaultBuildDataManager{
    
    @Override
    public void addField(UnresolvedFieldInfo field) {
        if (isInInterface()){
            field.setPublicVisible(true);
            field.setInheritanceVisible(true);
            field.setNamespaceVisible(true);
            field.setPrivateVibible(false);
            field.setInstanceMember(false);
        }
        
        super.addField(field);
    }
    
    @Override
    public UnresolvedClassInfo endClassDefinition(){
        classOrInterfaceStack.pop();
        return super.endClassDefinition();
    }
    
    @Override
    public UnresolvedMethodInfo endMethodDefinition() {
        UnresolvedMethodInfo method = super.endMethodDefinition();
        if (isInInterface()){
            method.setPublicVisible(true);
            method.setInheritanceVisible(true);
            method.setNamespaceVisible(true);
            method.setPrivateVibible(false);
        }
        return method;
    }
    
    @Override
    public void enterClassBlock(){
        super.enterClassBlock();
        
        UnresolvedClassInfo classInfo = getCurrentClass();
        if (classInfo.getSuperClasses().isEmpty()){
            classInfo.addSuperClass(OBJECT);
        }
    }
    
    @Override
    public void startClassDefinition(UnresolvedClassInfo classInfo) {
        super.startClassDefinition(classInfo);
        classOrInterfaceStack.push(CLASS_OR_INTERFACE.CLASS);
    }
    
    public void toInterface(){
        if (!classOrInterfaceStack.isEmpty()){
            classOrInterfaceStack.pop();
            classOrInterfaceStack.push(CLASS_OR_INTERFACE.INTERFACE);
        }
    }
    
    public boolean isInInterface(){
        if (classOrInterfaceStack.isEmpty())
            return false;
        return classOrInterfaceStack.peek().equals(CLASS_OR_INTERFACE.INTERFACE);
    }
    
    @Override
    public AvailableNamespaceInfoSet getAvailableNameSpaceSet() {
        AvailableNamespaceInfoSet result = super.getAvailableNameSpaceSet();
        result.add(JAVA_LANG);
        return result;
    }

    private static final AvailableNamespaceInfo JAVA_LANG = new AvailableNamespaceInfo(new String[]{"java","lang"},true);
    
    private static final UnresolvedReferenceTypeInfo OBJECT = new UnresolvedReferenceTypeInfo(
            new AvailableNamespaceInfoSet(),new String[]{"java","lang","Object"});
    
    private static enum CLASS_OR_INTERFACE{CLASS,INTERFACE}
    
    private final Stack<CLASS_OR_INTERFACE> classOrInterfaceStack = new Stack<CLASS_OR_INTERFACE>();

    
}
