package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.AvailableNamespaceInfoSet;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFieldUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMemberCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableInfo;


/**
 * ビルダーが構築する情報を管理して，情報全体の整合性を取るインタフェース．
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
public interface BuildDataManager {
    
    /**
     * 構築中のクラスにフィールド情報を追加する
     * 
     * @param field
     */
    public void addField(UnresolvedFieldInfo field);

    /**
     * 構築中のメソッドにフィールド代入情報を追加する
     * 
     * @param usage
     */
    public void addFieldAssignment(UnresolvedFieldUsageInfo usage);

    /**
     * 構築中のメソッドにフィールド参照情報を追加する
     * 
     * @param usage
     */
    public void addFieldReference(UnresolvedFieldUsageInfo usage);

    /**
     * 構築中のメソッドにローカルパラメータ（for文中で宣言される変数のように，
     * 宣言された場所から次のブロックの終わりまでスコープが有効な変数）情報を追加する
     * 
     * @param localParameter
     */
    public void addLocalParameter(UnresolvedLocalVariableInfo localParameter);

    /**
     * 構築中のメソッドにローカル変数情報を追加する
     * 
     * @param localVariable
     */
    public void addLocalVariable(UnresolvedLocalVariableInfo localVariable);

    /**
     * 構築中のメソッドにメソッド呼び出し情報を追加する
     * @param memberCall
     */
    public void addMethodCall(UnresolvedMemberCallInfo memberCall);

    /**
     * 構築中のメソッドに引数情報を追加する
     * 
     * @param parameter
     */
    public void addMethodParameter(UnresolvedParameterInfo parameter);

    
    /**
     * 構築中のデータに適切に型パラメータをセットする．
     * @param typeParameter　セットする型パラメータ
     */
    public void addTypeParameger(UnresolvedTypeParameterInfo typeParameter);
    
    
    /**
     * 現在のブロックスコープ内で有効な名前エイリアスを追加する
     * 
     * @param aliase
     * @param realName
     */
    public void addUsingAliase(String aliase, String[] realName);

    /**
     * 現在のブロックスコープ内で有効な，名前空間利用情報を追加する
     * 
     * @param nameSpace
     */
    public void addUsingNameSpace(String[] nameSpace);

    /**
     * クラス構築が終了する時に呼ばれる．
     * 構築中の
     * 
     * @return
     */
    public UnresolvedClassInfo endClassDefinition();

    public UnresolvedMethodInfo endMethodDefinition();

    public UnresolvedBlockInfo endInnerBlockDefinition();
    
    public void endScopedBlock();

    public void enterClassBlock();

    public void enterMethodBlock();

    public AvailableNamespaceInfoSet getAvailableNameSpaceSet();

    public AvailableNamespaceInfoSet getAvailableAliasSet();

    public AvailableNamespaceInfoSet getAllAvaliableNames();

    public String[] getAliasedName(String name);

    public int getAnonymousClassCount(UnresolvedClassInfo classInfo);

    public UnresolvedClassInfo getCurrentClass();

    public String[] getCurrentNameSpace();

    public UnresolvedMethodInfo getCurrentMethod();

    public UnresolvedVariableInfo getCurrentScopeVariable(String name);
    
    public UnresolvedTypeParameterInfo getTypeParameter(String name);

    public boolean hasAlias(String name);

    public void reset();

    public String[] popNameSpace();

    public void pushNewNameSpace(String[] nameSpace);

    public String[] resolveAliase(String[] name);

    public void startScopedBlock();

    public void startClassDefinition(UnresolvedClassInfo classInfo);

    public void startMethodDefinition(UnresolvedMethodInfo methodInfo);
    
    public void startInnerBlockDefinition(UnresolvedBlockInfo blockInfo);

}
