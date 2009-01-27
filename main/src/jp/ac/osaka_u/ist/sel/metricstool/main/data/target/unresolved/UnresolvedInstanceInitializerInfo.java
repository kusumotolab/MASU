package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.InstanceInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * クラスのインスタンスイニシャライザの未解決情報を保存するクラス
 * 
 * @author t-miyake
 *
 */
public class UnresolvedInstanceInitializerInfo extends
        UnresolvedCallableUnitInfo<InstanceInitializerInfo> {

    /**
     * このインスタンスイニシャライザを所有するクラスを与えて初期化
     * @param ownerClass インスタンスイニシャライザを所有するクラス
     */
    public UnresolvedInstanceInitializerInfo(UnresolvedClassInfo ownerClass) {
        super(ownerClass);
    }

    @Override
    public void setInstanceMember(boolean instance) {

    }

    @Override
    public boolean isInstanceMember() {
        return true;
    }

    @Override
    public boolean isStaticMember() {
        return false;
    }

    @Override
    public InstanceInitializerInfo resolve(TargetClassInfo usingClass,
            CallableUnitInfo usingMethod, ClassInfoManager classInfoManager,
            FieldInfoManager fieldInfoManager, MethodInfoManager methodInfoManager) {
        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == usingClass) {
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

        this.resolvedInfo = ownerClass.getInstanceInitializer();
        return this.resolvedInfo;
    }

}
