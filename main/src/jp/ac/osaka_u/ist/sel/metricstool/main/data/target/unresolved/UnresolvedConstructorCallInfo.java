package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決コンストラクタ呼び出しを保存するためのクラス
 * 
 * @author t-miyake, higo
 *
 */
public final class UnresolvedConstructorCallInfo extends UnresolvedMemberCallInfo {

    /**
     * コンストラクタ呼び出しが実行される参照型と名前を与えてオブジェクトを初期化
     * 
     * @param unresolvedClassType コンストラクタ呼び出しが実行される型
     */
    public UnresolvedConstructorCallInfo(final UnresolvedReferenceTypeInfo unresolvedClassType) {

        super();

        if (null == unresolvedClassType) {
            throw new IllegalArgumentException();
        }

        this.unresolvedClassType = unresolvedClassType;
        this.memberName = unresolvedClassType.getTypeName();
    }

    @Override
    public EntityUsageInfo resolveEntityUsage(final TargetClassInfo usingClass,
            final TargetMethodInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolvedEntityUsage();
        }

        // コンストラクタのシグネチャを取得
        final String name = this.getName();
        final List<EntityUsageInfo> actualParameters = super.resolveParameters(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        //　コンストラクタの型を解決
        final TypeInfo classType = this.getClassType().resolveType(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        if (!(classType instanceof ReferenceTypeInfo)) {
            assert false : "Error handling must be inserted!";
        }

        final ClassInfo referencedClass = ((ReferenceTypeInfo) classType).getReferencedClass();
        if (referencedClass instanceof TargetClassInfo) {

        } else if (referencedClass instanceof ExternalClassInfo) {

        }

        // TODO 
        return null;
    }

    public UnresolvedReferenceTypeInfo getClassType() {
        return this.unresolvedClassType;
    }

    private final UnresolvedReferenceTypeInfo unresolvedClassType;

}
