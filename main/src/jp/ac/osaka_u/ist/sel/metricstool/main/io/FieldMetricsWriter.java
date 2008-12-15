package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FieldMetricsInfoManager;


/**
 * フィールドメトリクスを書き出すクラスが実装しなければならないインターフェース
 * 
 * @author higo
 *
 */
public interface FieldMetricsWriter extends MetricsWriter {

    /**
     * フィールド名のタイトル文字列
     */
    String FIELD_NAME = new String("\"Field Name\"");

    /**
     * フィールドメトリクスを保存している定数
     */
    FieldMetricsInfoManager FIELD_METRICS_MANAGER = DataManager.getInstance()
            .getFieldMetricsInfoManager();

}
