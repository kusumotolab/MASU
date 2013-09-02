package jp.ac.osaka_u.ist.sel.metricstool.nom;


import java.io.PrintWriter;
import java.io.StringWriter;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractClassMetricPlugin;


/**
 * �N���X�̑����̐��𐔂���v���O�C���N���X.
 * 
 * {@link TargetClassInfo#getDefinedFields()} �� size
 * 
 * @author rniitani
 */
public class NomPlugin extends AbstractClassMetricPlugin {
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
        return new Integer(targetClass.getDefinedMethods().size());
    }

    /**
     * ���̃v���O�C���̊ȈՐ�����1�s�ŕԂ�
     * @return �ȈՐ���������
     */
    @Override
    protected String getDescription() {
        return "Counting number of methods.";
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
        return "NOM";
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

    static {
        // DETAIL_DESCRIPTION ����
        {
            StringWriter buffer = new StringWriter();
            PrintWriter writer = new PrintWriter(buffer);

            writer.println("This plugin measures the NOM (number of methods) metric.");
            writer.println();
            writer.println("NOM = number of methods in a class");
            writer.println();
            writer.flush();

            DETAIL_DESCRIPTION = buffer.toString();
        }
    }

}
