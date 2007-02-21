package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * T, E などの単純な型パラメータを表すクラス
 * 
 * @author y-higo
 * 
 */
public final class SimpleTypeParameterInfo extends TypeParameterInfo {

    /**
     * 型パラメータ名を与えてオブジェクトを初期化する
     * 
     * @param name 型パラメータ名
     */
    public SimpleTypeParameterInfo(final String name) {
        super(name);
    }
}
