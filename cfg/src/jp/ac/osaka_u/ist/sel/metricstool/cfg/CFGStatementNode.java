package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


/**
 * CFGの文ノードを表すクラス
 * @author t-miyake
 *
 */
public class CFGStatementNode extends CFGNormalNode<SingleStatementInfo> {

    /**
     * 生成するノードに対応する文を与えて初期化
     * @param statement 生成するノードに対応する文
     */
    public CFGStatementNode(final SingleStatementInfo statement) {
        super(statement);
    }
}
