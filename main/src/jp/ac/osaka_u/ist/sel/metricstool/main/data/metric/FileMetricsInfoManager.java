package jp.ac.osaka_u.ist.sel.metricstool.main.data.metric;


import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;


public final class FileMetricsInfoManager {

    /**
     * このクラスのインスタンスを返す．シングルトンパターンを用いている．
     * 
     * @return このクラスのインスタンス
     */
    public static FileMetricsInfoManager getInstance() {
        return FILE_METRICS_MAP;
    }

    /**
     * メトリクスを登録する
     * 
     * @param fileInfo メトリクス計測対象のファイルオブジェクト
     * @param plugin メトリクスのプラグイン
     * @param value メトリクス値
     * @throws MetricAlreadyRegisteredException 登録しようとしているメトリクスが既に登録されている
     */
    public void putMetric(final FileInfo fileInfo, final AbstractPlugin plugin, final int value)
            throws MetricAlreadyRegisteredException {

        FileMetricsInfo fileMetricsInfo = this.fileMetricsInfos.get(fileInfo);

        // 対象ファイルの fileMetricsInfo が無い場合は，new して Map に登録する
        if (null == fileMetricsInfo) {
            fileMetricsInfo = new FileMetricsInfo();
            this.fileMetricsInfos.put(fileInfo, fileMetricsInfo);
        }

        fileMetricsInfo.putMetric(plugin, value);
    }

    /**
     * ファイルメトリクスマネージャのオブジェクトを生成する． シングルトンパターンを用いているため，private がついている．
     * 
     */
    private FileMetricsInfoManager() {
        this.fileMetricsInfos = Collections
                .synchronizedMap(new TreeMap<FileInfo, FileMetricsInfo>());
    }

    /**
     * このクラスのオブジェクトを管理している定数．シングルトンパターンを用いている．
     */
    private static final FileMetricsInfoManager FILE_METRICS_MAP = new FileMetricsInfoManager();

    /**
     * ファイルメトリクスのマップを保存するための変数
     */
    private final Map<FileInfo, FileMetricsInfo> fileMetricsInfos;
}
