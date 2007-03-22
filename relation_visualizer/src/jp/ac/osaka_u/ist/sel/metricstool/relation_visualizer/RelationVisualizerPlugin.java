package jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer;


import java.io.File;
import java.io.IOException;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.ClassInfoAccessor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;
import jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph_builder.SimpleGraphBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.relation_visualizer.graph_builder.GraphvizWriter;


/**
 * プログラム要素間の関係を出力するプラグイン.
 * 
 * 現状の対応言語は Java のみ.
 * メソッド（関数）は必ずクラスの中で定義された言語のみしか扱えないため.
 * 
 * 何も計測しない.
 * 
 * @author rniitani
 */
public class RelationVisualizerPlugin extends AbstractPlugin {

    @Override
    protected void execute() {
        // クラス情報アクセサを取得
        final ClassInfoAccessor classAccessor = this.getClassInfoAccessor();

        // 進捗報告用
        // XXX 適当
        final int ADD_VERTEX_PHASE_PROGRESS = 45;
        final int ADD_EDGE_PHASE_PROGRESS = 90;
        final int WRITE_FILE_PHASE_PROGRESS = 100;

        // グラフ
        SimpleGraphBuilder builder = new SimpleGraphBuilder();

        // 頂点追加
        for (final TargetClassInfo targetClass : classAccessor) {
            // クラス追加
            builder.addClass(targetClass);
            // メソッド追加
            for (final TargetMethodInfo targetMethod : targetClass.getDefinedMethods()) {
                builder.addMethod(targetClass, targetMethod);
            }
            // フィールド追加
            for (final TargetFieldInfo targetField : targetClass.getDefinedFields()) {
                builder.addField(targetClass, targetField);
            }
        }
        // XXX 適当に進捗報告
        this.reportProgress(ADD_VERTEX_PHASE_PROGRESS);

        // 辺追加
        for (final TargetClassInfo targetClass : classAccessor) {
            // superclass
            for (final ClassInfo subclass : targetClass.getSubClasses()) {
                builder.addSuperclassRelation(targetClass, subclass);
            }
            // innerclass
            for (final ClassInfo inner : targetClass.getInnerClasses()) {
                builder.addSuperclassRelation(targetClass, inner);
            }
            // call
            for (final TargetMethodInfo targetMethod : targetClass.getDefinedMethods()) {
                for (final MethodInfo callee : targetMethod.getCallees()) {
                    builder.addCallRelation(targetMethod, callee);
                }
            }
            // TODO その他の関係の追加
        }
        // XXX 適当に進捗報告
        this.reportProgress(ADD_EDGE_PHASE_PROGRESS);

        try {
            GraphvizWriter.write(new File(getPluginRootDir(), "graph.dot"), builder.getResult());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // XXX 適当に進捗報告
        this.reportProgress(WRITE_FILE_PHASE_PROGRESS);
    }

    /**
     * メトリクス名を返す．
     * 
     * @return メトリクス名
     */
    @Override
    protected String getMetricName() {
        return "RV";
    }

    /**
     * とりあえずクラスのメトリクスを計測していることにする.
     */
    @Override
    protected METRIC_TYPE getMetricType() {
        return METRIC_TYPE.CLASS_METRIC;
    }

}
