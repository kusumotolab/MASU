package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.external.ExternalClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 引数を表すためのクラス． 型を提供するのみ．
 * 
 * @author higo
 * 
 */
public final class UnresolvedParameterInfo extends UnresolvedVariableInfo<TargetParameterInfo> {

    /**
     * 引数オブジェクトを初期化する．名前と型が必要．
     * 
     * @param name 引数名
     * @param type 引数の型
     */
    public UnresolvedParameterInfo(final String name, final UnresolvedTypeInfo type) {
        super(name, type);
    }

    /**
     * 未解決引数情報を解決し，解決済み参照を返す．
     * 
     * @param usingClass 未解決引数情報の定義が行われているクラス
     * @param usingMethod 未解決引数情報の定義が行われているメソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * @return 解決済み引数情報
     */
    @Override
    public TargetParameterInfo resolveUnit(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == usingClass) || (null == usingMethod) || (null == classInfoManager)) {
            throw new NullPointerException();
        }

        // 既に解決済みである場合は，キャッシュを返す
        if (this.alreadyResolved()) {
            return this.getResolvedUnit();
        }

        // 修飾子，パラメータ名，型，位置情報を取得
        final Set<ModifierInfo> parameterModifiers = this.getModifiers();
        final String parameterName = this.getName();
        final UnresolvedTypeInfo unresolvedParameterType = this.getType();
        TypeInfo parameterType = unresolvedParameterType.resolveType(usingClass, usingMethod,
                classInfoManager, null, null);
        assert parameterType != null : "resolveTypeInfo returned null!";
        if (parameterType instanceof UnknownTypeInfo) {
            if (unresolvedParameterType instanceof UnresolvedClassReferenceInfo) {

                // TODO 型パラメータの情報を格納する
                final ExternalClassInfo externalClass = NameResolver
                        .createExternalClassInfo((UnresolvedClassReferenceInfo) unresolvedParameterType);
                parameterType = new ClassTypeInfo(externalClass);
                classInfoManager.add(externalClass);

            } else if (unresolvedParameterType instanceof UnresolvedArrayTypeInfo) {

                // TODO 型パラメータの情報を格納する
                final UnresolvedTypeInfo unresolvedElementType = ((UnresolvedArrayTypeInfo) unresolvedParameterType)
                        .getElementType();
                final int dimension = ((UnresolvedArrayTypeInfo) unresolvedParameterType)
                        .getDimension();
                final TypeInfo elementType = unresolvedElementType.resolveType(usingClass,
                        usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);
                parameterType = ArrayTypeInfo.getType(elementType, dimension);
            } else {
                assert false : "Can't resolve dummy parameter type : "
                        + unresolvedParameterType.toString();
            }
        }
        final int parameterFromLine = this.getFromLine();
        final int parameterFromColumn = this.getFromColumn();
        final int parameterToLine = this.getToLine();
        final int parameterToColumn = this.getToColumn();

        // パラメータオブジェクトを生成する
        this.resolvedInfo = new TargetParameterInfo(parameterModifiers, parameterName,
                parameterType, parameterFromLine, parameterFromColumn, parameterToLine,
                parameterToColumn);
        return this.resolvedInfo;
    }

}
