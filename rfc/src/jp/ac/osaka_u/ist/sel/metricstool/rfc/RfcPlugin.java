package jp.ac.osaka_u.ist.sel.metricstool.rfc;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractClassMetricPlugin;


/**
 * RFCを計測するプラグインクラス.
 * 
 * @author rniitani
 */
public class RfcPlugin extends AbstractClassMetricPlugin {
    /**
     * 詳細説明文字列定数
     */
    private final static String DETAIL_DESCRIPTION;

    /**
     * メトリクスの計測.
     * 
     * @param targetClass 対象のクラス
     */
    @Override
    protected Number measureClassMetric(TargetClassInfo targetClass) {
        // この数が RFC
        final Set<CallableUnitInfo> rfcMethods = new HashSet<CallableUnitInfo>();

        // 現在のクラスで定義されているメソッド
        final Set<TargetMethodInfo> localMethods = targetClass.getDefinedMethods();
        rfcMethods.addAll(localMethods);

        // localMethods で呼ばれているメソッド
        for (final TargetMethodInfo m : localMethods) {
        	
            rfcMethods.addAll(MethodCallInfo.getCallees(m.getCalls()));
        }

        return new Integer(rfcMethods.size());
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
     * メトリクス名を返す．
     * 
     * @return メトリクス名
     */
    @Override
    protected String getMetricName() {
        return "RFC";
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
