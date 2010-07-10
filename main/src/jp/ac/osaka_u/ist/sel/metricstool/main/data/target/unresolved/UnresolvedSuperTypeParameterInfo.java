package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決型パラメータ（表記が「A super B」）を表すクラス
 * 
 * @author higo
 *
 */
public final class UnresolvedSuperTypeParameterInfo extends UnresolvedTypeParameterInfo {

    /**
     * 型パラメータ名，未解決派生クラス型を与えてオブジェクトを初期化
     * 
     * @param ownerUnit この型パラメータの所有ユニット(クラス or　メソッド)
     * @param name 型パラメータ名
     * @param index 何番目の型パラメータであるかを表す
     * @param extendsType 未解決基底クラス型
     * @param superType 未解決派生クラス型
     */
    public UnresolvedSuperTypeParameterInfo(final UnresolvedUnitInfo<?> ownerUnit,
            final String name, final int index,
            final UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> extendsType,
            final UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> superType) {

        super(ownerUnit, name, index, extendsType);

        if (null == superType) {
            throw new NullPointerException();
        }

        this.superType = superType;
    }

    /**
     * 名前解決を行う
     * 
     * @param usingClass 名前解決を行うエンティティがあるクラス
     * @param usingMethod 名前解決を行うエンティティがあるメソッド
     * @param classInfoManager 用いるクラスマネージャ
     * @param fieldInfoManager 用いるフィールドマネージャ
     * @param methodInfoManager 用いるメソッドマネージャ
     * 
     * @return 解決済みの型パラメータ
     */
    @Override
    public TypeParameterInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new NullPointerException();
        }

        final int index = this.getIndex();
        final String name = this.getName();
        /*
        final UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> unresolvedSuperType = this
                .getSuperType();
        final TypeInfo superType = unresolvedSuperType.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        //　型パラメータの所有ユニットを解決
        final UnresolvedUnitInfo<?> unresolvedOwnerUnit = this.getOwnerUnit();
        final TypeParameterizable ownerUnit = (TypeParameterizable) unresolvedOwnerUnit.resolve(
                usingClass, usingMethod, classInfoManager, fieldInfoManager, methodInfoManager);

        // extends 節 がある場合
        if (this.hasExtendsType()) {

            final UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> unresolvedExtendsType = this
                    .getExtendsType();
            final TypeInfo extendsType = unresolvedExtendsType.resolve(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);

            this.resolvedInfo = new SuperTypeParameterInfo(ownerUnit, name, index, extendsType,
                    superType);

        } else {

            this.resolvedInfo = new SuperTypeParameterInfo(ownerUnit, name, index, null, superType);
        }*/

        return super.resolve(usingClass, usingMethod, classInfoManager, fieldInfoManager,
                methodInfoManager);
    }

    /**
     * 未解決派生クラス型を返す
     * 
     * @return 未解決派生クラス型
     */
    public UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> getSuperType() {
        return this.superType;
    }

    /**
     * 未解決派生クラス型を保存する
     */
    private final UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> superType;
}
