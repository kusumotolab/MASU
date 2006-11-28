package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.FileMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;


/**
 * �v���O�C�����t�@�C�����g���N�X��o�^���邽�߂ɗp����N���X�D
 * 
 * @author y-higo
 */
public class DefaultFileMetricsRegister implements FileMetricsRegister {

    /**
     * �o�^�����p�̃I�u�W�F�N�g�̏��������s���D�v���O�C���͎��g�������Ƃ��ė^���Ȃ���΂Ȃ�Ȃ��D
     * 
     * @param plugin ���������s���v���O�C���̃C���X�^���X
     */
    public DefaultFileMetricsRegister(final AbstractPlugin plugin) {

        if (null == plugin) {
            throw new NullPointerException();
        }
        PluginInfo pluginInfo = plugin.getPluginInfo();
        if (METRIC_TYPE.FILE_METRIC != pluginInfo.getMetricType()) {
            throw new IllegalArgumentException("plugin must be file type!");
        }

        this.plugin = plugin;
    }

    /**
     * �������̃t�@�C���̃��g���N�X�l�i�������j��o�^����
     */
    public void registMetric(final FileInfo fileInfo, final int value)
            throws MetricAlreadyRegisteredException {
        FileMetricsInfoManager manager = FileMetricsInfoManager.getInstance();
        manager.putMetric(fileInfo, this.plugin, value);
    }

    /**
     * �v���O�C���I�u�W�F�N�g��ۑ����Ă������߂̕ϐ�
     */
    private final AbstractPlugin plugin;
}