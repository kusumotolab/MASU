package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


public final class UnresolvedSimpleTypeParameterUsage extends UnresolvedTypeParameterUsage {

    /**
     * 未解決型を与えてオブジェクトを初期化
     * 
     * @param type 未解決型
     */
    public UnresolvedSimpleTypeParameterUsage(final UnresolvedTypeInfo type) {

        if (null == type) {
            throw new NullPointerException();
        }

        this.type = type;
    }

    /**
     * 型パラメータの未解決型を返す
     * 
     * @return 未解決型
     */
    public UnresolvedTypeInfo getType() {
        return this.type;
    }

    /**
     * 未解決型を保存するための変数
     */
    private final UnresolvedTypeInfo type;
}
