package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.ClassMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRICS_TYPE;


/**
 * プラグインがクラスメトリクスを登録するために用いるクラス．
 * 
 * @author y-higo
 */
public class DefaultClassMetricsRegister implements ClassMetricsRegister {

    /**
     * 登録処理用のオブジェクトの初期化を行う．プラグインは自身を引数として与えなければならない．
     * 
     * @param plugin 初期化を行うプラグインのインスタンス
     */
    public DefaultClassMetricsRegister(final AbstractPlugin plugin) {

        if (null == plugin) {
            throw new NullPointerException();
        }
        PluginInfo pluginInfo = plugin.getPluginInfo();
        if (METRICS_TYPE.CLASS_METRICS != pluginInfo.getMetricsType()) {
            throw new IllegalArgumentException("plugin must be class type!");
        }

        this.plugin = plugin;
    }

    /**
     * 第一引数のクラスのメトリクス値（第二引数）を登録する
     */
    public void registMetric(final ClassInfo classInfo, final int value)
            throws MetricAlreadyRegisteredException {
        ClassMetricsInfoManager manager = ClassMetricsInfoManager.getInstance();
        manager.putMetric(classInfo, this.plugin, value);
    }

    /**
     * プラグインオブジェクトを保存しておくための変数
     */
    private final AbstractPlugin plugin;
}
