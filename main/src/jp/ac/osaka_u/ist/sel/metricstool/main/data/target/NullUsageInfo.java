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
     * null使用の型は不明
     * 
     * @return 不明型を返す
     */
    @Override
    public TypeInfo getType() {
        return NULLTYPE;
    }

    @Override
    public boolean alreadyResolved() {
        return true;
    }

    @Override
    public EntityUsageInfo getResolvedEntityUsage() {
        return this;
    }

    @Override
    public EntityUsageInfo resolveEntityUsage(TargetClassInfo usingClass,
            TargetMethodInfo usingMethod, ClassInfoManager classInfoManager,
            FieldInfoManager fieldInfoManager, MethodInfoManager methodInfoManager) {
        return this;
    }

    /**
     * null使用の型を保存するための定数
     */
    private static final TypeInfo NULLTYPE = UnknownTypeInfo.getInstance();
}
