package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StaticInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決 static initializer を表すクラス
 * 
 * @author t-miyake, higo
 */
public class UnresolvedStaticInitializerInfo extends
        UnresolvedCallableUnitInfo<StaticInitializerInfo> {

    /**
     * 所有クラスを与えて，オブジェクトを初期化
     * 
     * @param ownerClass 所有クラス
     */
    public UnresolvedStaticInitializerInfo(final UnresolvedClassInfo ownerClass) {
        super(ownerClass);
    }

    /**
     * 名前解決を行う
     */
    @Override
    public StaticInitializerInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolved();
        }

        // 所有クラスを取得
        final UnresolvedClassInfo unresolvedOwnerClass = this.getOwnerClass();
        final TargetClassInfo ownerClass = unresolvedOwnerClass.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        this.resolvedInfo = new StaticInitializerInfo(ownerClass, this.getFromLine(), this
                .getFromColumn(), this.getToLine(), this.getToColumn());
        return this.resolvedInfo;
    }

    @Override
    public boolean isStaticMember() {
        return true;
    }

    @Override
    public boolean isInstanceMember() {
        return true;
    }

    /**
     * なにもしない
     */
    @Override
    public void setInstanceMember(boolean instance) {
    }
}
