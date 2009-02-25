package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;


/**
 * CFGのConditionInfoのノードを表すクラス
 * @author higo
 *
 */
public class CFGConditionNode extends CFGNode<ConditionInfo> {

    /**
     * 生成するノードに対応するConditionInfoを与えて初期化
     * @param condition 生成するノードに対応するConditionInfo
     */
    public CFGConditionNode(final ConditionInfo condition) {
        super(condition);
        this.text = condition.getText() + "<" + condition.getFromLine() + ">";
    }
}
