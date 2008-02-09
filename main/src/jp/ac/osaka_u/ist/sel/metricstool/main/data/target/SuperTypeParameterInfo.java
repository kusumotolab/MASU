package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


public final class SuperTypeParameterInfo extends TypeParameterInfo {

    /**
     * 型パラメータ名，派生クラス型を与えてオブジェクトを初期化
     * 
     * @param ownerUnit この型パラメータの所有ユニット(クラス or メソッド)
     * @param name 型パラメータ名
     * @param extendsType 基底クラス型
     * @param superType 派生クラス型
     */
    public SuperTypeParameterInfo(final UnitInfo ownerUnit, final String name,
            final TypeInfo extendsType, final TypeInfo superType) {

        super(ownerUnit, name, extendsType);

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == superType) {
            throw new NullPointerException();
        }

        this.superType = superType;
    }

    /**
     * 派生クラス型を返す
     * 
     * @return 派生クラス型
     */
    public TypeInfo getSuperType() {
        return this.superType;
    }

    /**
     * 未解決派生クラス型を保存する
     */
    private final TypeInfo superType;
}
