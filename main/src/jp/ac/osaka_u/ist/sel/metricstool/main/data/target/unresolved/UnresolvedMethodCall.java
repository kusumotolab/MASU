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
public final class UnresolvedMethodCall implements Comparable<UnresolvedMethodCall> {

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
     * メソッド呼び出しの順序関係を定義する
     * 
     * @methodCall 比較対象メソッド呼び出し
     * @return メソッド呼び出しの順序
     */
    public int compareTo(final UnresolvedMethodCall methodCall) {

        if (null == methodCall) {
            throw new NullPointerException();
        }

        // メソッド名での比較
        final String methodName = this.getMethodName();
        final String correspondMethodName = methodCall.getMethodName();
        final int methodNameOrder = methodName.compareTo(correspondMethodName);
        if (0 != methodNameOrder) {
            return methodNameOrder;

        } else {

            // 引数の数での比較
            final List<UnresolvedTypeInfo> parameterTypes = this.getParameterTypes();
            final List<UnresolvedTypeInfo> correspondParameterTypes = methodCall
                    .getParameterTypes();
            if (parameterTypes.size() > correspondParameterTypes.size()) {
                return 1;
            } else if (parameterTypes.size() < correspondParameterTypes.size()) {
                return -1;
            } else {

                // 引数の型を前から順番に見て，比較を行う
                final Iterator<UnresolvedTypeInfo> typeIterator = parameterTypes.iterator();
                final Iterator<UnresolvedTypeInfo> correspondTypeIterator = correspondParameterTypes
                        .iterator();
                while (typeIterator.hasNext() && correspondTypeIterator.hasNext()) {
                    final UnresolvedTypeInfo typeInfo = typeIterator.next();
                    final UnresolvedTypeInfo correspondTypeInfo = correspondTypeIterator.next();
                    final String typeInfoName = typeInfo.getName();
                    final String correspondTypeInfoName = correspondTypeInfo.getName();
                    final int stringOrder = typeInfoName.compareTo(correspondTypeInfoName);
                    if (0 != stringOrder) {
                        return stringOrder;
                    }
                }

                // メソッド呼び出しが行われている変数の型で比較
                final String[] ownerClassName = this.getOwnerClassName();
                final String[] correspondOwnerClassName = methodCall.getOwnerClassName();
                if (ownerClassName.length > correspondOwnerClassName.length) {
                    return 1;
                } else if (ownerClassName.length < correspondOwnerClassName.length) {
                    return -1;
                } else {
                    for (int i = 0; i < ownerClassName.length; i++) {
                        final int stringOrder = ownerClassName[i]
                                .compareTo(correspondOwnerClassName[i]);
                        if (0 != stringOrder) {
                            return stringOrder;
                        }
                    }

                    return 0;
                }
            }
        }
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
