package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決パラーメータ使用を保存するためのクラス
 * 
 * @author t-miyake, higo
 *
 */
public final class UnresolvedParameterUsageInfo extends UnresolvedVariableUsageInfo {

    public UnresolvedParameterUsageInfo(final UnresolvedParameterInfo usedVariable,
            boolean reference) {

        super(reference);

        this.usedVariable = usedVariable;
    }

    @Override
    public EntityUsageInfo resolveEntityUsage(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolvedEntityUsage();
        }

        final ParameterInfo usedVariable = (ParameterInfo) this.getUsedVariable().resolveUnit(
                usingClass, usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        final boolean reference = this.isReference();

        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        this.resolvedInfo = new ParameterUsageInfo(usedVariable, reference, fromLine, fromColumn,
                toLine, toColumn);
        return this.resolvedInfo;
    }

    public UnresolvedParameterInfo getUsedVariable() {
        return this.usedVariable;
    }

    private UnresolvedParameterInfo usedVariable;
}
