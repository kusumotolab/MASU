package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EntityUsageInfo;


/**
 * 未解決変数使用を保存するためのクラス
 * 
 * @author t-miyake, higo
 *
 */
public abstract class UnresolvedVariableUsageInfo extends UnresolvedEntityUsageInfo {

    public UnresolvedVariableUsageInfo(final String usedVariableName, final boolean reference) {
        this.usedVariableName = usedVariableName;
        this.reference = reference;
    }

    /**
     * この変数使用が参照であるかどうかを返す
     * 
     * @return 参照である場合は true，代入である場合は false
     */
    public final boolean isReference() {
        return this.reference;
    }

    /**
     * この変数使用が代入であるかどうかを返す
     * 
     * @return 代入である場合は true，参照である場合は false
     */
    public final boolean isAssignment() {
        return !this.reference;
    }

    /**
     * この未解決変数使用が既に解決されているかどうかを返す
     * 
     * @return 既に解決されている場合は true, そうでない場合は false
     */
    @Override
    public final boolean alreadyResolved() {
        return null != this.resolvedInfo;
    }

    /**
     * 解決済み変数使用を返す
     */
    @Override
    public final EntityUsageInfo getResolvedEntityUsage() {

        if (!this.alreadyResolved()) {
            throw new NotResolvedException();
        }

        return this.resolvedInfo;
    }
    
    /**
     * 使用されている変数の名前を返す
     * @return 使用されている変数の名前
     */
    public String getUsedVariableName() {
        return usedVariableName;
    }

    /**
     * 使用されている変数の名前を保存する変数
     */
    protected final String usedVariableName;
    
    protected boolean reference;

    /**
     * 解決済み変数使用を保存するための変数
     */
    protected EntityUsageInfo resolvedInfo;

    
}
