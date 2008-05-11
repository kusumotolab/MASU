package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MethodMetricsInfoManager;


/**
 * メソッドのメトリクスを書き出すクラスが実装しなければならないインターフェース
 * 
 * @author higo
 *
 */
public interface MethodMetricsWriter extends MetricsWriter {

    /**
     * メソッド名のタイトル文字列
     */
    String METHOD_NAME = new String("\"Method Name\"");

    /**
     * メソッドメトリクスを保存している定数
     */
    MethodMetricsInfoManager METHOD_METRICS_MANAGER = MethodMetricsInfoManager.getInstance();

}
