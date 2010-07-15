package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 引数の使用を表すクラス
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public final class ParameterUsageInfo extends VariableUsageInfo<ParameterInfo> {

    /**
     * 使用されている引数を与えてオブジェクトを初期化
     * 
     * @param usedParameter 使用されている引数
     * @param reference 参照であるかどうか
     * @param assignment 代入であるかどうか
     * @param ownerMethod オーナーメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    private ParameterUsageInfo(final ParameterInfo usedParameter, final boolean reference,
            final boolean assignment, final CallableUnitInfo ownerMethod, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(usedParameter, reference, assignment, ownerMethod, fromLine, fromColumn, toLine,
                toColumn);
    }

    /**
     * 使用されているパラメータ，使用の種類，使用されている位置情報を与えてインスタンスを取得
     * 
     * @param usedParameter 使用されているパラメータ
     * @param reference 参照であるかどうか
     * @param assingment 代入であるかどうか
     * @param ownerMethod オーナーメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     * @return パラーメータ使用のインスタンス
     */
    public static ParameterUsageInfo getInstance(final ParameterInfo usedParameter,
            final boolean reference, final boolean assingment, final CallableUnitInfo ownerMethod,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        final ParameterUsageInfo instance = new ParameterUsageInfo(usedParameter, reference,
                assingment, ownerMethod, fromLine, fromColumn, toLine, toColumn);
        addParameterUsage(instance);
        return instance;
    }

    /**
     * パラメータ変数使用のインスタンスをパラメータ変数からパラメータ変数使用へのマップに追加
     * @param parameterUsage パラメータ変数使用
     */
    private static void addParameterUsage(final ParameterUsageInfo parameterUsage) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == parameterUsage) {
            throw new IllegalArgumentException("localVariableUsage is null");
        }

        final ParameterInfo usedParameter = parameterUsage.getUsedVariable();
        if (USAGE_MAP.containsKey(usedParameter)) {
            USAGE_MAP.get(usedParameter).add(parameterUsage);
        } else {
            final Set<ParameterUsageInfo> usages = new HashSet<ParameterUsageInfo>();
            usages.add(parameterUsage);
            USAGE_MAP.put(usedParameter, usages);
        }
    }

    /**
     * 与えられたパラメータの使用情報のセットを取得
     * @param parameter 使用情報を取得したいローカル変数
     * @return パラメータ使用のセット．引数で与えられたローカル変数が使用されていない場合はnull
     */
    public final static Set<ParameterUsageInfo> getUsages(final ParameterInfo parameter) {
        if (USAGE_MAP.containsKey(parameter)) {
            return USAGE_MAP.get(parameter);
        } else {
            return Collections.<ParameterUsageInfo> emptySet();
        }
    }

    /**
     * 与えられた変数利用のCollectionに含まれる引数利用のSetを返す
     * 
     * @param variableUsages 変数利用のCollection
     * @return 与えられた変数利用のCollectionに含まれる引数利用のSet
     */
    public final static Set<ParameterUsageInfo> getParameterUsages(
            Collection<VariableUsageInfo<?>> variableUsages) {
        final Set<ParameterUsageInfo> parameterUsages = new HashSet<ParameterUsageInfo>();
        for (final VariableUsageInfo<?> variableUsage : variableUsages) {
            if (variableUsage instanceof ParameterUsageInfo) {
                parameterUsages.add((ParameterUsageInfo) variableUsage);
            }
        }
        return Collections.unmodifiableSet(parameterUsages);
    }

    private static final Map<ParameterInfo, Set<ParameterUsageInfo>> USAGE_MAP = new HashMap<ParameterInfo, Set<ParameterUsageInfo>>();
}
