package jp.ac.osaka_u.ist.sel.metricstool.rfc;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.ClassInfoAccessor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LanguageUtil;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;


/**
 * RFCを計測するプラグインクラス.
 * 
 * @author rniitani
 */
public class RfcPlugin extends AbstractPlugin {
    /**
     * 詳細説明文字列定数
     */
    private final static String DETAIL_DESCRIPTION;

    /**
     * メトリクス計測を開始する．
     */
    @Override
    protected void execute() {
        // クラス情報アクセサを取得
        final ClassInfoAccessor classAccessor = this.getClassInfoAccessor();

        // 進捗報告用
        int measuredClassCount = 0;
        final int maxClassCount = classAccessor.getClassCount();

        //全クラスについて
        for (final TargetClassInfo targetClass : classAccessor) {
            // この数が RFC
            final Set<MethodInfo> rfcMethods = new HashSet<MethodInfo>();

            // 現在のクラスで定義されているメソッド
            final Set<TargetMethodInfo> localMethods = targetClass.getDefinedMethods();
            rfcMethods.addAll(localMethods);

            // localMethods で呼ばれているメソッド
            for (final TargetMethodInfo m : localMethods) {
                rfcMethods.addAll(m.getCallees());
            }

            try {
                this.registMetric(targetClass, rfcMethods.size());
            } catch (final MetricAlreadyRegisteredException e) {
                this.err.println(e);
            }

            //1クラスごとに%で進捗報告
            this.reportProgress(++measuredClassCount * 100 / maxClassCount);
        }
    }

    /**
     * このプラグインの簡易説明を1行で返す
     * @return 簡易説明文字列
     */
    @Override
    protected String getDescription() {
        return "Measuring the RFC metric.";
    }

    /**
     * このプラグインの詳細説明を返す
     * @return　詳細説明文字列
     */
    @Override
    protected String getDetailDescription() {
        return DETAIL_DESCRIPTION;
    }

    /**
     * このプラグインがメトリクスを計測できる言語を返す．
     * 
     * 計測対象の全言語の中でオブジェクト指向言語であるものの配列を返す．
     * 
     * @return オブジェクト指向言語の配列
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE
     */
    @Override
    protected LANGUAGE[] getMeasurableLanguages() {
        return LanguageUtil.getObjectOrientedLanguages();
    }

    /**
     * メトリクス名を返す．
     * 
     * @return メトリクス名
     */
    @Override
    protected String getMetricName() {
        return "RFC";
    }

    /**
     * このプラグインが計測するメトリクスのタイプを返す．
     * 
     * @return メトリクスタイプ
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE
     */
    @Override
    protected METRIC_TYPE getMetricType() {
        return METRIC_TYPE.CLASS_METRIC;
    }

    /**
     * このプラグインがクラスに関する情報を利用するかどうかを返すメソッド．
     * trueを返す．
     * 
     * @return true．
     */
    @Override
    protected boolean useClassInfo() {
        return true;
    }

    /**
     * このプラグインがメソッドに関する情報を利用するかどうかを返すメソッド．
     * trueを返す．
     * 
     * @return true．
     */
    @Override
    protected boolean useMethodInfo() {
        return true;
    }

    /**
     * このプラグインがメソッド内部に関する情報を利用するかどうかを返すメソッド.
     * trueを返す．
     * 
     * @return true．
     */
    @Override
    protected boolean useMethodLocalInfo() {
        return true;
    }

    static {
        // DETAIL_DESCRIPTION 生成
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
