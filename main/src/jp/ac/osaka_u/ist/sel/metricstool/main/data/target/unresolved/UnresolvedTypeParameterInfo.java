package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決型パラメータを表す抽象クラス
 * 
 * @author y-higo
 * 
 */
public abstract class UnresolvedTypeParameterInfo implements UnresolvedTypeInfo {

    /**
     * 型パラメータ名を与えてオブジェクトを初期化する
     * 
     * @param name 型パラメータ名
     */
    public UnresolvedTypeParameterInfo(final String name) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == name) {
            throw new NullPointerException();
        }
        
        this.name = name;
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
     * 型パラメータ名を保存するための変数
     */
    private final String name;
}
