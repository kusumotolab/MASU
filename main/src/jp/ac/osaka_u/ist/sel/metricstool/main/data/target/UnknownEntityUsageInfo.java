package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * 未解決エンティティ利用を表すクラス
 * 
 * @author higo
 *
 */
public final class UnknownEntityUsageInfo extends ExpressionInfo {

    @Override
    public TypeInfo getType() {
        return UnknownTypeInfo.getInstance();
    }

    /**
     * 位置情報を与えて，オブジェクトを初期化
     * 
     * @param ownerMethod オーナーメソッド
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public UnknownEntityUsageInfo(final CallableUnitInfo ownerMethod, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {
        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);
    }

    @Override
    public Set<VariableUsageInfo<?>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }

    /**
     * この未解決エンティティ使用のテキスト表現（型）を返す
     * 
     * @return この未解決エンティティ使用のテキスト表現（型）
     */
    @Override
    public String getText() {
        return UNKNOWNSTRING;
    }

    private static final String UNKNOWNSTRING = new String("UNKNOWN");
}
