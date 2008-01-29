package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;


/**
 * 未解決変数使用を保存するためのクラス
 * 
 * @author t-miyake
 *
 */
public abstract class UnresolvedVariableUsageInfo implements UnresolvedEntityUsageInfo {

    /**
     * 使用されている変数の未解決情報を返す
     * @return 使用されているローカル変数の未解決情報
     */
    public UnresolvedVariableInfo getReferencedVariable() {
        return this.referencedVariable;
    }
    
    /**
     * このローカル変数使用が参照であるかどうかを返す
     * 
     * @return 参照である場合は true，代入である場合は false
     */
    public boolean isReference() {
        return this.reference;
    }
    
    /**
     * このローカル変数使用が代入であるかどうかを返す
     * 
     * @return 代入である場合は true，参照である場合は false
     */
    public boolean isAssignment() {
        return !this.reference;
    }
    
    /**
     * この未解決変数使用が既に解決されているかどうかを返す
     * 
     * @return 既に解決されている場合は true, そうでない場合は false
     */
    public boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * 解決済み変数使用を返す
     */
    public EntityUsageInfo getResolvedEntityUsage() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }
    
    protected UnresolvedVariableInfo referencedVariable;
    
    protected boolean reference;
    
    /**
     * 解決済み変数使用を保存するための変数
     */
    protected EntityUsageInfo resolvedInfo;
}
