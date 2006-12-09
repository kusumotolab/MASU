package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * メソッド情報を管理するクラス． methodInfo を要素として持つ．
 * 
 * @author y-higo
 * 
 */
public final class MethodInfoManager {

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
    public void add(final TargetMethodInfo methodInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == methodInfo) {
            throw new NullPointerException();
        }

        this.targetMethodInfos.add(methodInfo);
    }

    /**
     * 
     * @param methodInfo 追加するメソッド情報
     */
    public void add(final ExternalMethodInfo methodInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == methodInfo) {
            throw new NullPointerException();
        }

        this.externalMethodInfos.add(methodInfo);
    }
    
    /**
     * 対象メソッド情報のSortedSetを返す．
     * 
     * @return 対象メソッド情報のSortedSet
     */
    public SortedSet<TargetMethodInfo> getTargetMethodInfos() {
        return Collections.unmodifiableSortedSet(this.targetMethodInfos);
    }

    /**
     * 外部メソッド情報のSortedSetを返す．
     * 
     * @return 外部メソッド情報のSortedSet
     */
    public SortedSet<ExternalMethodInfo> getExternalMethodInfos() {
        return Collections.unmodifiableSortedSet(this.externalMethodInfos);
    }
    
    /**
     * 持っている対象メソッド情報の個数を返す.
     * 
     * @return 対象メソッドの個数
     */
    public int getTargetMethodCount() {
        return this.targetMethodInfos.size();
    }

    /**
     * 持っている外部メソッド情報の個数を返す.
     * 
     * @return 外部メソッドの個数
     */
    public int getExternalMethodCount() {
        return this.externalMethodInfos.size();
    }
    
    /**
     * 
     * コンストラクタ． シングルトンパターンで実装しているために private がついている．
     */
    private MethodInfoManager() {
        this.targetMethodInfos = new TreeSet<TargetMethodInfo>();
        this.externalMethodInfos = new TreeSet<ExternalMethodInfo>();
    }

    /**
     * 
     * シングルトンパターンを実装するための変数．
     */
    private static final MethodInfoManager SINGLETON = new MethodInfoManager();

    /**
     * 
     * 対象メソッド情報を格納する変数．
     */
    private final SortedSet<TargetMethodInfo> targetMethodInfos;
    
    /**
     * 
     * 外部メソッド情報を格納する変数．
     */
    private final SortedSet<ExternalMethodInfo> externalMethodInfos;
}
