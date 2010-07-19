package jp.ac.osaka_u.ist.sel.metricstool.pdg.node;


import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGParameterNode;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;


/**
 * 引数を表すPDGノード
 * 
 * @author higo
 *
 */
public class PDGParameterNode extends PDGNormalNode<CFGParameterNode> {

    /**
     * 引数で与えられたparameterからPDGParameterNodeを作成して返す
     * 
     * @param parameter
     * @return
     */
    public static PDGParameterNode getInstance(final ParameterInfo parameter) {

        if (null == parameter) {
            throw new IllegalArgumentException();
        }

        final CFGParameterNode cfgNode = CFGParameterNode.getInstance(parameter);
        return new PDGParameterNode(cfgNode);
    }

    /**
     * cfgノードを与えて初期化．
     * このクラス内の getInstanceメソッドからのみ呼び出される
     * 
     * @param parameter
     */
    private PDGParameterNode(final CFGParameterNode node) {
        super(node);
    }
}
