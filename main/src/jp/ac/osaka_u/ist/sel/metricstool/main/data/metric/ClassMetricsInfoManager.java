package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * クラスメトリクスを管理するクラス．
 * 
 * @author y-higo
 * 
 */
public final class ClassMetricsInfoManager {

    /**
     * このクラスのインスタンスを返す．シングルトンパターンを用いている．
     * 
     * @return このクラスのインスタンス
     */
    public static ClassMetricsInfoManager getInstance() {
        return CLASS_METRICS_MAP;
    }

    /**
     * メトリクスが登録されているクラスのイテレータを返す．
     * 
     * @return メトリクスが登録されているクラスのイテレータ
     */
    public Iterator<ClassInfo> classInfoIterator() {
        Set<ClassInfo> unmodifiableClassInfoSet = Collections
                .unmodifiableSet(this.classMetricsInfos.keySet());
        return unmodifiableClassInfoSet.iterator();
    }

    /**
     * メトリクス情報一覧のイテレータを返す．
     * 
     * @return メトリクス情報のイテレータ
     */
    public Iterator<ClassMetricsInfo> classMetricsInfoIterator() {
        Collection<ClassMetricsInfo> unmodifiableClassMetricsInfoCollection = Collections
                .unmodifiableCollection(this.classMetricsInfos.values());
        return unmodifiableClassMetricsInfoCollection.iterator();
    }

    /**
     * 引数で指定されたクラスのメトリクス情報を返す． 引数で指定されたクラスのメトリクス情報が存在しない場合は， null を返す．
     * 
     * @param classInfo ほしいメトリクス情報のクラス
     * @return メトリクス情報
     */
    public ClassMetricsInfo get(final ClassInfo classInfo) {

        if (null == classInfo) {
            throw new NullPointerException();
        }

        return this.classMetricsInfos.get(classInfo);
    }

    /**
     * メトリクスを登録する
     * 
     * @param classInfo メトリクス計測対象のクラスオブジェクト
     * @param plugin メトリクスのプラグイン
     * @param value メトリクス値
     * @throws MetricAlreadyRegisteredException 登録しようとしているメトリクスが既に登録されている
     */
    public void putMetric(final ClassInfo classInfo, final AbstractPlugin plugin, final int value)
            throws MetricAlreadyRegisteredException {

        ClassMetricsInfo classMetricsInfo = this.classMetricsInfos.get(classInfo);

        // 対象クラスの classMetricsInfo が無い場合は，new して Map に登録する
        if (null == classMetricsInfo) {
            classMetricsInfo = new ClassMetricsInfo();
            this.classMetricsInfos.put(classInfo, classMetricsInfo);
        }

        classMetricsInfo.putMetric(plugin, value);
    }

    /**
     * クラスメトリクスマネージャのオブジェクトを生成する． シングルトンパターンを用いているため，private がついている．
     * 
     */
    private ClassMetricsInfoManager() {
        MetricsToolSecurityManager.getInstance().checkAccess();
        this.classMetricsInfos = Collections
                .synchronizedMap(new TreeMap<ClassInfo, ClassMetricsInfo>());
    }

    /**
     * このクラスのオブジェクトを管理している定数．シングルトンパターンを用いている．
     */
    private static final ClassMetricsInfoManager CLASS_METRICS_MAP = new ClassMetricsInfoManager();

    /**
     * クラスメトリクスのマップを保存するための変数
     */
    private final Map<ClassInfo, ClassMetricsInfo> classMetricsInfos;
}
