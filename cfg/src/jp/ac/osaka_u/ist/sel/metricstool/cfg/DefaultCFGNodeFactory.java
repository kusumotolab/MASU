package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.HashMap;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;


/**
 * デフォルトのCFGノードファクトリ
 * このファクトリで生成されたノードはマップで管理される．
 * @author t-miyake
 *
 */
public class DefaultCFGNodeFactory implements ICFGNodeFactory {

    /**
     * 生成されたノードを管理するマップ
     */
    private final Map<ExecutableElementInfo, CFGNode<? extends ExecutableElementInfo>> elementToNodeMap;

    /**
     * オブジェクトを生成
     */
    public DefaultCFGNodeFactory() {
        this.elementToNodeMap = new HashMap<ExecutableElementInfo, CFGNode<? extends ExecutableElementInfo>>();
    }

    /**
     * 既存のノードファクトリを与えて初期化．
     * このコンストラクタで生成されるオブジェクトは既存のファクトリのノード構築情報を継承する．
     * @param nodeFactory 既存のノードファクトリ
     */
    public DefaultCFGNodeFactory(final DefaultCFGNodeFactory nodeFactory) {
        if (null == nodeFactory) {
            throw new NullPointerException("nodeFactory is null");
        }

        this.elementToNodeMap = new HashMap<ExecutableElementInfo, CFGNode<? extends ExecutableElementInfo>>(
                nodeFactory.elementToNodeMap);
    }

    /**
     * ExecutableElementInfo　から CFG のノーマルノードを生成
     */
    public CFGNormalNode<? extends ExecutableElementInfo> makeNormalNode(
            final ExecutableElementInfo element) {
        CFGNormalNode<? extends ExecutableElementInfo> node = (CFGNormalNode<? extends ExecutableElementInfo>) this
                .getNode(element);

        if (null != node) {
            return node;
        }

        if (element instanceof SingleStatementInfo) {
            node = new CFGStatementNode((SingleStatementInfo) element);
        } else if (element instanceof CaseEntryInfo) {
            node = new CFGCaseEntryNode((CaseEntryInfo) element);
        } else if (element instanceof ConditionInfo) {
            node = new CFGExpressionNode((ConditionInfo) element);
        } else {
            node = new CFGEmptyNode(element);
        }
        this.elementToNodeMap.put(element, node);

        return node;
    }

    /**
     * ConditionInfo から CFG のコントロールノードを生成
     */
    public CFGControlNode makeControlNode(final ConditionInfo condition) {

        CFGControlNode node = (CFGControlNode) this.getNode(condition);
        if (null != node) {
            return node;
        }

        node = new CFGControlNode(condition);
        this.elementToNodeMap.put(condition, node);

        return node;
    }

    public CFGNode<? extends ExecutableElementInfo> getNode(final ExecutableElementInfo statement) {
        return this.elementToNodeMap.get(statement);
    }
}
