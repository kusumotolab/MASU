package jp.ac.osaka_u.ist.sel.metricstool.cbo;


import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractClassMetricPlugin;


/**
 * CBO(CKメトリクスの1つ)を計算するメトリクス
 * <p>
 * 全てのオブジェクト指向言語に対応
 * 
 * @author y-higo
 * 
 */
public class CBOPlugin extends AbstractClassMetricPlugin {

    /**
     * 引数で与えられたクラスのCBOを計算する
     * 
     * @param targetClass
     *            CBO計算対象クラス
     * @return 計算結果
     */
    @Override
    protected Number measureClassMetric(TargetClassInfo targetClass) {

        SortedSet<ClassInfo<?, ?, ?, ?>> classes = new TreeSet<ClassInfo<?, ?, ?, ?>>();

        // フィールドで使用されているクラス型を取得
        for (final TargetFieldInfo field : targetClass.getDefinedFields()) {
            final TypeInfo type = field.getType();
            classes.addAll(this.getCohesiveClasses(type));
        }

        // メソッドで使用されているクラス型を取得
        for (final TargetMethodInfo method : targetClass.getDefinedMethods()) {

            // 返り値についての処理
            {
                final TypeInfo returnType = method.getReturnType();
                classes.addAll(this.getCohesiveClasses(returnType));
            }

            // 引数のついての処理
            for (final ParameterInfo parameter : method.getParameters()) {
                final TypeInfo parameterType = parameter.getType();
                classes.addAll(this.getCohesiveClasses(parameterType));
            }

            // ローカル変数についての処理
            for (final VariableInfo<? extends UnitInfo> variable : LocalVariableInfo
                    .getLocalVariables(method.getDefinedVariables())) {
                final TypeInfo variableType = variable.getType();
                classes.addAll(this.getCohesiveClasses(variableType));
            }
        }

        // 自分自身は取り除く
        classes.remove(targetClass);

        return classes.size();
    }

    /**
     * 与えられた型で利用されているクラスのSortedSetを返す
     * 
     * @param type 対象の型
     * @return typeで利用されているクラスのSortedSet
     */
    private SortedSet<ClassInfo<?, ?, ?, ?>> getCohesiveClasses(final TypeInfo type) {

        final SortedSet<ClassInfo<?, ?, ?, ?>> cohesiveClasses = new TreeSet<ClassInfo<?, ?, ?, ?>>();

        if (type instanceof ClassTypeInfo) {
            final ClassTypeInfo classType = (ClassTypeInfo) type;
            cohesiveClasses.add(classType.getReferencedClass());
            for (final TypeInfo typeArgument : classType.getTypeArguments()) {
                cohesiveClasses.addAll(this.getCohesiveClasses(typeArgument));
            }
        }

        return Collections.unmodifiableSortedSet(cohesiveClasses);
    }

    /**
     * このプラグインの簡易説明を１行で返す
     * 
     * @return 簡易説明文字列
     */
    @Override
    protected String getDescription() {
        return "Measuring the CBO metric.";
    }

    /**
     * このプラグインが計測するメトリクスの名前を返す
     * 
     * @return CBO
     */
    @Override
    protected String getMetricName() {
        return "CBO";
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
