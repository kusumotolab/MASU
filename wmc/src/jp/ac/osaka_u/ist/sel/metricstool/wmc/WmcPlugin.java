package jp.ac.osaka_u.ist.sel.metricstool.wmc;


import java.io.PrintWriter;
import java.io.StringWriter;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractClassMetricPlugin;


/**
 * WMCを計測するプラグインクラス.
 * 
 * @author t-miyake
 */
public class WmcPlugin extends AbstractClassMetricPlugin {
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
    protected Number measureClassMetric(final TargetClassInfo targetClass) {
        // この数が WMC
        int wmc = 0;

        // localMethods で呼ばれているメソッド
        for (final TargetMethodInfo m : targetClass.getDefinedMethods()) {
            wmc += this.measureMethodWeight(m).intValue();
        }

        return new Integer(wmc);
    }
    
    /**
     * 引数で与えられたメソッドの重みを返す
     * 
     * @param method 重みを計測したいメソッド
     * @return メソッドの重み
     */
    protected Number measureMethodWeight(final TargetMethodInfo method) {
        
        // メソッドの重みにはサイクロマチック数を用いる
        int weight = this.measureCyclomatic(method);
        return weight;
    }
    
    /**
     * 引数で与えられた空間のサイクロマチック数を返す
     * 
     * @param block サイクロマチック数を計測したい空間
     * @return サイクロマチック数
     */
    private int measureCyclomatic(final LocalSpaceInfo block) {
        int cyclomatic = 1;
        for(final StatementInfo statement: block.getStatements()) {
            if(statement instanceof BlockInfo) {
                cyclomatic += this.measureCyclomatic((BlockInfo) statement);
                if(!(statement instanceof ConditionalBlockInfo)) {
                    cyclomatic--;
                }
            }
        }
        return cyclomatic;
    }
    

    /**
     * このプラグインの簡易説明を1行で返す
     * @return 簡易説明文字列
     */
    @Override
    protected String getDescription() {
        return "Measuring the WMC metric.";
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
        return "WMC";
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

            writer.println("This plugin measures the WFC (Response for a Class) metric.");
            writer.flush();

            DETAIL_DESCRIPTION = buffer.toString();
        }
    }

}
