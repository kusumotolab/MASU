package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決メソッド呼び出しを保存するためのクラス
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedMethodCall implements UnresolvedTypeInfo {

    /**
     * メソッド呼び出しが実行される変数の型，メソッド名を与えてオブジェクトを初期化
     * 
     * @param ownerClassType メソッド呼び出しが実行される変数の型
     * @param methodName メソッド名
     */
    public UnresolvedMethodCall(final UnresolvedTypeInfo ownerClassType, final String methodName,
            final boolean constructor) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == ownerClassType) || (null == methodName)) {
            throw new NullPointerException();
        }

        this.ownerClassType = ownerClassType;
        this.methodName = methodName;
        this.constructor = constructor;
        this.typeParameterUsages = new LinkedList<UnresolvedTypeParameterUsage>();
        this.parameterTypes = new LinkedList<UnresolvedTypeInfo>();
    }

    /**
     * 型パラメータ使用を追加する
     * 
     * @param typeParameterUsage 追加する型パラメータ使用
     */
    public void addTypeParameterUsage(final UnresolvedTypeParameterUsage typeParameterUsage) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeParameterUsage) {
            throw new NullPointerException();
        }

        this.typeParameterUsages.add(typeParameterUsage);
    }

    /**
     * 引数の型を追加
     * 
     * @param typeInfo
     */
    public void addParameterType(final UnresolvedTypeInfo typeInfo) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == typeInfo) {
            throw new NullPointerException();
        }

        this.parameterTypes.add(typeInfo);
    }

    /**
     * 引数の List を返す
     * 
     * @return 引数の List
     */
    public List<UnresolvedTypeInfo> getParameterTypes() {
        return Collections.unmodifiableList(this.parameterTypes);
    }

    /**
     * 型パラメータ使用の List を返す
     * 
     * @return 型パラメータ使用の List
     */
    public List<UnresolvedTypeParameterUsage> getTypeParameterUsages() {
        return Collections.unmodifiableList(this.typeParameterUsages);
    }

    /**
     * メソッド呼び出しが実行される変数の型を返す
     * 
     * @return メソッド呼び出しが実行される変数の型
     */
    public UnresolvedTypeInfo getOwnerClassType() {
        return this.ownerClassType;
    }

    /**
     * コンストラクタかどうかを返す
     * 
     * @return コンストラクタである場合は true，そうでない場合は false
     */
    public boolean isConstructor() {
        return this.constructor;
    }

    /**
     * メソッド名を返す
     * 
     * @return メソッド名
     */
    public String getMethodName() {
        return this.methodName;
    }

    /**
     * このメソッド呼び出しの返り値の型を返す
     * 
     * @return このメソッド呼び出しの返り値の型
     */
    public String getTypeName() {
        return UnresolvedTypeInfo.UNRESOLVED;
    }

    /**
     * メソッド呼び出しが実行される変数の型を保存するための変数
     */
    private final UnresolvedTypeInfo ownerClassType;

    /**
     * メソッド名を保存するための変数
     */
    private final String methodName;

    /**
     * 型パラメータ使用を保存するための変数
     */
    private final List<UnresolvedTypeParameterUsage> typeParameterUsages;

    /**
     * 引数を保存するための変数
     */
    private final List<UnresolvedTypeInfo> parameterTypes;

    /**
     * 呼び出しがコンストラクタかどうかを保存するための変数
     */
    private final boolean constructor;

}
