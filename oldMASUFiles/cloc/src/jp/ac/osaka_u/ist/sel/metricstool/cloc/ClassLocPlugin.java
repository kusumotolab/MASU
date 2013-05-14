package jp.ac.osaka_u.ist.sel.metricstool.cloc;

import java.io.PrintWriter;
import java.io.StringWriter;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractClassMetricPlugin;

/**
 * 
 * クラスのLOC(Lines Of Code)を計測するプラグインクラス.
 * <p>
 * 全てのオブジェクト指向言語に対応.
 * クラス内部の情報を必要とする.
 * @author k-choy
 *
 */
public class ClassLocPlugin extends AbstractClassMetricPlugin {

	/**
     * メトリクスの計測を行う
     * @param TargetClass 計測対象クラス
     */
	@Override
	protected Number measureClassMetric(TargetClassInfo targetClass) {
		int cloc = targetClass.getToLine() - targetClass.getFromLine() + 1;
		return cloc;
	}

	/**
     * このプラグインの簡易説明を１行で返す
     * @return 簡易説明文字列
     */
	@Override
	protected String getDescription() {
		return "measuring LOC metric of class.";
	}

	/**
     * メトリクス名を返す
     * @return メトリクス名
     */
	@Override
	protected String getMetricName() {
		return "CLOC";
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

            writer.println("This plugin measures the LOC metric of a class.");
            writer.println();
            writer.println("LOC = Lines of Code in a class");
            writer.println();
            writer.flush();

            DETAIL_DESCRIPTION = buffer.toString();
        }
    }
}
