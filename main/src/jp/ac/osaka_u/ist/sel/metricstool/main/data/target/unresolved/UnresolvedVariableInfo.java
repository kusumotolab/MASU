package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * Unresolvedな変数の共通な親クラス.
 * <ul>
 * <li>変数名</li>
 * <li>型</li>
 * <li>修飾子</li>
 * </ul>
 * 
 * @author y-higo
 * 
 */
public abstract class UnresolvedVariableInfo {

    /**
     * 変数名を返す
     * 
     * @return 変数名
     */
    public final String getName() {
        return this.name;
    }

    /**
     * 変数名をセットする
     * 
     * @param name 変数名
     */
    public final void setName(final String name) {

        if (null == name) {
            throw new NullPointerException();
        }

        this.name = name;
    }

    /**
     * 変数の型を返す
     * 
     * @return 変数の型
     */
    public final UnresolvedTypeInfo getType() {
        return this.type;
    }

    /**
     * 変数の型をセットする
     * 
     * @param type 変数の型
     */
    public final void setType(final UnresolvedTypeInfo type) {

        if (null == type) {
            throw new NullPointerException();
        }

        this.type = type;
    }

    /**
     * 変数オブジェクトを初期化する．
     * 
     * @param name 変数名
     * @param type 変数の型
     */
    protected UnresolvedVariableInfo(final String name, final UnresolvedTypeInfo type) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if ((null == name) || (null == type)) {
            throw new NullPointerException();
        }

        this.name = name;
        this.type = type;
    }

    /**
     * 変数オブジェクトを初期化する．
     */
    protected UnresolvedVariableInfo() {

        MetricsToolSecurityManager.getInstance().checkAccess();
        this.name = null;
        this.type = null;
    }

    /**
     * 変数名を表す変数
     */
    private String name;

    /**
     * 変数の型を表す変数
     */
    private UnresolvedTypeInfo type;

}
