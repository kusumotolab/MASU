package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * 未解決条件文付きブロック文を表すクラス
 * 
 * @author t-miyake
 *
 */

// TODO ConditionalBlockInfoが追加されたらTはConditionalBlockInfoを継承すべき
public abstract class UnresolvedConditionalBlockInfo<T extends /*Conditional*/BlockInfo> extends UnresolvedBlockInfo<T> {

	/**
	 * 未解決条件文を返す
	 * @return 未解決条件文
	 */
	public UnresolvedConditionalClauseInfo getConditionalSpace() {
		return this.conditionalSpace;
	}

	/**
	 * 未解決条件文をセットする
	 * @param conditionalSpace 未解決条件文
	 */
	public void setConditionalSpace(UnresolvedConditionalClauseInfo conditionalSpace) {
		// 不正な呼び出しでないかをチェック
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == conditionalSpace) {
            throw new IllegalArgumentException("conditionalSpace is null.");
        }
        
		this.conditionalSpace = conditionalSpace;
	}
	
	/**
	 * 未解決条件文を保存するための変数
	 */
	private UnresolvedConditionalClauseInfo conditionalSpace;

	
}
