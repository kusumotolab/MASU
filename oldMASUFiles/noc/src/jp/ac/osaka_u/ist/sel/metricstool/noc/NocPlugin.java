package jp.ac.osaka_u.ist.sel.metricstool.noc;


import java.io.PrintWriter;
import java.io.StringWriter;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractClassMetricPlugin;


/**
 * 
 * <p>CK���g���N�X��NOC(Number Of Childlen)���v������v���O�C���N���X�D</p>
 * <p>{@link TargetClassInfo#getSubClasses()} �� size</p>
 * 
 * @author wtnb-y
 */
public class NocPlugin extends AbstractClassMetricPlugin {
    /**
     * ���g���N�X����Ԃ�
     * @return ���g���N�X��
     */
    @Override
    protected String getMetricName() {
        return "NOC";
    }

    /**
     * ���̃v���O�C���̊ȈՐ������P�s�ŕԂ�
     * @return �ȈՐ���������
     */
    @Override
    protected String getDescription() {
        return "measuring NOC metric.";
    }

    /**
     * ���g���N�X�̌v�����s��
     * @param TargetClass �v���ΏۃN���X
     */
    @Override
    protected Number measureClassMetric(TargetClassInfo targetClass) {
        return targetClass.getSubClasses().size();
    }

    /**
     * �ڍא���������萔
     */
    private final static String DETAIL_DESCRIPTION;

    /**
     * ���̃v���O�C���̏ڍא�����Ԃ�
     * @return�@�ڍא���������
     */
    @Override
    protected String getDetailDescription() {
        return DETAIL_DESCRIPTION;
    }

    static {
        {
            StringWriter buffer = new StringWriter();
            PrintWriter writer = new PrintWriter(buffer);

            writer.println("This plugin measures the NOC metric.");
            writer.println();
            writer.println("NOC = number of children(subclasses) in a class");
            writer.println();
            writer.flush();

            DETAIL_DESCRIPTION = buffer.toString();
        }
    }
}
