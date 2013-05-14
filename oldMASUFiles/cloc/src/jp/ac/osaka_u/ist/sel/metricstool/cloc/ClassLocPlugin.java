package jp.ac.osaka_u.ist.sel.metricstool.cloc;

import java.io.PrintWriter;
import java.io.StringWriter;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractClassMetricPlugin;

/**
 * 
 * �N���X��LOC(Lines Of Code)���v������v���O�C���N���X.
 * <p>
 * �S�ẴI�u�W�F�N�g�w������ɑΉ�.
 * �N���X�����̏���K�v�Ƃ���.
 * @author k-choy
 *
 */
public class ClassLocPlugin extends AbstractClassMetricPlugin {

	/**
     * ���g���N�X�̌v�����s��
     * @param TargetClass �v���ΏۃN���X
     */
	@Override
	protected Number measureClassMetric(TargetClassInfo targetClass) {
		int cloc = targetClass.getToLine() - targetClass.getFromLine() + 1;
		return cloc;
	}

	/**
     * ���̃v���O�C���̊ȈՐ������P�s�ŕԂ�
     * @return �ȈՐ���������
     */
	@Override
	protected String getDescription() {
		return "measuring LOC metric of class.";
	}

	/**
     * ���g���N�X����Ԃ�
     * @return ���g���N�X��
     */
	@Override
	protected String getMetricName() {
		return "CLOC";
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

            writer.println("This plugin measures the LOC metric of a class.");
            writer.println();
            writer.println("LOC = Lines of Code in a class");
            writer.println();
            writer.flush();

            DETAIL_DESCRIPTION = buffer.toString();
        }
    }
}
