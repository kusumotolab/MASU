package jp.ac.osaka_u.ist.sel.metricstool.lcom1;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetFieldInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractClassMetricPlugin;


/**
 * 
 * LCOM1(CKメトリクスのLCOM)を計測するプラグインクラス.
 * <p>
 * 全てのオブジェクト指向言語に対応.
 * クラス，メソッド，フィールド，メソッド内部の情報を必要とする.
 * @author kou-tngt
 *
 */
public class Lcom1Plugin extends AbstractClassMetricPlugin {
    //  オブジェクトを何度も生成するのを回避するためにフィールドとして生成する
    /**
     * 対象クラスのメソッド一覧.
     * このオブジェクトは再利用される.
     */
    final List<TargetMethodInfo> methods = new ArrayList<TargetMethodInfo>(100);

    /** 
     * 対象クラスのインスタンスフィールド一覧.
     * このオブジェクトは再利用される.
     */
    final Set<TargetFieldInfo> instanceFields = new HashSet<TargetFieldInfo>();

    /**
     * 使用されたフィールド一覧
     * このオブジェクトは再利用される.
     */
    final Set<FieldInfo> usedFields = new HashSet<FieldInfo>();

    /**
     * 再利用しているオブジェクトを空にする.
     */
    protected void clearReusedObjects() {
        methods.clear();
        instanceFields.clear();
        usedFields.clear();
    }

    /**
     * オブジェクト再利用の後始末.
     */
    @Override
    protected void teardownExecute() {
        clearReusedObjects();
    }

    /**
     * メトリクスの計測.
     * 
     * @param targetClass 対象のクラス
     */
    @Override
    protected Number measureClassMetric(TargetClassInfo targetClass) {
        // オブジェクト再利用のための準備
        clearReusedObjects();

        int p = 0;
        int q = 0;

        methods.addAll(targetClass.getDefinedMethods());

        //このクラスのインスタンスフィールドのセットを取得
        instanceFields.addAll(targetClass.getDefinedFields());
        for (Iterator<TargetFieldInfo> it = instanceFields.iterator(); it.hasNext();) {
            if (it.next().isStaticMember()) {
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

        if (p <= q || allMethodsDontUseAnyField) {
            //pがq以下，または全てのメソッドがフィールドを利用しない場合lcomは0
            return Integer.valueOf(0);
        } else {
            //そうでないならp-qがlcom
            return Integer.valueOf(p - q);
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
     * メトリクス名を返す．
     * 
     * @return メトリクス名
     */
    @Override
    protected String getMetricName() {
        return "LCOM1";
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
