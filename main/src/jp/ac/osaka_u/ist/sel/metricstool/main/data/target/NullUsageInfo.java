package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * null使用を表すクラス．
 * 
 * @author higo, t-miyake
 * 
 */
public final class NullUsageInfo extends EntityUsageInfo {

    public NullUsageInfo() {
        super();
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
