package jp.ac.osaka_u.ist.sel.metricstool.cfg;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;

/**
 * CFGの文ノードを表すクラス
 * @author t-miyake
 *
 */
public class CFGStatementNode extends CFGNode<SingleStatementInfo> {

    /**
     * 生成するノードに対応する文を与えて初期化
     * @param statement 生成するノードに対応する文
     */
    public CFGStatementNode(final SingleStatementInfo statement) {
        super(statement);
    }
    
}
