package jp.ac.osaka_u.ist.sel.metricstool.floc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FileInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;

/**
 * 
 * �t�@�C����LOC(Lines of Code)���v������v���O�C���N���X.
 * <p>
 * �S�ẴI�u�W�F�N�g�w������ɑΉ�.
 * �t�@�C���Ɋւ������K�v�Ƃ���.
 * @author k-choy
 *
 */

public class FileLocPlugin extends AbstractPlugin {

	/**
	 * ���g���N�X�l���v������
	 * 
	 */
	@Override
	protected void execute(){
		Iterator<FileInfo> fit = this.getFileInfoAccessor().iterator();
		
		while (fit.hasNext()) {
			FileInfo fi = fit.next();
			try {
				this.registMetric(fi, fi.getLOC());
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
		return "measuring LOC metrics of file.";
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

            writer.println("This plugin measures the LOC metric of file.");
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
		return "FLOC";
	}

	/**
	 * �v�����郁�g���N�X�^�C�v��Ԃ�
	 * @return ���g���N�X�^�C�v
	 */
	@Override
	protected METRIC_TYPE getMetricType() {
		return jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE.FILE_METRIC;
	}

	/**
	 * �t�@�C���Ɋւ����񂪕K�v���ǂ���
	 * @return �t�@�C���Ɋւ����񂪕K�v����\���^�U�l
	 */
	@Override
	protected boolean useFileInfo() {
		return true;
	}

}
