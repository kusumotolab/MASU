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
 * ファイルのLOC(Lines of Code)を計測するプラグインクラス.
 * <p>
 * 全てのオブジェクト指向言語に対応.
 * ファイルに関する情報を必要とする.
 * @author k-choy
 *
 */

public class FileLocPlugin extends AbstractPlugin {

	/**
	 * メトリクス値を計測する
	 * @throws MetricAlreadyRegisteredException 
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
     * このプラグインの簡易説明を１行で返す
     * @return 簡易説明文字列
     */
	@Override
	protected String getDescription() {
		return "measuring LOC metrics of file.";
	}

	/**
     * 詳細説明文字列定数
     */
    private final static String DETAIL_DESCRIPTION;

    /**
     * このプラグインの詳細説明を返す
     * @return　詳細説明文字列
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
     * メトリクス名を返す
     * @return メトリクス名
     */
	@Override
	protected String getMetricName() {
		return "floc";
	}

	/**
	 * 計測するメトリクスタイプを返す
	 * @return メトリクスタイプ
	 */
	@Override
	protected METRIC_TYPE getMetricType() {
		return jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE.FILE_METRIC;
	}

	/**
	 * ファイルに関する情報が必要かどうか
	 * @return ファイルに関する情報が必要かを表す真偽値
	 */
	@Override
	protected boolean useFileInfo() {
		return true;
	}

}
