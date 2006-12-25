package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DefaultBuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;

public class JavaBuildManager extends DefaultBuildDataManager{
    public void enterClassBlock(){
        super.enterClassBlock();
        
        UnresolvedClassInfo classInfo = getCurrentClass();
        if (classInfo.getSuperClasses().isEmpty()){
            classInfo.addSuperClass(OBJECT);
        }
    }
    
    private static final UnresolvedClassInfo OBJECT = new UnresolvedClassInfo();
    
    static{
        OBJECT.setClassName("Object");
        OBJECT.setNamespace(new String[]{"java","lang"});
        OBJECT.setPublicVisible(true);
        OBJECT.setInheritanceVisible(true);
        OBJECT.setNamespaceVisible(true);
        OBJECT.setPrivateVibible(false);
    }
}
