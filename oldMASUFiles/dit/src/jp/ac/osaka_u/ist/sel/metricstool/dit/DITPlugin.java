package jp.ac.osaka_u.ist.sel.metricstool.dit;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractClassMetricPlugin;


/**
 * DIT(CKメトリクスの1つ)を計算するメトリクス
 * <p>
 * 全てのオブジェクト指向言語に対応
 * 
 * @author y-higo
 * 
 */
public class DITPlugin extends AbstractClassMetricPlugin {

    /**
     * 引数で与えられたクラスのDITを計算する
     * 
     * @param targetClass DIT計算対象クラス
     * @return 計算結果
     */
    @Override
    protected Number measureClassMetric(TargetClassInfo targetClass) {

        ClassInfo classInfo = targetClass;
        for (int depth = 1;; depth++) {

            final List<ClassTypeInfo> superClasses = classInfo.getSuperClasses();
            if (0 == superClasses.size()) {
                return depth;
            }
            classInfo = superClasses.get(0).getReferencedClass();
        }
    }

    /**
     * このプラグインの簡易説明を１行で返す
     * 
     * @return 簡易説明文字列
     */
    @Override
    protected String getDescription() {
        return "Measuring the DIT metric.";
    }

    /**
     * このプラグインが計測するメトリクスの名前を返す
     * 
     * @return DIT
     */
    @Override
    protected String getMetricName() {
        return "DIT";
    }

    /**
     * このプラグインがフィールドに関する情報を利用するかどうかを返すメソッド． falseを返す．
     * 
     * @return false．
     */
    @Override
    protected boolean useFieldInfo() {
        return false;
    }

    /**
     * このプラグインがメソッドに関する情報を利用するかどうかを返すメソッド． falseを返す．
     * 
     * @return false．
     */
    @Override
    protected boolean useMethodInfo() {
        return false;
    }
}
