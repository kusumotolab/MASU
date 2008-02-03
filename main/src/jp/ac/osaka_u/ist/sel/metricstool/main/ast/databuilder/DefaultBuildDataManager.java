package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.AvailableNamespaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.AvailableNamespaceInfoSet;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMemberCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableInfo;


/**
 * ビルダーが構築する情報を管理して，情報全体の整合性を取るクラス.
 * 以下の3種類の機能を連携して行う.
 * 
 * 1. 構築中のデータに関する情報の管理，提供及び構築状態の管理
 * 
 * 2. 名前空間，エイリアス，変数などのスコープ管理
 * 
 * 3. クラス情報，メソッド情報，変数代入，変数参照，メソッド呼び出し情報などの登録作業の代行
 * 
 * @author kou-tngt
 *
 */
public class DefaultBuildDataManager implements BuildDataManager{

    public DefaultBuildDataManager() {
        innerInit();
    }

    public void reset() {
        innerInit();
    }

    public void addField(final UnresolvedFieldInfo field) {
        if (!this.classStack.isEmpty() && MODE.CLASS == this.mode) {
            this.classStack.peek().addDefinedField(field);
            addScopedVariable(field);
        }
    }
    
    public void addFieldAssignment(UnresolvedFieldUsageInfo usage) {
    	if (!this.methodStack.isEmpty()&& MODE.METHOD == this.mode){
            this.methodStack.peek().addFieldUsage(usage);
        }else if(!this.blockStack.isEmpty() && MODE.BLOCK == this.mode){
    		this.blockStack.peek().addFieldUsage(usage);
    	}
    }

    public void addFieldReference(UnresolvedFieldUsageInfo usage) {
        if (!this.methodStack.isEmpty()&& MODE.METHOD == this.mode){
            this.methodStack.peek().addFieldUsage(usage);
        }else if(!this.blockStack.isEmpty() && MODE.BLOCK == this.mode){
    		this.blockStack.peek().addFieldUsage(usage);
    	}
    }

    public void addLocalParameter(final UnresolvedLocalVariableInfo localParameter){
        if (!this.methodStack.isEmpty() && MODE.METHOD == this.mode) {
            this.methodStack.peek().addLocalVariable(localParameter);
            addNextScopedVariable(localParameter);
        }
    }

    public void addLocalVariable(final UnresolvedLocalVariableInfo localVariable) {
        if (!this.methodStack.isEmpty() && MODE.METHOD == this.mode) {
            this.methodStack.peek().addLocalVariable(localVariable);
            addScopedVariable(localVariable);
        } else if(!this.blockStack.isEmpty() && MODE.BLOCK == this.mode){
        	this.blockStack.peek().addLocalVariable(localVariable);
        	addScopedVariable(localVariable);
        }
    }
    
    public void addMethodCall(UnresolvedMemberCallInfo memberCall) {
        if (!this.methodStack.isEmpty() && MODE.METHOD == this.mode){
            this.methodStack.peek().addMemberCall(memberCall);
        } else if(!this.blockStack.isEmpty() && MODE.BLOCK == this.mode){
        	this.blockStack.peek().addMemberCall(memberCall);
        }
    }

    public void addMethodParameter(final UnresolvedParameterInfo parameter) {
        if (!this.methodStack.isEmpty()&& MODE.METHOD == this.mode) {
            final UnresolvedMethodInfo method = this.methodStack.peek();
            method.adParameter(parameter);
            addNextScopedVariable(parameter);
        }
    }

    /**
     * 現在のブロックスコープに変数を追加する.
     * @param var 追加する変数
     */
    private void addScopedVariable(UnresolvedVariableInfo var) {
        if (!scopeStack.isEmpty()) {
            scopeStack.peek().addVariable(var);
        }
    }

    /**
     * 現在から次のブロック終了までスコープが有効な変数を追加する.
     * @param var　追加する変数
     */
    private void addNextScopedVariable(UnresolvedVariableInfo var) {
        nextScopedVariables.add(var);
    }
    
    public void addTypeParameger(UnresolvedTypeParameterInfo typeParameter){
        if (!this.modeStack.isEmpty() && MODE.CLASS == this.mode){
            if (!this.classStack.isEmpty()){
                classStack.peek().addTypeParameter(typeParameter);
            }
        } else if (!this.modeStack.isEmpty() && MODE.METHOD == this.mode){
            if (!this.methodStack.isEmpty()){
                methodStack.peek().addTypeParameter(typeParameter);
            }
        }
    }

