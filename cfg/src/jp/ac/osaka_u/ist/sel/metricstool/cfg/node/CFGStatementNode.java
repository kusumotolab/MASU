package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;

import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;

/**
 * CFGの文ノードを表すクラス
 * 
 * @author t-miyake
 * 
 */
public abstract class CFGStatementNode<T extends SingleStatementInfo> extends
		CFGNormalNode<T> {

	/**
	 * 生成するノードに対応する文を与えて初期化
	 * 
	 * @param statement
	 *            生成するノードに対応する文
	 */
	CFGStatementNode(final T statement) {
		super(statement);
	}

	@Override
	void replace(
			final List<CFGNode<? extends ExecutableElementInfo>> dissolvedNodeList) {

		final StatementInfo statement = this.getCore();
		final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();

		ownerSpace.removeStatement(statement);
		for (final CFGNode<? extends ExecutableElementInfo> node : dissolvedNodeList) {
			final ExecutableElementInfo core = node.getCore();
			ownerSpace.addStatement((StatementInfo) core);
		}
	}
}
