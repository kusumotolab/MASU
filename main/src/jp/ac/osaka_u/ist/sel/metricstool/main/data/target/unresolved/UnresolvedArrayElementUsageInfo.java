package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayElementUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決配列に対する要素の参照を表すためのクラス．以下の情報を持つ．
 * 
 * @author kou-tngt, higo
 * @see UnresolvedEntityUsageInfo
 */
public final class UnresolvedArrayElementUsageInfo extends
        UnresolvedEntityUsageInfo<ArrayElementUsageInfo> {

    /**
     * 要素が参照された配列の型を与える.
     * 
     * @param ownerArrayType 要素が参照された配列の型
     * @param indexExpression 参照された要素のインデックス
     */
    public UnresolvedArrayElementUsageInfo(final UnresolvedEntityUsageInfo<?> ownerArrayType,
            final UnresolvedExpressionInfo<?> indexExpression) {

        if (null == ownerArrayType) {
            throw new NullPointerException("ownerArrayType is null.");
        }

        this.ownerArrayType = ownerArrayType;
        this.indexExpression = indexExpression;
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
    public ArrayElementUsageInfo resolve(final TargetClassInfo usingClass,
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
            return this.getResolved();
        }

        //　位置情報を取得
        final int fromLine = this.getFromLine();
        final int fromColumn = this.getFromColumn();
        final int toLine = this.getToLine();
        final int toColumn = this.getToColumn();

        // 要素使用のインデックスを名前解決
        final UnresolvedExpressionInfo<?> unresolvedIndexExpression = this.getIndexExpression();
        final ExpressionInfo indexExpression = unresolvedIndexExpression.resolve(usingClass,
                usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
        assert indexExpression != null : "method \"resolve\" returned null!";

        // 要素使用がくっついている未定義型を取得
        final UnresolvedEntityUsageInfo<?> unresolvedOwnerUsage = this.getOwnerArrayType();
        EntityUsageInfo ownerUsage = unresolvedOwnerUsage.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);
        assert ownerUsage != null : "method \"resolve\" returned null!";

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

            // 親が特定できない場合も配列の要素使用を作成して返す
            // もしかすると，UnknownEntityUsageInfoを返す方が適切かもしれない
            this.resolvedInfo = new ArrayElementUsageInfo(ownerUsage, indexExpression, fromLine,
                    fromColumn, toLine, toColumn);
            return this.resolvedInfo;
        }

        this.resolvedInfo = new ArrayElementUsageInfo(ownerUsage, indexExpression, fromLine,
                fromColumn, toLine, toColumn);
        return this.resolvedInfo;
    }

    /**
     * 要素が参照された配列の型を返す
     * 
     * @return 要素が参照された配列の型
     */
    public UnresolvedEntityUsageInfo<?> getOwnerArrayType() {
        return this.ownerArrayType;
    }

    /**
     * 参照された要素のインデックスを返す
     * 
     * @return　参照された要素のインデックス
     */
    public UnresolvedExpressionInfo<?> getIndexExpression() {
        return this.indexExpression;
    }

    /**
     * 要素が参照された配列の型
     */
    private final UnresolvedEntityUsageInfo<?> ownerArrayType;

    /**
     * 配列要素使用のインデックスを格納する変数
     */
    private final UnresolvedExpressionInfo<?> indexExpression;

}
