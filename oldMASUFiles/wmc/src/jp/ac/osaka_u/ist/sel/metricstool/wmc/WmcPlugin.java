package jp.ac.osaka_u.ist.sel.metricstool.wmc;


import java.io.PrintWriter;
import java.io.StringWriter;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractClassMetricPlugin;


/**
 * WMC���v������v���O�C���N���X.
 * 
 * @author t-miyake
 */
public class WmcPlugin extends AbstractClassMetricPlugin {
    /**
     * �ڍא���������萔
     */
    private final static String DETAIL_DESCRIPTION;

    /**
     * ���g���N�X�̌v��.
     * 
     * @param targetClass �Ώۂ̃N���X
     */
    @Override
    protected Number measureClassMetric(final TargetClassInfo targetClass) {
        // ���̐��� WMC
        int wmc = 0;

        // localMethods �ŌĂ΂�Ă��郁�\�b�h
        for (final MethodInfo m : targetClass.getDefinedMethods()) {
            wmc += this.measureMethodWeight(m).intValue();
        }

        return new Integer(wmc);
    }

    /**
     * �����ŗ^����ꂽ���\�b�h�̏d�݂�Ԃ�
     * 
     * @param method �d�݂��v�����������\�b�h
     * @return ���\�b�h�̏d��
     */
    protected Number measureMethodWeight(final MethodInfo method) {

        // ���\�b�h�̏d�݂ɂ̓T�C�N���}�`�b�N����p����
        int weight = this.measureCyclomatic(method);
        return weight;
    }

    /**
     * �����ŗ^����ꂽ��Ԃ̃T�C�N���}�`�b�N����Ԃ�
     * 
     * @param block �T�C�N���}�`�b�N�����v�����������
     * @return �T�C�N���}�`�b�N��
     */
    private int measureCyclomatic(final LocalSpaceInfo block) {
        int cyclomatic = 1;
        for (final StatementInfo statement : block.getStatements()) {
            if (statement instanceof BlockInfo) {
                cyclomatic += this.measureCyclomatic((BlockInfo) statement);
                if (!(statement instanceof ConditionalBlockInfo)) {
                    cyclomatic--;
                }
            }
        }
        return cyclomatic;
    }

    /**
     * ���̃v���O�C���̊ȈՐ�����1�s�ŕԂ�
     * @return �ȈՐ���������
     */
    @Override
    protected String getDescription() {
        return "Measuring the WMC metric.";
    }

    /**
     * ���̃v���O�C���̏ڍא�����Ԃ�
     * @return�@�ڍא���������
     */
    @Override
    protected String getDetailDescription() {
        return DETAIL_DESCRIPTION;
    }

    /**
     * ���g���N�X����Ԃ��D
     * 
     * @return ���g���N�X��
     */
    @Override
    protected String getMetricName() {
        return "WMC";
    }

    /**
     * ���̃v���O�C�������\�b�h�Ɋւ�����𗘗p���邩�ǂ�����Ԃ����\�b�h�D
     * true��Ԃ��D
     * 
     * @return true�D
     */
    @Override
    protected boolean useMethodInfo() {
        return true;
    }

    /**
     * ���̃v���O�C�������\�b�h�����Ɋւ�����𗘗p���邩�ǂ�����Ԃ����\�b�h.
     * true��Ԃ��D
     * 
     * @return true�D
     */
    @Override
    protected boolean useMethodLocalInfo() {
        return true;
    }

    static {
        // DETAIL_DESCRIPTION ����
        {
            StringWriter buffer = new StringWriter();
            PrintWriter writer = new PrintWriter(buffer);

            writer.println("This plugin measures the WFC (Response for a Class) metric.");
            writer.flush();

            DETAIL_DESCRIPTION = buffer.toString();
        }
    }

}
