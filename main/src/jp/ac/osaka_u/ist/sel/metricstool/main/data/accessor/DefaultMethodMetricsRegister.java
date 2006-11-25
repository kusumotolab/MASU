package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MethodMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;


/**
 * プラグインがメソッドメトリクスを登録するために用いるクラス．
 * 
 * @author y-higo
 */
public class DefaultMethodMetricsRegister implements MethodMetricsRegister {

    /**
     * 登録処理用のオブジェクトの初期化を行う．プラグインは自身を引数として与えなければならない．
     * 
     * @param plugin 初期化を行うプラグインのインスタンス
     */
    public DefaultMethodMetricsRegister(final AbstractPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * 第一引数のメソッドのメトリクス値（第二引数）を登録する
     */
    public void registMetric(final MethodInfo methodInfo, final int value)
            throws MetricAlreadyRegisteredException {
        MethodMetricsInfoManager manager = MethodMetricsInfoManager.getInstance();
        manager.putMetric(methodInfo, this.plugin, value);
    }

    /**
     * プラグインオブジェクトを保存しておくための変数
     */
    private final AbstractPlugin plugin;
}
