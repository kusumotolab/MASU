package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * クラス情報を管理するクラス． ClassInfo を要素として持つ．
 * 
 * @author y-higo
 * 
 */
public final class ClassInfoManager implements Iterable<ClassInfo> {

    /**
     * クラス情報を管理しているインスタンスを返す． シングルトンパターンを持ちている．
     * 
     * @return クラス情報を管理しているインスタンス
     */
    public static ClassInfoManager getInstance() {
        if (CLASS_INFO_DATA == null) {
            CLASS_INFO_DATA = new ClassInfoManager();
        }
        return CLASS_INFO_DATA;
    }

    /**
     * 
     * @param classInfo 追加するクラス情報 (classInfo)
     */
    public void add(final ClassInfo classInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfo) {
            throw new NullPointerException();
        }

        this.classInfos.add(classInfo);
    }

    /**
     * クラス情報の Iterator を返す．この Iterator は unmodifiable であり，変更操作を行うことはできない．
     */
    public Iterator<ClassInfo> iterator() {
        Set<ClassInfo> unmodifiableClassInfos = Collections.unmodifiableSet(this.classInfos);
        return unmodifiableClassInfos.iterator();
    }

    /**
     * 持っているクラス情報の個数を返す.
     * @return クラスの個数
     */
    public int getClassCount() {
        return this.classInfos.size();
    }

    /**
     * 
     * コンストラクタ． シングルトンパターンで実装しているために private がついている．
     */
    private ClassInfoManager() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.classInfos = new TreeSet<ClassInfo>();
    }

    /**
     * 
     * シングルトンパターンを実装するための変数．
     */
    private static ClassInfoManager CLASS_INFO_DATA = null;

    /**
     * 
     * クラス情報 (ClassInfo) を格納する変数．
     */
    private final SortedSet<ClassInfo> classInfos;
}
