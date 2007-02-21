package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


public final class UnresolvedExtendsTypeParameterInfo extends UnresolvedTypeParameterInfo {

    /**
     * 型パラメータ名，未解決基底クラス型を与えてオブジェクトを初期化
     * 
     * @param name 型パラメータ名
     * @param extendsType 未解決基底クラス型
     */
    public UnresolvedExtendsTypeParameterInfo(final String name,
            final UnresolvedTypeInfo extendsType) {

        super(name);

        if (null == extendsType) {
            throw new NullPointerException();
        }

        this.extendsType = extendsType;
    }

    /**
     * 未解決基底クラス型を返す
     * 
     * @return 未解決基底クラス型
     */
    public UnresolvedTypeInfo getExtendsType() {
        return this.extendsType;
    }

    /**
     * 基底クラス型を保存する
     */
    private final UnresolvedTypeInfo extendsType;

}
