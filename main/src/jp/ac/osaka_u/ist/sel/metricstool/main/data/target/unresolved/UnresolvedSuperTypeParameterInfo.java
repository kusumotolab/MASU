package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SuperTypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public final class UnresolvedSuperTypeParameterInfo extends UnresolvedTypeParameterInfo {

    /**
     * 型パラメータ名，未解決派生クラス型を与えてオブジェクトを初期化
     * 
     * @param ownerUnit この型パラメータの所有ユニット(クラス or　メソッド)
     * @param name 型パラメータ名
     * @param extendsType 未解決基底クラス型
     * @param superType 未解決派生クラス型
     */
    public UnresolvedSuperTypeParameterInfo(final UnresolvedUnitInfo<?> ownerUnit,
            final String name, final int index, final UnresolvedTypeInfo extendsType,
            final UnresolvedTypeInfo superType) {

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
        final UnresolvedTypeInfo unresolvedSuperType = this.getSuperType();
        final TypeInfo superType = unresolvedSuperType.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        //　型パラメータの所有ユニットを解決
        final UnresolvedUnitInfo<?> unresolvedOwnerUnit = this.getOwnerUnit();
        final UnitInfo ownerUnit = unresolvedOwnerUnit.resolve(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        // extends 節 がある場合
        if (this.hasExtendsType()) {

            final UnresolvedTypeInfo unresolvedExtendsType = this.getExtendsType();
            final TypeInfo extendsType = unresolvedExtendsType.resolve(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);

            this.resolvedInfo = new SuperTypeParameterInfo(ownerUnit, name, index, extendsType,
                    superType);

        } else {

            this.resolvedInfo = new SuperTypeParameterInfo(ownerUnit, name, index, null, superType);
        }

        return this.resolvedInfo;
    }

    /**
     * 未解決派生クラス型を返す
     * 
     * @return 未解決派生クラス型
     */
    public UnresolvedTypeInfo getSuperType() {
        return this.superType;
    }

    /**
     * 未解決派生クラス型を保存する
     */
    private final UnresolvedTypeInfo superType;
}
