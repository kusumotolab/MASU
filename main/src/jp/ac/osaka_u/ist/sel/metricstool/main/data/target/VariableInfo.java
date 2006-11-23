package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * フィールド，引数，ローカル変数の共通の親クラス． 以下の情報を持つ．
 * <ul>
 * <li>変数名</li>
 * <li>型</li>
 * <li>修飾子</li>
 * 
 * @author y-higo
 * 
 */
public abstract class VariableInfo implements Comparable<VariableInfo> {

    /**
     * 変数の順序を定義するメソッド．変数名（String）に従う．
     * 
     * @return 変数の順序関係
     */
    public int compareTo(final VariableInfo variable) {
        
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
    public final TypeInfo getType() {
        return this.type;
    }

    /**
     * 変数オブジェクトを初期化する．プラグインからアクセスできないように protected．
     * 
     * @param name 変数名
     * @param type 変数の型
     */
    protected VariableInfo(final String name, final TypeInfo type) {
        
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
    private final TypeInfo type;

}
