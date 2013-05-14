package jp.ac.osaka_u.ist.sel.metricstool.mloc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;

/**
 * 
 * ���\�b�h��LOC(Lines of Code)���v������v���O�C���N���X.
 * <p>
 * �S�ẴI�u�W�F�N�g�w������ɑΉ�.
 * ���\�b�h�Ɋւ������K�v�Ƃ���.
 * @author k-choy
 *
 */
public class MethodLocPlugin extends AbstractPlugin {
	
	/**
	 * ���g���N�X�l���v������
	 * 
	 */
	@Override
	protected void execute() {
		Iterator<TargetMethodInfo> mit = this.getMethodInfoAccessor().iterator();
		
		while (mit.hasNext()) {
			TargetMethodInfo mi = mit.next();
			try {
				int mloc = mi.getToLine() - mi.getFromLine() + 1;
				this.registMetric(mi, mloc);
			} catch (MetricAlreadyRegisteredException e) {
				e.printStackTrace();
			}
		}
	}

	/**
     * ���̃v���O�C���̊ȈՐ������P�s�ŕԂ�
     * @return �ȈՐ���������
     */
	@Override
	protected String getDescription() {
		return "measuring LOC metrics of method.";
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

            writer.println("This plugin measures the LOC metric of method.");
            writer.println();
            writer.println("LOC = Lines Of Code");
            writer.println();
            writer.flush();

            DETAIL_DESCRIPTION = buffer.toString();
        }
    }

    /**
     * ���g���N�X����Ԃ�
     * @return ���g���N�X��
     */
	@Override
	protected String getMetricName() {
		return "MLOC";
	}

	/**
	 * �v�����郁�g���N�X�^�C�v��Ԃ�
	 * @return ���g���N�X�^�C�v
	 */
	@Override
	protected METRIC_TYPE getMetricType() {
		return jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE.METHOD_METRIC;
	}

	/**
	 * ���\�b�h�Ɋւ����񂪕K�v���ǂ���
	 * @return ���\�b�h�Ɋւ����񂪕K�v����\���^�U�l
	 */
	@Override
	protected boolean useMethodInfo() {
		return true;
	}

}
