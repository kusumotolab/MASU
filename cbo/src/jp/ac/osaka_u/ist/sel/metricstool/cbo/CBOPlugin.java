package jp.ac.osaka_u.ist.sel.metricstool.cbo;


import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
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
     * @param targetClass CBO計算対象クラス
     * @return 計算結果
     */
    @Override
    protected Number measureClassMetric(TargetClassInfo targetClass) {

        Set<ClassInfo> classes = new HashSet<ClassInfo>();

        // フィールドで使用されているクラス型を取得
        for (final TargetFieldInfo field : targetClass.getDefinedFields()) {
            final TypeInfo type = field.getType();
            if (type instanceof ClassInfo) {
                classes.add((ClassInfo) type);
            }
        }

        // メソッドで使用されているクラス型を取得
        for (final TargetMethodInfo method : targetClass.getDefinedMethods()) {

            // 返り値についての処理
            {
                final TypeInfo returnType = method.getReturnType();
                if (returnType instanceof ClassInfo) {
                    classes.add((ClassInfo) returnType);
                }
            }

            // 引数のついての処理
            for (final ParameterInfo parameter : method.getParameters()) {
                final TypeInfo parameterType = parameter.getType();
                if(parameterType instanceof ClassInfo){
                    classes.add((ClassInfo) parameterType);
                }
            }
            
            // ローカル変数についての処理
            for (final LocalVariableInfo variable : method.getLocalVariables()){
                final TypeInfo variableType = variable.getType();
                if(variableType instanceof ClassInfo){
                    classes.add((ClassInfo)variableType);
                }
            }
        }
        
        //自分自身は取り除く
        classes.remove(targetClass);

        return classes.size();
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
