package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;


/**
 * CFGの制御ノードを表すクラス
 * @author t-miyake
 *
 */
public class CFGControlNode extends CFGNode<ConditionalBlockInfo> {

    /**
     * 生成するノードに対応する制御文を与えて初期化
     * @param controlStatement 生成するノードに対応する制御文
     */
    CFGControlNode(final ConditionalBlockInfo controlStatement) {
        super(controlStatement);
        this.text = controlStatement.getConditionalClause().getCondition().getText() + "<"
                + controlStatement.getConditionalClause().getCondition().getFromLine() + ">";
    }
}
