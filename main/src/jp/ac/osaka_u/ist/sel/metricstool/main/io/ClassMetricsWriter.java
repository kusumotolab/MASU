package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.ClassMetricsInfoManager;


/**
 * クラスメトリクスをファイルに書き出すクラスが実装しなければならないインターフェース
 * 
 * @author y-higo
 */
public interface ClassMetricsWriter extends MetricsWriter {

    /**
     * クラス名のタイトル文字列
     */
    String CLASS_NAME = new String("\"Class Name\"");

    /**
     * クラスメトリクスを保存している定数
     */
    ClassMetricsInfoManager CLASS_METRICS_MANAGER = ClassMetricsInfoManager.getInstance();

}
