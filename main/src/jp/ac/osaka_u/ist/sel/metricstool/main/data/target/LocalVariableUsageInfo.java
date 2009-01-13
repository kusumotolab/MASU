package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * ローカル変数の使用を表すクラス
 * 
 * @author higo
 * 
 */
public final class LocalVariableUsageInfo extends VariableUsageInfo<LocalVariableInfo> {

    /**
     * 使用されているローカル変数を与えてオブジェクトを初期化
     * 
     * @param usedLocalVariable 使用されているローカル変数
     * @param reference 参照であるかどうか
     * @param assignment 代入であるかどうか
     * @param ownerMethod オーナーメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    private LocalVariableUsageInfo(final LocalVariableInfo usedLocalVariable,
            final boolean reference, final boolean assignment, final CallableUnitInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(usedLocalVariable, reference, assignment, ownerMethod, fromLine, fromColumn, toLine,
                toColumn);
    }

    /**
     * 使用されているローカル変数，使用の種類，使用されている位置情報を与えてインスタンスを取得
     * 
     * @param usedLocalVariable 使用されているローカル変数
     * @param reference 参照であるかどうか
     * @param assingment 代入であるかどうか
     * @param ownerMethod オーナーメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     * @return ローカル変数使用のインスタンス
     */
    public static LocalVariableUsageInfo getInstance(final LocalVariableInfo usedLocalVariable,
            final boolean reference, final boolean assignment, final CallableUnitInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        final LocalVariableUsageInfo instance = new LocalVariableUsageInfo(usedLocalVariable,
                reference, assignment, ownerMethod, fromLine, fromColumn, toLine, toColumn);
        addLocalVariableUsage(instance);
        return instance;
    }

    /**
     * ローカル変数使用のインスタンスをローカル変数からローカル変数使用へのマップに追加
     * @param localVariableUsage ローカル変数使用
     */
    private static void addLocalVariableUsage(final LocalVariableUsageInfo localVariableUsage) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == localVariableUsage) {
            throw new IllegalArgumentException("localVariableUsage is null");
        }

        final LocalVariableInfo usedLocalVariable = localVariableUsage.getUsedVariable();
        if (USAGE_MAP.containsKey(usedLocalVariable)) {
            USAGE_MAP.get(usedLocalVariable).add(localVariableUsage);
        } else {
            final TreeSet<LocalVariableUsageInfo> usages = new TreeSet<LocalVariableUsageInfo>();
            usages.add(localVariableUsage);
            USAGE_MAP.put(usedLocalVariable, usages);
        }
    }

    /**
     * 与えられたローカル変数の使用情報のセットを取得
     * @param localVarible 使用情報を取得したいローカル変数
     * @return ローカル変数使用のセット．引数で与えられたローカル変数が使用されていない場合はnull
     */
    public final static Set<LocalVariableUsageInfo> getUsages(final LocalVariableInfo localVarible) {
        if (USAGE_MAP.containsKey(localVarible)) {
            return USAGE_MAP.get(localVarible);
        } else {
            return Collections.<LocalVariableUsageInfo> emptySet();
        }
    }

    private static final Map<LocalVariableInfo, TreeSet<LocalVariableUsageInfo>> USAGE_MAP = new HashMap<LocalVariableInfo, TreeSet<LocalVariableUsageInfo>>();
}