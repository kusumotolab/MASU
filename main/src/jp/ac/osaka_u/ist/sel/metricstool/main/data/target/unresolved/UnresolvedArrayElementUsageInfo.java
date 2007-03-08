package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決配列に対する要素の参照を表すためのクラス．以下の情報を持つ．
 * 
 * @author kou-tngt
 * @see UnresolvedEntityUsageInfo
 */
public class UnresolvedArrayElementUsageInfo implements UnresolvedEntityUsageInfo {

    /**
     * 要素が参照された配列の型を与える.
     * 
     * @param ownerArrayType 要素が参照された配列の型
     */
    public UnresolvedArrayElementUsageInfo(final UnresolvedEntityUsageInfo ownerArrayType) {

        if (null == ownerArrayType) {
            throw new NullPointerException("ownerArrayType is null.");
        }

        this.ownerArrayType = ownerArrayType;
        this.resolvedInfo = null;
    }

    /**
     * 未解決配列要素の参照を解決し，解決済み参照を返す．
     * 
     * @param usingClass 未解決配列要素参照が行われているクラス
     * @param usingMethod 未解決配列要素参照が行われているメソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * @return 解決済み参照
     */
    public EntityUsageInfo resolveEntityUsage(final TargetClassInfo usingClass,
            final TargetMethodInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)
                || (null == fieldInfoManager) || (null == methodInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolvedEntityUsage();
        }

        // 要素使用がくっついている未定義型を取得
        final UnresolvedEntityUsageInfo unresolvedOwnerUsage = this.getOwnerArrayType();
        EntityUsageInfo ownerUsage = unresolvedOwnerUsage.resolveEntityUsage(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        assert ownerUsage != null : "resolveEntityUsage returned null!";

        // 未解決型の名前解決ができなかった場合
        if (ownerUsage.getType() instanceof UnknownTypeInfo) {

            // 未解決型が配列型である場合は，型を作成する
            if (unresolvedOwnerUsage instanceof UnresolvedArrayTypeInfo) {
                final UnresolvedEntityUsageInfo unresolvedElementType = ((UnresolvedArrayTypeInfo) unresolvedOwnerUsage)
                        .getElementType();
                final int dimension = ((UnresolvedArrayTypeInfo) unresolvedOwnerUsage).getDimension();
                final ExternalClassInfo externalClassInfo = NameResolver
                        .createExternalClassInfo((UnresolvedClassReferenceInfo) unresolvedElementType);
                classInfoManager.add(externalClassInfo);
                ownerUsage = ArrayTypeInfo.getType(new ClassReferenceInfo(externalClassInfo),
                        dimension);

                // 配列型以外の場合はどうしようもない
            } else {

                usingMethod.addUnresolvedUsage(this);
                this.resolvedInfo = UnknownEntityUsageInfo.getInstance();
                return this.resolvedInfo;
            }
        }

        // 配列の次元に応じて型を生成
        final int ownerArrayDimension = ((ArrayTypeInfo) ownerUsage).getDimension();
        final EntityUsageInfo ownerArrayElement = ((ArrayTypeInfo) ownerUsage).getElement();

        // 配列が二次元以上の場合は，次元を一つ落とした配列を返し，配列が一次元の場合は要素の型を返す
        this.resolvedInfo = 1 < ownerArrayDimension ? ArrayTypeInfo.getType(ownerArrayElement,
                ownerArrayDimension - 1) : ownerArrayElement;
        return this.resolvedInfo;
    }

    /**
     * この未解決配列要素の参照が解決済みかどうか返す
     * 
     * @return 解決済みの場合は true, そうでない場合は false
     */
    public boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * この未解決配列要素の参照の解決済み要素を返す
     * 
     * @return 解決済み要素
     * @throws NotResolvedException 未解決状態でこのメソッドが呼ばれた場合にスローされる
     */
    public EntityUsageInfo getResolvedEntityUsage() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }

    /**
     * 未解決配列要素使用の型名を返す
     * 
     * @return 未解決配列要素使用の型名
     */
    public String getTypeName() {
        final String ownerTypeName = this.getOwnerArrayType().getTypeName();
        return "An element usage of " + ownerTypeName;
    }

    /**
     * 要素が参照された配列の型を返す
     * 
     * @return 要素が参照された配列の型
     */
    public UnresolvedEntityUsageInfo getOwnerArrayType() {
        return this.ownerArrayType;
    }

    /**
     * 要素が参照された配列の型
     */
    private final UnresolvedEntityUsageInfo ownerArrayType;

    /**
     * 解決済み配列要素使用を保存するための変数
     */
    private EntityUsageInfo resolvedInfo;

}
