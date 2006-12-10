package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


/**
 * ローカル変数を表すためのクラス． 
 * 以下の情報を持つ．
 * <ul>
 * <li>変数名</li>
 * <li>未解決型名</li>
 * </ul>
 * @author y-higo
 * 
 */
public final class UnresolvedLocalVariableInfo extends UnresolvedVariableInfo {

    /**
     * ローカル変数ブジェクトを初期化する．
     * 
     * @param name 変数名
     * @param type 未解決型名
     */
    public UnresolvedLocalVariableInfo(final String name, final UnresolvedTypeInfo type) {
        super(name, type);
    }
}

