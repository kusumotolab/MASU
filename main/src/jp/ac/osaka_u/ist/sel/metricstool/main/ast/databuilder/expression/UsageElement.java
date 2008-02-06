package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import java.util.Map;
import java.util.WeakHashMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableUsageInfo;

/**
 * @author t-miyake
 *
 */
public class UsageElement implements ExpressionElement {

    public static synchronized UsageElement getInstance(final UnresolvedEntityUsageInfo usageInfo) {
        UsageElement instance = instanceMap.get(usageInfo);
        if (null == instance) {
            instance = new UsageElement(usageInfo);
            instanceMap.put(usageInfo, instance);
        }
        return instance;
    }
    
    public UnresolvedTypeInfo getType() {
        return null;
    }

    private UsageElement(final UnresolvedEntityUsageInfo usage) {
        if (null == usage) {
            throw new NullPointerException("usage is null.");
        }
        this.usage = usage;
    }
    
    public UnresolvedEntityUsageInfo getUsage() {
        return this.usage;
    }

    public boolean isMemberCall() {
        return this.usage instanceof UnresolvedCallInfo;
    }
    
    public boolean isVariableUsage() {
        return this.usage instanceof UnresolvedVariableUsageInfo;
    }
    
    private final UnresolvedEntityUsageInfo usage;

    private static final Map<UnresolvedEntityUsageInfo, UsageElement> instanceMap = new WeakHashMap<UnresolvedEntityUsageInfo, UsageElement>();
}
