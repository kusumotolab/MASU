package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * キャストの使用を表すクラス
 * 
 * @author higo
 *
 */
public final class CastUsageInfo extends EntityUsageInfo {

    /**
     * 必要な情報を与えてオブジェクトを初期化
     * 
     * @param castType キャストの型
     * @param castedUsage キャストされる要素
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public CastUsageInfo(final TypeInfo castType, final ExpressionInfo castedUsage,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == castType || null == castedUsage) {
            throw new IllegalArgumentException();
        }

        this.castType = castType;
        this.castedUsage = castedUsage;
    }

    /**
     * このキャストの型を返す
     * 
     * @return このキャストの型
     */
    @Override
    public TypeInfo getType() {
        return this.castType;
    }

    /**
     * キャストされる要素を返す
     * 
     * @return キャストされる要素
     */
    public ExpressionInfo getCastedUsage() {
        return this.castedUsage;
    }

    /**
     * この式（キャスト使用）における変数利用の一覧を返す
     * 
     * @return 変数利用のSet
     */
    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return this.getCastedUsage().getVariableUsages();
    }

    /**
     * このキャスト使用のテキスト表現（String型）を返す
     * 
     * @return このキャスト使用のテキスト表現（String型）
     */
    @Override
    public String getText() {

        final StringBuilder sb = new StringBuilder();

        sb.append("(");

        final ExpressionInfo expression = this.getCastedUsage();
        sb.append(expression.getText());

        sb.append(")");

        return sb.toString();
    }

    private final TypeInfo castType;

    private final ExpressionInfo castedUsage;
}
