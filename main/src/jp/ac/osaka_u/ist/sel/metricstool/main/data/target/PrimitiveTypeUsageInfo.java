package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * プリミティブ型の使用を表すクラス
 * 
 * @author higo
 *
 */
public final class PrimitiveTypeUsageInfo extends EntityUsageInfo {

    /**
     * 使用されている型，文字列，位置情報を与えて初期化
     * @param type 使用されている型
     * @param literal　文字列
     * @param fromLine　開始行
     * @param fromColumn　開始列
     * @param toLine　終了行
     * @param toColumn　終了列
     */
    public PrimitiveTypeUsageInfo(final PrimitiveTypeInfo type, final String literal,
            final int fromLine, final int fromColumn, final int toLine, final int toColumn) {

        super(fromLine, fromColumn, toLine, toColumn);

        this.type = type;
        this.literal = literal;
    }

    public String getLiteral() {
        return this.literal;
    }

    @Override
    public TypeInfo getType() {
        return this.type;
    }

    private final PrimitiveTypeInfo type;

    private final String literal;
}
