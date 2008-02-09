package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FieldInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決型パラメータを表す抽象クラス
 * 
 * @author higo
 * 
 */
public class UnresolvedTypeParameterInfo implements UnresolvedReferenceTypeInfo {
    // TODO 多言語対応時にUnresolvedTypeInfoに変更すべき

    /**
     * 型パラメータ名を与えてオブジェクトを初期化する
     * 
     * @param ownerUnit この型パラメータを定義しているユニット(クラス or メソッド)
     * @param name 型パラメータ名
     * @param extends 未解決基底クラス型
     */
    public UnresolvedTypeParameterInfo(final UnresolvedUnitInfo<?> ownerUnit, final String name,
            final UnresolvedTypeInfo extendsType) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == ownerUnit) || (null == name)) {
            throw new IllegalArgumentException();
        }

        // ownerUnitがメソッドかクラスでない場合はエラー
        if ((!(ownerUnit instanceof UnresolvedClassInfo))
                && (!(ownerUnit instanceof UnresolvedCallableUnitInfo))) {
            throw new IllegalArgumentException();
        }

        this.ownerUnit = ownerUnit;
        this.name = name;
        this.extendsType = extendsType;
    }

    /**
     * 既に名前解決されているかどうかを返す
     * 
     * @return 既に名前解決されている場合は true, そうでない場合は false
     */
    public final boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * 名前解決された情報を返す
     * 
     * @return 名前解決された情報
     * @throws NotResolvedException
     */
    public final TypeInfo getResolvedType() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
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
     * @return 解決済みのエンティティ
     */
    public TypeInfo resolveType(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {

        // 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == classInfoManager) {
            throw new NullPointerException();
        }

        //　型パラメータの所有ユニットを解決
        final UnresolvedUnitInfo<?> unresolvedOwnerUnit = this.getOwnerUnit();
        final UnitInfo ownerUnit = unresolvedOwnerUnit.resolveUnit(usingClass, usingMethod,
                classInfoManager, fieldInfoManager, methodInfoManager);

        final String name = this.getName();

        if (this.hasExtendsType()) {

            final UnresolvedTypeInfo unresolvedExtendsType = this.getExtendsType();
            final TypeInfo extendsType = unresolvedExtendsType.resolveType(usingClass, usingMethod,
                    classInfoManager, fieldInfoManager, methodInfoManager);

            this.resolvedInfo = new TypeParameterInfo(ownerUnit, name, extendsType);

        } else {

            this.resolvedInfo = new TypeParameterInfo(ownerUnit, name, null);
        }

        return this.resolvedInfo;
    }

    /**
     * この型パラメータを宣言しているユニット(クラス or メソッド)を返す
     * 
     * @return この型パラメータを宣言しているユニット(クラス or メソッド)
     */
    public final UnresolvedUnitInfo<?> getOwnerUnit() {
        return this.ownerUnit;
    }

    /**
     * 型パラメータ名を返す
     * 
     * @return 型パラメータ名
     */
    public final String getName() {
        return this.name;
    }

    /**
     * 基底クラスの未解決型情報を返す
     * 
     * @return 基底クラスの未解決型情報
     */
    public final UnresolvedTypeInfo getExtendsType() {
        return this.extendsType;
    }

    /**
     * 基底クラスを持つかどうかを返す
     * 
     * @return 基底クラスを持つ場合は true, 持たない場合は false
     */
    public final boolean hasExtendsType() {
        return null != this.extendsType;
    }

    /**
     * 型パラメータを宣言しているユニットを保存するための変数
     */
    private final UnresolvedUnitInfo<?> ownerUnit;

    /**
     * 型パラメータ名を保存するための変数
     */
    private final String name;

    /**
     * 基底クラスを保存するための変数
     */
    private final UnresolvedTypeInfo extendsType;

    /**
     * 名前解決された情報を保存するための変数
     */
    protected TypeParameterInfo resolvedInfo;
}
