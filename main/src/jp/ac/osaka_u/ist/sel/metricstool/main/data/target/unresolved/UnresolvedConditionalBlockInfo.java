package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;


/**
 * 未解決条件文付きブロック文を表すクラス
 * 
 * @author t-miyake, higo
 *
 */
public abstract class UnresolvedConditionalBlockInfo<T extends ConditionalBlockInfo> extends
        UnresolvedBlockInfo<T> {

    public UnresolvedConditionalBlockInfo(final UnresolvedLocalSpaceInfo<?> ownerSpace) {
        super(ownerSpace);
        this.conditionalClause = new UnresolvedConditionalClauseInfo();
    }

    /**
     * 未解決条件文を返す
     * @return 未解決条件文
     */
    public UnresolvedConditionalClauseInfo getConditionalClause() {
        return this.conditionalClause;
    }

    /**
     * 未解決条件文を保存するための変数
     */
    private final UnresolvedConditionalClauseInfo conditionalClause;
}
