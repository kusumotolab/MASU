package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public final class UnresolvedLiteralUsageInfo extends UnresolvedEntityUsageInfo {

    public UnresolvedLiteralUsageInfo(final String literal, final PrimitiveTypeInfo type) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == literal) || (null == type)) {
            throw new IllegalArgumentException();
        }

        this.literal = literal;
        this.type = type;
    }

    @Override
    public final boolean alreadyResolved() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public final EntityUsageInfo getResolvedEntityUsage() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EntityUsageInfo resolveEntityUsage(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * このリテラル使用の文字列を返す
     * 
     * @return このリテラル使用の文字列
     */
    public final String getLiteral() {
        return this.literal;
    }

    /**
     * このリテラル使用の文字列を返す
     * 
     * @return　このリテラル使用の文字列を返す
     */
    public final UnresolvedTypeInfo getType() {
        return this.type;
    }

    private final String literal;

    private final PrimitiveTypeInfo type;
}
