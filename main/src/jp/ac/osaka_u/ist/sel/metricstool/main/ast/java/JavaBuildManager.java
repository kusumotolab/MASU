package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DefaultBuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.AvailableNamespaceInfoSet;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedReferenceTypeInfo;

public class JavaBuildManager extends DefaultBuildDataManager{
    public void enterClassBlock(){
        super.enterClassBlock();
        
        UnresolvedClassInfo classInfo = getCurrentClass();
        if (classInfo.getSuperClasses().isEmpty()){
            classInfo.addSuperClass(OBJECT);
        }
    }
    
    private static final UnresolvedReferenceTypeInfo OBJECT = new UnresolvedReferenceTypeInfo(
            new AvailableNamespaceInfoSet(),new String[]{"java","lang","Object"});
}
