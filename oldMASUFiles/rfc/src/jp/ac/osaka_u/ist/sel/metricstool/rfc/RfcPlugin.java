package jp.ac.osaka_u.ist.sel.metricstool.rfc;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractClassMetricPlugin;


/**
 * RFC���v������v���O�C���N���X.
 * 
 * @author rniitani
 */
public class RfcPlugin extends AbstractClassMetricPlugin {
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
    protected Number measureClassMetric(TargetClassInfo targetClass) {
        // ���̐��� RFC
        final Set<CallableUnitInfo> rfcMethods = new HashSet<CallableUnitInfo>();

        // ���݂̃N���X�Œ�`����Ă��郁�\�b�h
        final Set<MethodInfo> localMethods = targetClass.getDefinedMethods();
        rfcMethods.addAll(localMethods);

        // localMethods �ŌĂ΂�Ă��郁�\�b�h
        for (final MethodInfo m : localMethods) {

            rfcMethods.addAll(MethodCallInfo.getCallees(m.getCalls()));
        }

        return new Integer(rfcMethods.size());
    }

    /**
     * ���̃v���O�C���̊ȈՐ�����1�s�ŕԂ�
     * @return �ȈՐ���������
     */
    @Override
    protected String getDescription() {
        return "Measuring the RFC metric.";
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
        return "RFC";
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

            writer.println("This plugin measures the RFC (Response for a Class) metric.");
            writer.println();
            writer.println("RFC = number of local methods in a class");
            writer.println("    + number of remote methods called by local methods");
            writer.println();
            writer.println("A given remote method is counted by once.");
            writer.println();
            writer.flush();

            DETAIL_DESCRIPTION = buffer.toString();
        }
    }

}
