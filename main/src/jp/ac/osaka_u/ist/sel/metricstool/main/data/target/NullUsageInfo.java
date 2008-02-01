package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsageInfo;


/**
 * null使用を表すクラス．
 * 
 * @author higo, t-miyake
 * 
 */
public final class NullUsageInfo extends EntityUsageInfo implements UnresolvedEntityUsageInfo {

    public NullUsageInfo() {
        super();
    }

    /**
     * 名前解決されているかどうかを返す．
     * 
     * @return 常に true を返す
     */
    public boolean alreadyResolved() {
        return true;
    }

    /**
     * 名前解決された使用情報を返す
     * 
     * @return 自分自身を返す
     */
    @Override
    public EntityUsageInfo getResolvedEntityUsage() {
        return this;
    }

    /**
     * 使用情報の名前解決する
     * nullは既に解決済みなので自分自身をそのまま返す
     * 
     * @return 解決済みの使用情報（自分自身）
     */
    @Override
    public EntityUsageInfo resolveEntityUsage(TargetClassInfo usingClass,
            TargetMethodInfo usingMethod, ClassInfoManager classInfoManager,
            FieldInfoManager fieldInfoManager, MethodInfoManager methodInfoManager) {
        return this;
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
