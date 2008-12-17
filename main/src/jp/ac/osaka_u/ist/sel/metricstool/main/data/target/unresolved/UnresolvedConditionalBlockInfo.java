package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決条件式付きブロック文を表すクラス
 * 
 * @author t-miyake, higo
 * @param <T> 解決済みブロックの型
 *
 */
public abstract class UnresolvedConditionalBlockInfo<T extends ConditionalBlockInfo> extends
        UnresolvedBlockInfo<T> {

    /**
     * 外側のブロック情報を与えて，オブジェクトを初期化
     * 
     * @param outerSpace 外側のブロック情報
     */
    public UnresolvedConditionalBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        super(outerSpace);
    }

    /**
     * 未解決条件式を返す
     * @return 未解決条件式
     */
    public final UnresolvedConditionalClauseInfo getConditionalClause() {
        return this.conditionalClause;
    }

    /**
     * 未解決条件式を設定する
     * @param conditionalClause 未解決条件式
     */
    public final void setConditionalClause(
            final UnresolvedConditionalClauseInfo conditionalClause) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if(null == conditionalClause) {
            throw new IllegalArgumentException("conditionalClause is null");
        }
        
        this.conditionalClause = conditionalClause;
    }

    /**
     * 未解決条件式を保存するための変数
     */
    private UnresolvedConditionalClauseInfo conditionalClause;
}
