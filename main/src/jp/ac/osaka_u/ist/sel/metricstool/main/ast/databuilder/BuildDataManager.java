package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.AvailableNamespaceInfoSet;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldUsage;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMethodCall;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableInfo;

/**
 * @author kou-tngt
 *
 */
public interface BuildDataManager{
    public void addField(UnresolvedFieldInfo field);
    public void addFieldAssignment(UnresolvedFieldUsage usage);
    public void addFieldReference(UnresolvedFieldUsage usage);
    public void addLocalParameter(UnresolvedLocalVariableInfo localParameter);
    public void addLocalVariable(UnresolvedLocalVariableInfo localVariable);
    public void addMethodCall(UnresolvedMethodCall methodCall);
    public void addMethodParameter(UnresolvedParameterInfo parameter);
    
    public void addUsingAliase(String aliase, String[] realName);
    public void addUsingNameSpace(String[] nameSpace);
    
    public UnresolvedClassInfo endClassDefinition();
    public UnresolvedMethodInfo endMethodDefinition();
    
    public void endScopedBlock();
    
    public void enterClassBlock();
    public void enterMethodBlock();
    
    public AvailableNamespaceInfoSet getAvailableNameSpaceSet();
    public String[] getAliasedName(String name);
    public int getAnonymousClassCount(UnresolvedClassInfo classInfo);
    
    public UnresolvedClassInfo getCurrentClass();
    public String[] getCurrentNameSpace();
    public UnresolvedMethodInfo getCurrentMethod();
    
    public UnresolvedVariableInfo getCurrentScopeVariable(String name);
    
    public boolean hasAlias(String name);
    public void reset();
    
    public String[] popNameSpace();
    public void pushNewNameSpace(String[] nameSpace);
    
    public String[] resolveAliase(String[] name);
    
    public void startScopedBlock();
    
    public void startClassDefinition(UnresolvedClassInfo classInfo);
    public void startMethodDefinition(UnresolvedMethodInfo methodInfo);
}
