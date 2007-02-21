package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


public final class UnresolvedSimpleTypeParameterUsage extends UnresolvedTypeParameterUsage {

    /**
     * 未解決クラス参照を与えてオブジェクトを初期化
     * 
     * @param type 未解決クラス参照
     */
    public UnresolvedSimpleTypeParameterUsage(final UnresolvedReferenceTypeInfo type) {

        if (null == type) {
            throw new NullPointerException();
        }

        this.type = type;
    }

    /**
     * 型パラメータの未解決クラス参照を返す
     * 
     * @return 未解決クラス参照
     */
    public UnresolvedReferenceTypeInfo getType() {
        return this.type;
    }

    /**
     * 未解決クラス参照を保存するための変数
     */
    private final UnresolvedReferenceTypeInfo type;
}
