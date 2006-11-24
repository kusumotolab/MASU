package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import java.util.HashMap;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;


/**
 * ファイルメトリクスを登録するためのデータクラス
 * 
 * @author y-higo
 *
 */
public final class FileMetricsInfo {

    /**
     * 引数なしコンストラクタ．
     */
    public FileMetricsInfo() {
        this.fileMetrics = new HashMap<AbstractPlugin, Float>();
    }

    /**
     * 第一引数で与えられたプラグインで計測されたメトリクス値（第二引数）を登録する．
     * 
     * @param key 計測したプラグインインスタンス，Map のキーとして用いる．
     * @param value 登録するメトリクス値(int)
     * @throws MetricAlreadyRegisteredException 登録しようとしていたメトリクスが既に登録されていた場合にスローされる
     */
    public void putMetric(final AbstractPlugin key, final int value) throws MetricAlreadyRegisteredException {
        this.putMetric(key, new Float(value));
    }
    
    /**
     * 第一引数で与えられたプラグインで計測されたメトリクス値（第二引数）を登録する．
     * 
     * @param key 計測したプラグインインスタンス，Map のキーとして用いる．
     * @param value 登録するメトリクス値(float)
     * @throws MetricAlreadyRegisteredException 登録しようとしていたメトリクスが既に登録されていた場合にスローされる
     */
    public void putMetric(final AbstractPlugin key, final float value) throws MetricAlreadyRegisteredException {
        this.putMetric(key, new Float(value));
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
        if (this.fileMetrics.containsKey(key)) {
            throw new MetricAlreadyRegisteredException();
        }

        this.fileMetrics.put(key, value);
    }

    /**
     * ファイルメトリクスを保存するための変数
     */
    private final Map<AbstractPlugin, Float> fileMetrics;
}
