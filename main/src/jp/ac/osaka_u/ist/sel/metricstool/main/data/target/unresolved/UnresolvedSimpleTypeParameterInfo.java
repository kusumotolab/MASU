package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


/**
 * T, E などの単純な型パラメータを表すクラス
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedSimpleTypeParameterInfo extends UnresolvedTypeParameterInfo {

    /**
     * 型パラメータ名を与えてオブジェクトを初期化する
     * 
     * @param name 型パラメータ名
     */
    public UnresolvedSimpleTypeParameterInfo(final String name) {
        super(name);
    }
}
