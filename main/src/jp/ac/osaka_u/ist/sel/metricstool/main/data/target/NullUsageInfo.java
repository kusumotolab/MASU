package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * null使用を表すクラス．
 * 
 * @author higo, t-miyake
 * 
 */
public final class NullUsageInfo extends EntityUsageInfo {

    public NullUsageInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);
    }

    /**
     * null使用の型は不明
     * 
     * @return 不明型を返す
     */
    @Override
    public TypeInfo getType() {
        return NULLTYPE;
    }

    /**
     * null使用の型を保存するための定数
     */
    private static final TypeInfo NULLTYPE = UnknownTypeInfo.getInstance();
}