    public void addUsingAliase(final String aliase, final String[] realName) {
        if (!this.scopeStack.isEmpty()) {
            final BlockScope scope = this.scopeStack.peek();
            scope.addAlias(aliase, realName);
            
            //名前のエイリアス情報が変化したのでキャッシュをリセット
            aliaseNameSetCache = null;
            allAvaliableNameSetCache = null;
        }
    }

    public void addUsingNameSpace(final String[] nameSpace) {
        if (!this.scopeStack.isEmpty()) {
            final BlockScope scope = this.scopeStack.peek();
            scope.addUsingNameSpace(nameSpace);
            
            //名前空間情報が変化したのでキャッシュをリセット
            availableNameSpaceSetCache = null;
            allAvaliableNameSetCache = null;
        }
    }

    public void endScopedBlock() {
        if (!this.scopeStack.isEmpty()) {
            this.scopeStack.pop();
            nextScopedVariables.clear();
            
            //名前情報キャッシュをリセット
            aliaseNameSetCache = null;
            availableNameSpaceSetCache = null;
            allAvaliableNameSetCache = null;
        }
    }

    public UnresolvedClassInfo endClassDefinition() {
        this.restoreMode();
        
        if (this.classStack.isEmpty()) {
            return null;
        } else {
            final UnresolvedClassInfo classInfo = this.classStack.pop();
            
            //外側のクラスがない場合にだけ登録を行う
            if (this.classStack.isEmpty()){
                UnresolvedClassInfoManager.getInstance().addClass(classInfo);
            }
            
            if (!this.methodStack.isEmpty()) {
                //TODO methodStack.peek().addInnerClass(classInfo);
            }
            
            return classInfo;
        }
    }

    public UnresolvedMethodInfo endMethodDefinition() {
        this.restoreMode();
        
        if (this.methodStack.isEmpty()) {
            return null;
        } else {
            final UnresolvedMethodInfo methodInfo = this.methodStack.pop();

            nextScopedVariables.clear();
            
            if (!this.classStack.isEmpty()) {
                this.classStack.peek().addDefinedMethod(methodInfo);
            }
            
            return methodInfo;
        }
    }
    
    public UnresolvedBlockInfo endInnerBlockDefinition(){
    	this.restoreMode();
    	
    	if (this.blockStack.isEmpty()) {
    		return null;
    	} else {
    		final UnresolvedBlockInfo blockInfo = this.blockStack.pop();
    		UnresolvedLocalSpaceInfo parentInfo = null;
    		if(this.blockStack.isEmpty()){
    			if(!this.methodStack.isEmpty()){
    				parentInfo = this.methodStack.peek();
    			}
    		}else{
    			parentInfo = this.blockStack.peek();
    		}
    		
    		if (null != parentInfo) {
    			parentInfo.addChildSpaceInfo(blockInfo);
    		}
    		
    		return blockInfo;
    	}
    }
    
    public void enterClassBlock(){
        int size = classStack.size();
        if (size > 1){
            UnresolvedClassInfo current = classStack.peek();
            UnresolvedClassInfo outer = classStack.get(size-2);
            outer.addInnerClass(current);
            current.setOuterClass(outer);
        }
    }
    
    public void enterMethodBlock(){
        
    }
    
    public AvailableNamespaceInfoSet getAllAvaliableNames(){
//      nullじゃなければ変化してないのでキャッシュ使いまわし
        if (null != allAvaliableNameSetCache){
            return allAvaliableNameSetCache;
        }
        
        AvailableNamespaceInfoSet resultSet = getAvailableAliasSet();
        for(AvailableNamespaceInfo info : getAvailableNameSpaceSet()){
            resultSet.add(info);
        }
        
        allAvaliableNameSetCache = resultSet;
        
        return resultSet;
    }

    public AvailableNamespaceInfoSet getAvailableNameSpaceSet() {
        //nullじゃなければ変化してないのでキャッシュ使いまわし
        if (null != availableNameSpaceSetCache){
            return availableNameSpaceSetCache;
        }
        
        final AvailableNamespaceInfoSet result = new AvailableNamespaceInfoSet();
        //まず先に今の名前空間を登録
        if (null == currentNameSpaceCache){
            currentNameSpaceCache = new AvailableNamespaceInfo(getCurrentNameSpace(),true);
        }
        result.add(currentNameSpaceCache);
        
        final int size = this.scopeStack.size();
        for (int i = size - 1; i >= 0; i--) {//Stackの実体はVectorなので後ろからランダムアクセス
            final BlockScope scope = this.scopeStack.get(i);
            final AvailableNamespaceInfoSet scopeLocalNameSpaceSet = scope.getAvailableNameSpaces();
            for (final AvailableNamespaceInfo info : scopeLocalNameSpaceSet) {
                result.add(info);
            }
        }
        availableNameSpaceSetCache = result;
        
        return result;
    }
    
