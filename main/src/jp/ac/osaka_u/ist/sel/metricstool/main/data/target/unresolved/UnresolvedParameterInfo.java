package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


/**
 * 引数を表すためのクラス． 型を提供するのみ．
 * 
 * @author y-higo
 * 
 */
public final class UnresolvedParameterInfo extends UnresolvedVariableInfo {

    /**
     * 引数オブジェクトを初期化する．名前と型が必要．
     * 
     * @param name 引数名
     * @param type 引数の型
     */
    public UnresolvedParameterInfo(final String name, final UnresolvedTypeInfo type) {
        super(name, type);
    }
}
