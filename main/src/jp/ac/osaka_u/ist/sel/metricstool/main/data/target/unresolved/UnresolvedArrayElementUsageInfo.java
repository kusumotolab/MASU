package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayElementUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決配列に対する要素の参照を表すためのクラス．以下の情報を持つ．
 * 
 * @author kou-tngt, higo
 * @see UnresolvedEntityUsageInfo
 */
public final class UnresolvedArrayElementUsageInfo extends UnresolvedEntityUsageInfo {

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
    @Override
    public EntityUsageInfo resolveEntityUsage(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
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

        //　位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // 要素使用がくっついている未定義型を取得
        final UnresolvedEntityUsageInfo unresolvedOwnerUsage = this.getOwnerArrayType();
        EntityUsageInfo ownerUsage = unresolvedOwnerUsage.resolveEntityUsage(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        assert ownerUsage != null : "resolveEntityUsage returned null!";

        // 未解決型の名前解決ができなかった場合
        if (ownerUsage.getType() instanceof UnknownTypeInfo) {

            //            // 未解決型が配列型である場合は，型を作成する
            //            if (unresolvedOwnerUsage instanceof UnresolvedArrayTypeInfo) {
            //                final UnresolvedEntityUsageInfo unresolvedElementType = ((UnresolvedArrayTypeInfo) unresolvedOwnerUsage)
            //                        .getElementType();
            //                final int dimension = ((UnresolvedArrayTypeInfo) unresolvedOwnerUsage)
            //                        .getDimension();
            //                final ExternalClassInfo externalClassInfo = NameResolver
            //                        .createExternalClassInfo((UnresolvedClassReferenceInfo) unresolvedElementType);
            //                classInfoManager.add(externalClassInfo);
            //
            //                // TODO 型パラメータの情報を格納する
            //                final ReferenceTypeInfo reference = new ReferenceTypeInfo(externalClassInfo);
            //                ownerUsage = ArrayTypeInfo.getType(reference, dimension);
            //
            //                // 配列型以外の場合はどうしようもない
            //            } else {
            //
            //                usingMethod.addUnresolvedUsage(this);
            //                this.resolvedInfo = UnknownEntityUsageInfo.getInstance();
            //                return this.resolvedInfo;
            //            }
            usingMethod.addUnresolvedUsage(this);
            this.resolvedInfo = new UnknownEntityUsageInfo(fromLine, fromColumn, toLine, toColumn);
            return this.resolvedInfo;
        }

        this.resolvedInfo = new ArrayElementUsageInfo(ownerUsage, fromLine, fromColumn, toLine,
                toColumn);
        return this.resolvedInfo;
    }

    /**
     * この未解決配列要素の参照が解決済みかどうか返す
     * 
     * @return 解決済みの場合は true, そうでない場合は false
     */
    @Override
    public boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * この未解決配列要素の参照の解決済み要素を返す
     * 
     * @return 解決済み要素
     * @throws NotResolvedException 未解決状態でこのメソッドが呼ばれた場合にスローされる
     */
    @Override
    public EntityUsageInfo getResolvedEntityUsage() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
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
