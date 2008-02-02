package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NullUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決null使用を表すためのクラス．
 * UnresolvedEntityUsageInfoとEntityUsageInfoがクラスであるため，
 * NullUsageInfoでこれら両方を継承することができない．
 * そのための妥協案として作成したクラス．
 * 
 * @author higo
 *
 */
public final class UnresolvedNullUsageInfo implements UnresolvedEntityUsageInfo {

    public UnresolvedNullUsageInfo() {
        this.resolvedInfo = null;
    }

    /**
     * 名前解決されているかどうかを返す．
     * 
     * @return 名前解決されている場合は true，そうでない場合は false
     */
    @Override
    public boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * 名前解決された使用情報を返す
     * 
     * @return 名前解決された使用情報
     */
    @Override
    public EntityUsageInfo getResolvedEntityUsage() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    /**
     * 使用情報の名前解決する
     * 
     * @return 解決済みの使用情報
     */
    @Override
    public EntityUsageInfo resolveEntityUsage(final TargetClassInfo usingClass,
            final TargetMethodInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == classInfoManager) || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolvedEntityUsage();
        }

        this.resolvedInfo = new NullUsageInfo();
        return this.resolvedInfo;
    }

    private NullUsageInfo resolvedInfo;
}
