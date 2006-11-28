package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MethodMetricsInfoManager;


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
