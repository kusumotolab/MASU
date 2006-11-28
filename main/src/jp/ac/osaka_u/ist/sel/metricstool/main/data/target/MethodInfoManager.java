package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * メソッド情報を管理するクラス． methodInfo を要素として持つ．
 * 
 * @author y-higo
 * 
 */
public final class MethodInfoManager implements Iterable<MethodInfo> {

    /**
     * メソッド情報を管理しているインスタンスを返す． シングルトンパターンを持ちている．
     * 
     * @return メソッド情報を管理しているインスタンス
     */
    public static MethodInfoManager getInstance() {
        return SINGLETON;
    }

    /**
     * 
     * @param methodInfo 追加するメソッド情報
     */
    public void add(final MethodInfo methodInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == methodInfo) {
            throw new NullPointerException();
        }

        this.methodInfos.add(methodInfo);
    }

    /**
     * メソッド情報の Iterator を返す．この Iterator は unmodifiable であり，変更操作を行うことはできない．
     */
    public Iterator<MethodInfo> iterator() {
        Set<MethodInfo> unmodifiableMethodInfos = Collections.unmodifiableSet(this.methodInfos);
        return unmodifiableMethodInfos.iterator();
    }

    /**
     * 持っているメソッド情報の個数を返す.
     * @return メソッドの個数
     */
    public int getMethodCount() {
        return this.methodInfos.size();
    }

    /**
     * 
     * コンストラクタ． シングルトンパターンで実装しているために private がついている．
     */
    private MethodInfoManager() {
        this.methodInfos = new TreeSet<MethodInfo>();
    }

    /**
     * 
     * シングルトンパターンを実装するための変数．
     */
    private static final MethodInfoManager SINGLETON = new MethodInfoManager();

    /**
     * 
     * メソッド情報 (methodInfo) を格納する変数．
     */
    private final SortedSet<MethodInfo> methodInfos;
}
