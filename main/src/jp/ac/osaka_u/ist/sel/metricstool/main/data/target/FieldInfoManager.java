package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * フィールド情報を管理するクラス． FieldInfo を要素として持つ．
 * 
 * @author y-higo
 * 
 */
public final class FieldInfoManager implements Iterable<FieldInfo> {

    /**
     * フィールド情報を管理しているインスタンスを返す． シングルトンパターンを持ちている．
     * 
     * @return フィールド情報を管理しているインスタンス
     */
    public static FieldInfoManager getInstance() {
        if (FIELD_INFO_DATA == null) {
            FIELD_INFO_DATA = new FieldInfoManager();
        }
        return FIELD_INFO_DATA;
    }

    /**
     * フィールド情報を追加する
     * 
     * @param fieldInfo 追加するフィールド情報
     */
    public void add(final FieldInfo fieldInfo) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == fieldInfo) {
            throw new NullPointerException();
        }

        this.fieldInfos.add(fieldInfo);
    }

    /**
     * フィールド情報の Iterator を返す．この Iterator は unmodifiable であり，変更操作を行うことはできない．
     */
    public Iterator<FieldInfo> iterator() {
        SortedSet<FieldInfo> unmodifiableFieldInfos = Collections
                .unmodifiableSortedSet(this.fieldInfos);
        return unmodifiableFieldInfos.iterator();
    }

    /**
     * 
     * コンストラクタ． シングルトンパターンで実装しているために private がついている．
     */
    private FieldInfoManager() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.fieldInfos = new TreeSet<FieldInfo>();
    }

    /**
     * 
     * シングルトンパターンを実装するための変数．
     */
    private static FieldInfoManager FIELD_INFO_DATA = null;

    /**
     * 
     * フィールド情報 (FieldInfo) を格納する変数．
     */
    private final SortedSet<FieldInfo> fieldInfos;
}
