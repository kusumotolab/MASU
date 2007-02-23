package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決型パラメータを表す抽象クラス
 * 
 * @author y-higo
 * 
 */
public class UnresolvedTypeParameterInfo implements UnresolvedTypeInfo {

    /**
     * 型パラメータ名を与えてオブジェクトを初期化する
     * 
     * @param name 型パラメータ名
     * @param extends 未解決基底クラス型
     */
    public UnresolvedTypeParameterInfo(final String name, final UnresolvedTypeInfo extendsType) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == name) {
            throw new NullPointerException();
        }

        this.name = name;
        this.extendsType = extendsType;
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
     * 型名（実際には型パラメータ名）を返す．
     * 
     * @return 型名
     */
    public final String getTypeName() {
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
     * 型パラメータ名を保存するための変数
     */
    private final String name;

    /**
     * 基底クラスを保存するための変数
     */
    private final UnresolvedTypeInfo extendsType;
}
