package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FileMetricsInfoManager;


/**
 * ファイルメトリクスを書き出すクラスが実装しなければならないインターフェース
 * 
 * @author higo
 *
 */
public interface FileMetricsWriter extends MetricsWriter {

    /**
     * ファイル名のタイトル文字列
     */
    String FILE_NAME = new String("\"File Name\"");

    /**
     * ファイルメトリクスを保存している定数
     */
    FileMetricsInfoManager FILE_METRICS_MANAGER = DataManager.getInstance()
            .getFileMetricsInfoManager();

}
