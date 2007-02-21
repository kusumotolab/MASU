package jp.ac.osaka_u.ist.sel.metricstool.lcom1;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor.ClassInfoAccessor;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LANGUAGE;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.LanguageUtil;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;


/**
 * 
 * LCOM1(CKメトリクスのLCOM)を計測するプラグインクラス.
 * <p>
 * 全てのオブジェクト指向言語に対応.
 * クラス，メソッド，フィールド，メソッド内部の情報を必要とする.
 * @author kou-tngt
 *
 */
public class Lcom1Plugin extends AbstractPlugin {

    /**
     * メトリクス計測を開始する．
     */
    @Override
    protected void execute() {
        final List<TargetMethodInfo> methods = new ArrayList<TargetMethodInfo>(100);
        final Set<TargetFieldInfo> instanceFields = new HashSet<TargetFieldInfo>();
        final Set<FieldInfo> usedFields = new HashSet<FieldInfo>();

        //クラス情報アクセサを取得
        final ClassInfoAccessor accessor = this.getClassInfoAccessor();

        int measuredClassCount = 0;
        final int maxClassCount = accessor.getClassCount();

        //全クラスについて
        for (final TargetClassInfo cl : accessor) {
            int p = 0;
            int q = 0;

            methods.addAll(cl.getDefinedMethods());
            
            //このクラスのインスタンスフィールドのセットを取得
            instanceFields.addAll(cl.getDefinedFields());
            for(Iterator<TargetFieldInfo> it = instanceFields.iterator(); it.hasNext();){
                if (it.next().isStaticMember()){
                    it.remove();
                }
            }
            
            final int methodCount = methods.size();

            //フィールドを利用するメソッドが1つもないかどうか
            boolean allMethodsDontUseAnyField = true;

            //全メソッドi対して
            for (int i = 0; i < methodCount; i++) {
                //メソッドiを取得して，代入or参照しているフィールドを全てsetに入れる
                final TargetMethodInfo firstMethod = methods.get(i);
                usedFields.addAll(firstMethod.getAssignmentees());
                usedFields.addAll(firstMethod.getReferencees());
                
                //自クラスのインスタンスフィールドだけを残す
                usedFields.retainAll(instanceFields);

                if (allMethodsDontUseAnyField) {
                    //まだどのメソッドも1つもフィールドを利用していない場合
                    allMethodsDontUseAnyField = usedFields.isEmpty();
                }

                //i以降のメソッドjについて
                for (int j = i + 1; j < methodCount; j++) {
                    //メソッドjを取得して，参照しているフィールドが１つでもsetにあるかどうかを調べる
                    final TargetMethodInfo secondMethod = methods.get(j);
                    boolean isSharing = false;
                    for (final FieldInfo secondUsedField : secondMethod.getReferencees()) {
                        if (usedFields.contains(secondUsedField)) {
                            isSharing = true;
                            break;
                        }
                    }

                    //代入しているフィールドが１つでもsetにあるかどうかを調べる
                    if (!isSharing) {
                        for (final FieldInfo secondUsedField : secondMethod.getAssignmentees()) {
                            if (usedFields.contains(secondUsedField)) {
                                isSharing = true;
                                break;
                            }
                        }
                    }

                    //共有しているフィールドがあればqを，なければpを増やす
                    if (isSharing) {
                        q++;
                    } else {
                        p++;
                    }
                }

                usedFields.clear();
            }

            try {
                if (p <= q || allMethodsDontUseAnyField) {
                    //pがq以下，または全てのメソッドがフィールドを利用しない場合lcomは0
                    this.registMetric(cl, 0);
                } else {
                    //そうでないならp-qがlcom
                    this.registMetric(cl, p - q);
                }
            } catch (final MetricAlreadyRegisteredException e) {
                this.err.println(e);
            }

            methods.clear();
            instanceFields.clear();
            
            //1クラスごとに%で進捗報告
            this.reportProgress(++measuredClassCount * 100 / maxClassCount);
        }
    }

    /**
     * このプラグインの簡易説明を１行で返す
     * @return 簡易説明文字列
     */
    @Override
    protected String getDescription() {
        return "Measuring the LCOM1 metric(CK-metrics's LCOM).";
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
        return "LCOM1";
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
     * このプラグインがフィールドに関する情報を利用するかどうかを返すメソッド．
     * trueを返す．
     * 
     * @return true．
     */
    @Override
    protected boolean useFieldInfo() {
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
     * 詳細説明文字列定数
     */
    private final static String DETAIL_DESCRIPTION;

    static {
        final String lineSeparator = "\n";//System.getProperty("line.separator");//TODO　この辺のセキュリティは緩和した方がいいかも
        final StringBuilder builder = new StringBuilder();

        builder.append("This plugin measures the LCOM1 metric(CK-metrics's LCOM).");
        builder.append(lineSeparator);
        builder
                .append("The LCOM1 is one of the class cohesion metrics measured by following steps:");
        builder.append(lineSeparator);
        builder.append("1. P is a set of pairs of methods which do not share any field.");
        builder.append("If all methods do not use any field, P is a null set.");
        builder.append(lineSeparator);
        builder.append("2. Q is a set of pairs of methods which share some fields.");
        builder.append(lineSeparator);
        builder.append("3. If |P| > |Q|, the result is measured as |P| - |Q|, otherwise 0.");
        builder.append(lineSeparator);

        DETAIL_DESCRIPTION = builder.toString();
    }
}