    public AvailableNamespaceInfoSet getAvailableAliasSet(){
        //nullじゃなければ変化してないのでキャッシュ使いまわし
        if (null != aliaseNameSetCache){
            return aliaseNameSetCache;
        }
        
        final AvailableNamespaceInfoSet result = new AvailableNamespaceInfoSet();
        final int size = this.scopeStack.size();
        for (int i = size - 1; i >= 0; i--) {//Stackの実体はVectorなので後ろからランダムアクセス
            final BlockScope scope = this.scopeStack.get(i);
            final AvailableNamespaceInfoSet scopeLocalNameSpaceSet = scope.getAvailableAliases();
            for (final AvailableNamespaceInfo info : scopeLocalNameSpaceSet) {
                result.add(info);
            }
        }
        
        aliaseNameSetCache = result;
        
        return result;
    }

    public String[] getAliasedName(final String alias) {
        final int size = this.scopeStack.size();
        for (int i = size - 1; i >= 0; i--) {//Stackの実体はVectorなので後ろからランダムアクセス
            final BlockScope scope = this.scopeStack.get(i);
            if (scope.hasAlias(alias)) {
                return scope.replaceAlias(alias);
            }
        }
        return EMPTY_NAME;
    }

    public UnresolvedClassInfo getCurrentClass() {
        if (this.classStack.isEmpty()) {
            return null;
        } else {
            return this.classStack.peek();
        }
    }

    public int getAnonymousClassCount(UnresolvedClassInfo classInfo) {
        if (null == classInfo) {
            throw new NullPointerException("classInfo is null.");
        }

        if (anonymousClassCountMap.containsKey(classInfo)) {
            int count = anonymousClassCountMap.get(classInfo);
            anonymousClassCountMap.put(classInfo, ++count);
            return count;
        } else {
            anonymousClassCountMap.put(classInfo, 1);
            return 1;
        }
    }

    public UnresolvedMethodInfo getCurrentMethod() {
        if (this.methodStack.isEmpty()) {
            return null;
        } else {
            return this.methodStack.peek();
        }
    }

    /**
     * 現在の名前空間名を返す．
     * 
     * @return
     */
    public String[] getCurrentNameSpace() {
        final List<String> nameSpaceList = new ArrayList<String>();

        for (final String[] nameSpace : this.nameSpaceStack) {
            for (final String nameSpaceString : nameSpace) {
                nameSpaceList.add(nameSpaceString);
            }
        } 

        return nameSpaceList.toArray(new String[nameSpaceList.size()]);
    }

    /**
     * スタックにつまれているクラスのクラス名も付けた名前空間を返す.
     * @return
     */
    public String[] getCurrentFullNameSpace() {
        final List<String> nameSpaceList = new ArrayList<String>();

        for (final String[] nameSpace : this.nameSpaceStack) {
            for (final String nameSpaceString : nameSpace) {
                nameSpaceList.add(nameSpaceString);
            }
        }

        for (final UnresolvedClassInfo classes : this.classStack) {
            final String className = classes.getClassName();
            nameSpaceList.add(className);
        }

        return nameSpaceList.toArray(new String[nameSpaceList.size()]);
    }
    
    public UnresolvedVariableInfo getCurrentScopeVariable(String name){
        
        for (UnresolvedVariableInfo var : nextScopedVariables){
            if (name.equals(var.getName())){
                return var;
            }
        }
        
        final int size = this.scopeStack.size();
        for (int i = size - 1; i >= 0; i--) {
            final BlockScope scope = this.scopeStack.get(i);
            if (scope.hasVariable(name)){
                return scope.getVariable(name);
            }
        }
        return null;
    }
    
    public UnresolvedTypeParameterInfo getTypeParameter(String name){
        for(int i = modeStack.size()-1, cli = classStack.size() -1, mei = methodStack.size()-1 ; i >= 0; i--){
            MODE mode = modeStack.get(i);
            
            if (MODE.CLASS == mode){
                assert(cli >= 0);
                if (cli >= 0){
                    UnresolvedClassInfo classInfo = classStack.get(cli--);
                    for(UnresolvedTypeParameterInfo param : classInfo.getTypeParameters()){
                        if (param.getName().equals(name)){
                            return param;
                        }
                    }
                }
            } else if (MODE.METHOD == mode){
                assert(mei >= 0);
                if (mei >= 0){
                    UnresolvedMethodInfo methodInfo = methodStack.get(mei--);
                    for(UnresolvedTypeParameterInfo param : methodInfo.getTypeParameters()){
                        if (param.getName().equals(name)){
                            return param;
                        }
                    }
                }
            } 
        }
        
        return null;
    }

