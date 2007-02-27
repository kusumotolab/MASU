package jp.ac.osaka_u.ist.sel.metricstool.noa;


import java.io.PrintWriter;
import java.io.StringWriter;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractClassMetricPlugin;


/**
 * クラスの属性の数を数えるプラグインクラス.
 * 
 * {@link TargetClassInfo#getDefinedFields()} の size
 * 
 * @author rniitani
 */
public class NoaPlugin extends AbstractClassMetricPlugin {
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
        return new Integer(targetClass.getDefinedFields().size());
    }

    /**
     * このプラグインの簡易説明を1行で返す
     * @return 簡易説明文字列
     */
    @Override
    protected String getDescription() {
        return "Counting number of attributes.";
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
        return "NOA";
    }

    /**
     * このプラグインがフィールドに関する情報を利用するかどうかを返すメソッド．
     * trueを返す．
     * 
     * @return true．
     */
    @Override
    protected boolean useFieldInfo() {
        return true;
    }

    static {
        // DETAIL_DESCRIPTION 生成
        {
            StringWriter buffer = new StringWriter();
            PrintWriter writer = new PrintWriter(buffer);

            writer.println("This plugin measures the NOA (number of attributes) metric.");
            writer.println();
            writer.println("NOA = number of attributes in a class");
            writer.println();
            writer.flush();

            DETAIL_DESCRIPTION = buffer.toString();
        }
    }

}
