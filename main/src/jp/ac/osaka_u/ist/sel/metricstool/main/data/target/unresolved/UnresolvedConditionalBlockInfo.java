package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;


/**
 * 未解決条件文付きブロック文を表すクラス
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
        this.conditionalClause = new UnresolvedConditionalClauseInfo(this);
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