    public boolean hasAlias(final String name) {
        final int size = this.scopeStack.size();
        for (int i = size - 1; i >= 0; i--) {
            final BlockScope scope = this.scopeStack.get(i);
            if (scope.hasAlias(name)) {
                return true;
            }
        }
        return false;
    }

    public void startScopedBlock() {
        BlockScope newScope = new BlockScope();
        this.scopeStack.push(newScope);
        
        for(UnresolvedVariableInfo var : nextScopedVariables){
            newScope.addVariable(var);
        }
    }

    public void pushNewNameSpace(final String[] nameSpace) {
        if (null == nameSpace) {
            throw new NullPointerException("nameSpace is null.");
        }

        if (0 == nameSpace.length) {
            throw new IllegalArgumentException("nameSpace has no entry.");
        }
        this.nameSpaceStack.push(nameSpace);
    }

    public String[] popNameSpace() {
        if (this.nameSpaceStack.isEmpty()) {
            return null;
        } else {
            return this.nameSpaceStack.pop();
        }
    }
    
    public String[] resolveAliase(String[] name){
        if (name == null){
            throw new NullPointerException("empty name.");
        }
        
        if (0 == name.length){
            throw new IllegalArgumentException("empty name.");
        }
        
        List<String> resolvedName = new ArrayList<String>();
        int startPoint = 0;
        if (hasAlias(name[0])){
            startPoint++;
            String[] aliasedName = getAliasedName(name[0]);
            for(String str: aliasedName){
                resolvedName.add(str);
            }
        }
        
        for(int i=startPoint; i < name.length; i++){
            resolvedName.add(name[i]);
        }
        
        return resolvedName.toArray(new String[resolvedName.size()]);
    }
    
    public void startClassDefinition(final UnresolvedClassInfo classInfo) {
        if (null == classInfo) {
            throw new NullPointerException("class info was null.");
        }
        
        classInfo.setNamespace(this.getCurrentFullNameSpace());

        this.classStack.push(classInfo);
        
        this.toClassMode();
    }

    public void startMethodDefinition(final UnresolvedMethodInfo methodInfo) {
        if (null == methodInfo) {
            throw new NullPointerException("method info was null.");
        }

        if (!this.classStack.isEmpty()) {
            UnresolvedClassInfo currentClass = this.classStack.peek();
            currentClass.addDefinedMethod(methodInfo);
            methodInfo.setOwnerClass(currentClass);
            if (methodInfo.isConstructor()){
                methodInfo.setReturnType(UnresolvedClassTypeInfo.getInstance(currentClass));
            }
        }
        
        this.toMethodMode();

        this.methodStack.push(methodInfo);

    }
    
    public void startInnerBlockDefinition(final UnresolvedBlockInfo blockInfo){
    	if(null == blockInfo){
    		throw new NullPointerException("block info was null.");
    	}
    	
    	if(!this.methodStack.isEmpty()){
    		// TODO ブロック文のownerブロックとownerメソッドを登録したほうが便利かも
    		UnresolvedMethodInfo currentMethod = getCurrentMethod();
    		//blockInfo.setOwnerMethod(currentMethod);
	    	if(!this.blockStack.isEmpty()){
	    		UnresolvedBlockInfo currentBlock = this.blockStack.peek();
	    		currentBlock.addInnerBlock(blockInfo);
	    		//blockInfo.setOwnerBlock(currentBlock);
	    	}else{
	    		currentMethod.addInnerBlock(blockInfo);
	    	}
    	}
    	
    	this.toBlockMode();
    	
    	this.blockStack.push(blockInfo);
    }
    
    protected void toClassMode(){
        this.modeStack.push(this.mode);
        this.mode = MODE.CLASS;
    }
    
    protected void toMethodMode(){
        this.modeStack.push(this.mode);
        this.mode = MODE.METHOD;
    }
    
    protected void toBlockMode(){
    	this.modeStack.push(this.mode);
    	this.mode = MODE.BLOCK;
    }
    
    protected void restoreMode(){
        if (!modeStack.isEmpty()){
            this.mode = modeStack.pop();
        }
    }
    
