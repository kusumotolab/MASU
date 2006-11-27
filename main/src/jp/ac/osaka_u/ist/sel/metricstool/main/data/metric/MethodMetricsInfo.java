package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.PluginManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;


/**
 * メソッドメトリクスを登録するためのデータクラス
 * 
 * @author y-higo
 * 
 */
public final class MethodMetricsInfo {

    /**
     * 引数なしコンストラクタ．
     */
    public MethodMetricsInfo(final MethodInfo methodInfo) {

        if (null == methodInfo) {
            throw new NullPointerException();
        }

        this.methodInfo = methodInfo;
        this.methodMetrics = Collections.synchronizedMap(new TreeMap<AbstractPlugin, Float>());
    }

    /**
     * このメトリクス情報のメソッドを返す
     * 
     * @return このメトリクス情報のメソッド
     */
    public MethodInfo getMethodInfo() {
        return this.methodInfo;
    }

    /**
     * 引数で指定したプラグインによって登録されたメトリクス情報を取得するメソッド．
     * 
     * @param key ほしいメトリクスを登録したプラグイン
     * @return メトリクス値
     * @throws MetricNotRegisteredException メトリクスが登録されていないときにスローされる
     */
    public float getMetric(final AbstractPlugin key) throws MetricNotRegisteredException {

        if (null == key) {
            throw new NullPointerException();
        }

        Float value = this.methodMetrics.get(key);
        if (null == value) {
            throw new MetricNotRegisteredException();
        }

        return value.floatValue();
    }

    /**
     * 第一引数で与えられたプラグインで計測されたメトリクス値（第二引数）を登録する．
     * 
     * @param key 計測したプラグインインスタンス，Map のキーとして用いる．
     * @param value 登録するメトリクス値(int)
     * @throws MetricAlreadyRegisteredException 登録しようとしていたメトリクスが既に登録されていた場合にスローされる
     */
    public void putMetric(final AbstractPlugin key, final int value)
            throws MetricAlreadyRegisteredException {
        this.putMetric(key, new Float(value));
    }

    /**
     * 第一引数で与えられたプラグインで計測されたメトリクス値（第二引数）を登録する．
     * 
     * @param key 計測したプラグインインスタンス，Map のキーとして用いる．
     * @param value 登録するメトリクス値(float)
     * @throws MetricAlreadyRegisteredException 登録しようとしていたメトリクスが既に登録されていた場合にスローされる
     */
    public void putMetric(final AbstractPlugin key, final float value)
            throws MetricAlreadyRegisteredException {
        this.putMetric(key, new Float(value));
    }

    /**
     * このメトリクス情報に不足がないかをチェックする
     * 
     * @throws MetricNotRegisteredException
     */
    void checkMetrics() throws MetricNotRegisteredException {
        PluginManager pluginManager = PluginManager.getInstance();
        for (AbstractPlugin plugin : pluginManager.getPlugins()) {
            Float value = this.getMetric(plugin);
            if (null == value) {
                PluginInfo pluginInfo = plugin.getPluginInfo();
                String metricName = pluginInfo.getMetricsName();
                MethodInfo methodInfo = this.getMethodInfo();
                String methodName = methodInfo.getName();
                ClassInfo ownerClassInfo = methodInfo.getOwnerClass();
                String ownerClassName = ownerClassInfo.getName();
                throw new MetricNotRegisteredException("Metric \"" + metricName + "\" of "
                        + ownerClassName + "::" + methodName + " is not registered!");
            }
        }
    }

    /**
     * 第一引数で与えられたプラグインで計測されたメトリクス値（第二引数）を登録する．
     * 
     * @param key 計測したプラグインインスタンス，Map のキーとして用いる．
     * @param value 登録するメトリクス値
     * @throws MetricAlreadyRegisteredException 登録しようとしていたメトリクスが既に登録されていた場合にスローされる
     */
    private void putMetric(final AbstractPlugin key, final Float value)
            throws MetricAlreadyRegisteredException {

        if (null == key) {
            throw new NullPointerException();
        }
        if (this.methodMetrics.containsKey(key)) {
            throw new MetricAlreadyRegisteredException();
        }

        this.methodMetrics.put(key, value);
    }

    /**
     * このメトリクス情報のメソッドを保存するための変数
     */
    private final MethodInfo methodInfo;

    /**
     * メソッドメトリクスを保存するための変数
     */
    private final Map<AbstractPlugin, Float> methodMetrics;
}
