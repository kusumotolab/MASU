package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.HashMap;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;


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
    private final Map<StatementInfo, CFGNode<? extends StatementInfo>> statementToNodeMap;

    /**
     * オブジェクトを生成
     */
    public DefaultCFGNodeFactory() {
        this.statementToNodeMap = new HashMap<StatementInfo, CFGNode<? extends StatementInfo>>();
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

        this.statementToNodeMap = new HashMap<StatementInfo, CFGNode<? extends StatementInfo>>(
                nodeFactory.statementToNodeMap);
    }

    public CFGNode<? extends StatementInfo> makeNode(StatementInfo statement) {
        CFGNode<? extends StatementInfo> node = this.getNode(statement);

        if (null != node) {
            return node;
        }

        if (statement instanceof SingleStatementInfo) {
            node = new CFGStatementNode((SingleStatementInfo) statement);
            this.statementToNodeMap.put(statement, node);
        } else if (statement instanceof ConditionalBlockInfo) {
            node = new CFGControlNode((ConditionalBlockInfo) statement);
            this.statementToNodeMap.put(statement, node);
        } else {
            node = new CFGEmptyNode(statement);
        }

        return node;
    }

    public CFGNode<? extends StatementInfo> getNode(final StatementInfo statement) {
        return this.statementToNodeMap.get(statement);
    }
}