    protected static class BlockScope {
        private final Map<String,UnresolvedVariableInfo> variables = new LinkedHashMap<String,UnresolvedVariableInfo>();

//        private final Map<String, String[]> nameAliases = new LinkedHashMap<String, String[]>();
        private final Map<String,AvailableNamespaceInfo> nameAliases = new LinkedHashMap<String,AvailableNamespaceInfo>();

        private final AvailableNamespaceInfoSet availableNameSpaces = new AvailableNamespaceInfoSet();
        
        public void addVariable(final UnresolvedVariableInfo variable) {
            this.variables.put(variable.getName(),variable);
        }

        public void addAlias(final String alias, final String[] name) {
            if (name.length == 0 || (name.length == 1 && name[0] == alias)) {
                throw new IllegalArgumentException("Illegal name alias.");
            }

            final String[] tmp = new String[name.length];
            System.arraycopy(name, 0, tmp, 0, name.length);
            
            AvailableNamespaceInfo info = new AvailableNamespaceInfo(tmp,false);
            
            this.nameAliases.put(alias, info);
        }

        public void addUsingNameSpace(final String[] name) {
            final String[] tmp = new String[name.length];
            System.arraycopy(name, 0, tmp, 0, name.length);
            final AvailableNamespaceInfo info = new AvailableNamespaceInfo(tmp, true);
            this.availableNameSpaces.add(info);
        }

        public AvailableNamespaceInfoSet getAvailableNameSpaces() {
            return this.availableNameSpaces;
        }
        
        public AvailableNamespaceInfoSet getAvailableAliases(){
            AvailableNamespaceInfoSet resultSet = new AvailableNamespaceInfoSet();
            for(AvailableNamespaceInfo info : this.nameAliases.values()){
                resultSet.add(info);
            }
            return resultSet;
        }
        
        public UnresolvedVariableInfo getVariable(String name){
            return this.variables.get(name);
        }

        public boolean hasVariable(final String varName) {
            return this.variables.containsKey(varName);
        }

        public boolean hasAlias(final String alias) {
            return this.nameAliases.containsKey(alias);
        }

        public String[] replaceAlias(final String alias) {
            Set<String[]> cycleCheckSet = new HashSet<String[]>();

            String aliasString = alias;
            if (this.nameAliases.containsKey(aliasString)) {
                String[] result = this.nameAliases.get(aliasString).getImportName();
                cycleCheckSet.add(result);

                if (result.length == 1) {
                    aliasString = result[0];
                    while (this.nameAliases.containsKey(aliasString)) {
                        result = this.nameAliases.get(aliasString).getImportName();
                        if (result.length == 1) {
                            if (cycleCheckSet.contains(result)) {
                                return result;
                            } else {
                                cycleCheckSet.add(result);
                                aliasString = result[0];
                            }
                        } else {
                            return result;
                        }
                    }
                } else {
                    return result;
                }
            }
            return EMPTY_NAME;
        }
    }
    
    private void innerInit(){
        this.classStack.clear();
        this.methodStack.clear();
        this.nameSpaceStack.clear();
        this.scopeStack.clear();

        this.scopeStack.add(new BlockScope());
        
        aliaseNameSetCache = null;
        availableNameSpaceSetCache = null;
        allAvaliableNameSetCache = null;
        currentNameSpaceCache = null;
    }

    private static final String[] EMPTY_NAME = new String[0];

    private AvailableNamespaceInfoSet aliaseNameSetCache = null;
    private AvailableNamespaceInfoSet availableNameSpaceSetCache = null;
    private AvailableNamespaceInfoSet allAvaliableNameSetCache = null;
    private AvailableNamespaceInfo currentNameSpaceCache = null;
    
    private final Stack<BlockScope> scopeStack = new Stack<BlockScope>();

    private final Stack<String[]> nameSpaceStack = new Stack<String[]>();

    private final Stack<UnresolvedClassInfo> classStack = new Stack<UnresolvedClassInfo>();

    private final Stack<UnresolvedMethodInfo> methodStack = new Stack<UnresolvedMethodInfo>();
    
    private final Stack<UnresolvedBlockInfo> blockStack = new Stack<UnresolvedBlockInfo>();
    
    private final Set<UnresolvedVariableInfo> nextScopedVariables = new HashSet<UnresolvedVariableInfo>();

    private final Map<UnresolvedClassInfo, Integer> anonymousClassCountMap = new HashMap<UnresolvedClassInfo, Integer>();
    
    private MODE mode = MODE.INIT;
    private Stack<MODE> modeStack = new Stack<MODE>();
    
    private static enum MODE{INIT,BLOCK,METHOD,CLASS}
}
