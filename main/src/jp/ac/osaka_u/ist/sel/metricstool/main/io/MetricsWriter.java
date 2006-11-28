package jp.ac.osaka_u.ist.sel.metricstool.main.io;


import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.PluginManager;


public interface MetricsWriter {

    /**
     * メトリクスをファイルに書き出す
     */
    public abstract void write();

    /**
     * プラグインを管理しているオブジェクトを指す定数
     */
    PluginManager PLUGIN_MANAGER = PluginManager.getInstance();

    /**
     * メトリクス値がないことを表す文字
     */
    char NO_METRIC = '-';

}