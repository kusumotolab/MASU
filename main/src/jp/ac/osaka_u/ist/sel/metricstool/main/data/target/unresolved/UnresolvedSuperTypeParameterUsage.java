package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


/**
 * <? super A>のような型パラメータの使用を表すクラス
 * 
 * @author y-higo
 * 
 */
public class UnresolvedSuperTypeParameterUsage extends UnresolvedTypeParameterUsage {

    /**
     * 未解決派生クラス参照を与えてオブジェクトを初期化
     * 
     * @param superType 未解決派生クラス参照
     */
    public UnresolvedSuperTypeParameterUsage(final UnresolvedReferenceTypeInfo superType) {

        if (null == superType) {
            throw new NullPointerException();
        }

        this.superType = superType;
    }

    /**
     * 未解決派生クラス参照を返す
     * 
     * @return 未解決派生クラス参照
     */
    public UnresolvedReferenceTypeInfo getSuperType() {
        return this.superType;
    }

    /**
     * 未解決派生クラス参照を保存するための変数
     */
    private UnresolvedReferenceTypeInfo superType;
}
