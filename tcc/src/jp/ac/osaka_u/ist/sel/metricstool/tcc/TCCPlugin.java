package jp.ac.osaka_u.ist.sel.metricstool.tcc;


import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractClassMetricPlugin;


/**
 * TCC(Tight Class Cohesion)を計算するメトリクス
 * <p>
 * 全てのオブジェクト指向言語に対応
 * 
 * @author y-higo
 * 
 */
public class TCCPlugin extends AbstractClassMetricPlugin {

    /**
     * 引数で与えられたクラスのTCCを計算する
     * 
     * @param targetClass TCC計算対象クラス
     * @return 計算結果
     */
    @Override
    protected Number measureClassMetric(TargetClassInfo targetClass) {

        int couplings = 0;
        final SortedSet<TargetMethodInfo> methods = targetClass.getDefinedMethods();
        for (final TargetMethodInfo method1 : methods) {

            // method1 が参照している変数，代入している変数を取得
            final SortedSet<FieldInfo> method1Referencees = method1.getReferencees();
            final SortedSet<FieldInfo> method1Assignmentees = method1.getAssignmentees();

            METHOD2: for (final TargetMethodInfo method2 : methods.tailSet(method1)) {

                // metho1 と method2 が同じ場合はスキップ
                if (method1.equals(method2)) {
                    continue;
                }

                // method2 が参照している変数，代入している変数を取得
                final SortedSet<FieldInfo> method2Referencees = method2.getReferencees();
                final SortedSet<FieldInfo> method2Assignmentees = method2.getAssignmentees();

                // method1 が参照している変数を method2 が利用していたら．．．
                for (final FieldInfo field : method1Referencees) {
                    if (method2Referencees.contains(field) || method2Assignmentees.contains(field)) {
                        couplings++;
                        continue METHOD2;
                    }
                }

                // method1 が代入している変数を method2 が利用していたら．．．
                for (final FieldInfo field : method1Assignmentees) {
                    if (method2Referencees.contains(field) || method2Assignmentees.contains(field)) {
                        couplings++;
                        continue METHOD2;
                    }
                }
            }
        }

        final int combinations = methods.size() * (methods.size() - 1) / 2;
        return 1 < methods.size() ? new Float((float) couplings / (float) combinations)
                : new Float(0);
    }

    /**
     * このプラグインの簡易説明を１行で返す
     * 
     * @return 簡易説明文字列
     */
    @Override
    protected String getDescription() {
        return "Measuring the TCC metric.";
    }

    /**
     * このプラグインが計測するメトリクスの名前を返す
     * 
     * @return TCC
     */
    @Override
    protected String getMetricName() {
        return "TCC";
    }

    /**
     * このプラグインがフィールドに関する情報を利用するかどうかを返すメソッド． trueを返す．
     * 
     * @return true．
     */
    @Override
    protected boolean useFieldInfo() {
        return true;
    }

    /**
     * このプラグインがメソッドに関する情報を利用するかどうかを返すメソッド． trueを返す．
     * 
     * @return true．
     */
    @Override
    protected boolean useMethodInfo() {
        return true;
    }
}
