package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * リテラルの使用を表すクラス
 * @author t-miyake
 *
 */
public final class LiteralUsageInfo extends ExpressionInfo {

    /**
     * リテラル、リテラルの型、出現位置を与えて初期化
     * 
     * @param ownerMethod オーナーメソッド
     * @param literal リテラル
     * @param type リテラルの型
     * @param fromLine 開始行
     * @param fromColumn 開始列
     * @param toLine 終了行
     * @param toColumn 終了列
     */
    public LiteralUsageInfo(final String literal, final PrimitiveTypeInfo type,
            final CallableUnitInfo ownerMethod, final int fromLine, final int fromColumn,
            final int toLine, final int toColumn) {

        super(ownerMethod, fromLine, fromColumn, toLine, toColumn);

        this.literal = literal;
        this.type = type;

    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo#getType()
     */
    @Override
    public final TypeInfo getType() {
        return this.type;
    }

    /**
     * リテラルの文字列を返す
     * 
     * @return リテラルの文字列
     */
    public final String getLiteral() {
        return this.literal;
    }

    /**
     * リテラルは変数参照ではないので空のセットを返す
     * 
     * @return 空のセット
     */
    @Override
    public final Set<VariableUsageInfo<?>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }

    /**
     * このリテラル使用のテキスト表現（String型）を返す
     * 
     * @return このリテラル使用のテキスト表現（String型）
     */
    @Override
    public String getText() {
        return this.getLiteral();
    }

    /**
     * リテラルを保存するための変数
     */
    private final String literal;

    /**
     * リテラルの型を保存するための変数
     */
    private final PrimitiveTypeInfo type;
}