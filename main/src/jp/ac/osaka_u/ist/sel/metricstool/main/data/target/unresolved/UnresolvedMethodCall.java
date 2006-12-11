package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決メソッド呼び出しを保存するためのクラス
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedMethodCall {

    /**
     * メソッド呼び出しが実行される変数の型名，メソッド名を与えてオブジェクトを初期化
     * 
     * @param ownerClassName メソッド呼び出しが実行される変数の型名
     * @param methodName メソッド名
     */
    public UnresolvedMethodCall(final String[] ownerClassName, final String methodName,
            final boolean constructor) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == ownerClassName) || (null == methodName)) {
            throw new NullPointerException();
        }

        this.ownerClassName = ownerClassName;
        this.methodName = methodName;
        this.constructor = constructor;
        this.parameterTypes = new LinkedList<UnresolvedTypeInfo>();
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
     * 引数のリストを返す
     * 
     * @return 引数のリスト
     */
    public List<UnresolvedTypeInfo> getParameterTypes() {
        return Collections.unmodifiableList(this.parameterTypes);
    }

    /**
     * メソッド呼び出しが実行される変数の型名を返す
     * 
     * @return メソッド呼び出しが実行される変数の型
     */
    public String[] getOwnerClassName() {
        return this.ownerClassName;
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
     * メソッド呼び出しが実行される変数の型名を保存するための変数
     */
    private final String[] ownerClassName;

    /**
     * メソッド名を保存するための変数
     */
    private final String methodName;

    /**
     * 引数を保存するための変数
     */
    private final List<UnresolvedTypeInfo> parameterTypes;

    /**
     * 呼び出しがコンストラクタかどうかを保存するための変数
     */
    private final boolean constructor;

}
