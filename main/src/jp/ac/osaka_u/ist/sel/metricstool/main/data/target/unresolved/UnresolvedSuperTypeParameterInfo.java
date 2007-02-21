package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


public final class UnresolvedSuperTypeParameterInfo extends UnresolvedTypeParameterInfo {

    /**
     * 型パラメータ名，未解決派生クラス型を与えてオブジェクトを初期化
     * 
     * @param name 型パラメータ名
     * @param extendsType 未解決派生クラス型
     */
    public UnresolvedSuperTypeParameterInfo(final String name,
            final UnresolvedTypeInfo superType) {

        super(name);

        if (null == superType) {
            throw new NullPointerException();
        }

        this.superType = superType;
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
