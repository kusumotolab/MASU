package jp.ac.osaka_u.ist.sel.metricstool.noc;


import java.io.PrintWriter;
import java.io.StringWriter;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractClassMetricPlugin;


/**
 * 
 * <p>CKメトリクスのNOC(Number Of Childlen)を計測するプラグインクラス．</p>
 * <p>{@link TargetClassInfo#getSubClasses()} の size</p>
 * 
 * @author wtnb-y
 */
public class NocPlugin extends AbstractClassMetricPlugin {
    /**
     * メトリクス名を返す
     * @return メトリクス名
     */
    @Override
    protected String getMetricName() {
        return "NOC";
    }

    /**
     * このプラグインの簡易説明を１行で返す
     * @return 簡易説明文字列
     */
    @Override
    protected String getDescription() {
        return "measuring NOC metric.";
    }

    /**
     * メトリクスの計測を行う
     * @param TargetClass 計測対象クラス
     */
    @Override
    protected Number measureClassMetric(TargetClassInfo targetClass) {
        return targetClass.getSubClasses().size();
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

            writer.println("This plugin measures the NOC metric.");
            writer.println();
            writer.println("NOC = number of children(subclasses) in a class");
            writer.println();
            writer.flush();

            DETAIL_DESCRIPTION = buffer.toString();
        }
    }
}
