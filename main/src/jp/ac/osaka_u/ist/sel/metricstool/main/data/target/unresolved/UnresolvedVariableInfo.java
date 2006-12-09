package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * Unresolvedな変数の共通な親クラス.
 * <ul>
 * <li>変数名</li>
 * <li>型</li>
 * <li>修飾子</li>
 * 
 * @author y-higo
 * 
 */
public class UnresolvedVariableInfo implements Comparable<UnresolvedVariableInfo> {

    /**
     * 変数の順序を定義するメソッド．変数名（String）に従う．
     * 
     * @return 変数の順序関係
     */
    public int compareTo(final UnresolvedVariableInfo variable) {

        if (null == variable) {
            throw new NullPointerException();
        }

        String variableName = this.getName();
        String correspondVariableName = variable.getName();
        return variableName.compareTo(correspondVariableName);
    }

    /**
     * 変数名を返す
     * 
     * @return 変数名
     */
    public final String getName() {
        return this.name;
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
     * 変数オブジェクトを初期化する．プラグインからアクセスできないように protected．
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
     * 変数名を表す変数
     */
    private final String name;

    /**
     * 変数の型を表す変数
     */
    private final UnresolvedTypeInfo type;

}
