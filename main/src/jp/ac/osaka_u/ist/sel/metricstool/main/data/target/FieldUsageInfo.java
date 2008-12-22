package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * フィールドの使用を表すクラス
 * 
 * @author higo
 * 
 */
public class FieldUsageInfo extends VariableUsageInfo<FieldInfo> {

    /**
     * 使用されているフィールドを与えてオブジェクトを初期化
     * 
     * @param ownerUsage フィールド使用が実行される親の式
     * @param usedField 使用されているフィールド
     * @param reference 参照である場合は true, 代入である場合は false
     */
    protected FieldUsageInfo(final ExpressionInfo ownerExpression, final TypeInfo ownerType,
            final FieldInfo usedField, final boolean reference, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(usedField, reference, fromLine, fromColumn, toLine, toColumn);

        this.qualifierExpression = ownerExpression;
        this.qualifierType = ownerType;
    }

    /**
     * このフィールド使用の型を返す
     * 
     * @return このフィールド使用の型
     */
    @Override
    public TypeInfo getType() {

        final FieldInfo usedVariable = this.getUsedVariable();
        final TypeInfo definitionType = usedVariable.getType();

        // 定義の返り値が型パラメータでなければそのまま返せる
        if (!(definitionType instanceof TypeParameterInfo)) {
            return definitionType;
        }

        //　準備
        final int typeParameterIndex = ((TypeParameterInfo) definitionType).getIndex();
        final ClassTypeInfo callOwnerType = (ClassTypeInfo) this.getQualifierType();
        final TypeInfo typeArgument = callOwnerType.getTypeArgument(typeParameterIndex);
        return typeArgument;
    }

    /**
     * このフィールド使用の親，つまりこのフィールド使用がくっついている式を返す
     * 
     * @return このフィールド使用の親
     */
    public final TypeInfo getQualifierType() {
        return this.qualifierType;
    }

    /**
     * フィールド使用が実行される親の式を返す
     * @return フィールド使用が実行される親の式
     */
    public final ExpressionInfo getQualifierExpression() {
        return this.qualifierExpression;
    }

    /**
     * この式（フィールド使用）における変数利用の一覧を返す
     * 
     * @return 変数利用のSet
     */
    @Override
    public SortedSet<VariableUsageInfo<?>> getVariableUsages() {

        final SortedSet<VariableUsageInfo<?>> variableUsages = new TreeSet<VariableUsageInfo<?>>();
        variableUsages.addAll(super.getVariableUsages());

        final ExpressionInfo ownerExpression = this.getQualifierExpression();
        variableUsages.addAll(ownerExpression.getVariableUsages());

        return Collections.unmodifiableSortedSet(variableUsages);
    }

    private final TypeInfo qualifierType;

    /**
     * フィールド参照が実行される親の式を保存する変数
     */
    private final ExpressionInfo qualifierExpression;

    /**
     * 必要な情報を与えて，インスタンスを取得
     * 
     * @param qualifierExpression 親の式
     * @param qualifierType 親エンティティの型
     * @param usedField 使用されているフィールド
     * @param reference 参照である場合はtrue，代入である場合はfalse
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     * @return フィールド使用のインスタンス
     */
    public static FieldUsageInfo getInstance(final ExpressionInfo qualifierExpression,
            final TypeInfo qualifierType, final FieldInfo usedField, final boolean reference,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {
        final FieldUsageInfo instance = new FieldUsageInfo(qualifierExpression, qualifierType,
                usedField, reference, fromLine, fromColumn, toLine, toColumn);
        addFieldUsage(instance);
        return instance;
    }

    /**
     * フィールド使用のインスタンスをフィールドからフィールド使用へのマップに追加
     * @param fieldUsage フィールド使用
     */
    private static void addFieldUsage(final FieldUsageInfo fieldUsage) {

        MetricsToolSecurityManager.getInstance().checkAccess();

        if (null == fieldUsage) {
            throw new IllegalArgumentException("localVariableUsage is null");
        }

        final FieldInfo usedField = fieldUsage.getUsedVariable();
        if (USAGE_MAP.containsKey(usedField)) {
            USAGE_MAP.get(usedField).add(fieldUsage);
        } else {
            final TreeSet<FieldUsageInfo> usages = new TreeSet<FieldUsageInfo>();
            usages.add(fieldUsage);
            USAGE_MAP.put(usedField, usages);
        }
    }

    /**
     * 与えられたフィールドの使用情報のセットを取得
     * @param field 使用情報を取得したいフィールド
     * @return フィールド使用のセット．引数で与えられたフィールドが使用されていない場合はnull
     */
    public final static Set<FieldUsageInfo> getUsages(final FieldInfo field) {
        if (USAGE_MAP.containsKey(field)) {
            return USAGE_MAP.get(field);
        } else {
            return Collections.<FieldUsageInfo> emptySet();
        }
    }

    private static final Map<FieldInfo, TreeSet<FieldUsageInfo>> USAGE_MAP = new HashMap<FieldInfo, TreeSet<FieldUsageInfo>>();
}
