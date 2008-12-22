package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedVariableUsageInfo;

/**
 * @author t-miyake
 *
 */
public class UsageElement extends ExpressionElement {

    public UnresolvedTypeInfo<? extends TypeInfo> getType() {
        return null;
    }

    public UsageElement(final UnresolvedEntityUsageInfo<? extends EntityUsageInfo> usage) {
        super(usage);
    }
    
    public boolean isMemberCall() {
        return this.usage instanceof UnresolvedCallInfo;
    }
    
    public boolean isVariableUsage() {
        return this.usage instanceof UnresolvedVariableUsageInfo;
    }

}
