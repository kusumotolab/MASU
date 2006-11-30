package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import java.util.Collections;
import java.util.SortedMap;
import java.util.TreeMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.MetricTypeAndNamePluginComparator;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.PluginManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;


/**
 * クラスメトリクスを登録するためのデータクラス
 * 
 * @author y-higo
 * 
 */
public final class ClassMetricsInfo implements MessageSource {

    /**
     * 引数なしコンストラクタ．
     */
    public ClassMetricsInfo(final ClassInfo classInfo) {

        if (null == classInfo) {
            throw new NullPointerException();
        }

        this.classInfo = classInfo;
        this.classMetrics = Collections.synchronizedSortedMap(new TreeMap<AbstractPlugin, Float>(
                new MetricTypeAndNamePluginComparator()));
    }

    /**
     * このメトリクス情報のクラスを返す
     * 
     * @return このメトリクス情報のクラス
     */
    public ClassInfo getClassInfo() {
        return this.classInfo;
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

        Float value = this.classMetrics.get(key);
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
        this.putMetric(key, Float.valueOf(value));
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
        this.putMetric(key, Float.valueOf(value));
    }

    /**
     * メッセージの送信者名を返す
     * 
     * @return メッセージの送信者名
     */
    public String getMessageSourceName() {
        return this.getClass().getName();
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
                String metricName = pluginInfo.getMetricName();
                ClassInfo classInfo = this.getClassInfo();
                String className = classInfo.getName();
                String message = "Metric \"" + metricName + "\" of " + className
                        + " is not registered!";
                MessagePrinter printer = new DefaultMessagePrinter(this,
                        MessagePrinter.MESSAGE_TYPE.ERROR);
                printer.println(message);
                throw new MetricNotRegisteredException(message);
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

        if ((null == key) || (null == value)) {
            throw new NullPointerException();
        }
        if (this.classMetrics.containsKey(key)) {
            throw new MetricAlreadyRegisteredException();
        }

        this.classMetrics.put(key, value);
    }

    /**
     * メトリクス情報のクラスを保存するための変数
     */
    private final ClassInfo classInfo;

    /**
     * クラスメトリクスを保存するための変数
     */
    private final SortedMap<AbstractPlugin, Float> classMetrics;
}
