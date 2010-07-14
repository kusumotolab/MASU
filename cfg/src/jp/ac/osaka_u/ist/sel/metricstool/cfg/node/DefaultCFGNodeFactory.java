package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BreakStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ContinueStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ThrowStatementInfo;


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
    private final ConcurrentMap<ExecutableElementInfo, CFGNode<? extends ExecutableElementInfo>> elementToNodeMap;

    /**
     * オブジェクトを生成
     */
    public DefaultCFGNodeFactory() {
        this.elementToNodeMap = new ConcurrentHashMap<ExecutableElementInfo, CFGNode<? extends ExecutableElementInfo>>();
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

        this.elementToNodeMap = new ConcurrentHashMap<ExecutableElementInfo, CFGNode<? extends ExecutableElementInfo>>(
                nodeFactory.elementToNodeMap);
    }

    /**
     * ExecutableElementInfo　から CFG のノーマルノードを生成
     */
    @Override
    public CFGNormalNode<? extends ExecutableElementInfo> makeNormalNode(
            final ExecutableElementInfo element) {
        CFGNormalNode<? extends ExecutableElementInfo> node = (CFGNormalNode<? extends ExecutableElementInfo>) this
                .getNode(element);

        if (null != node) {
            return node;
        }

        if (element instanceof SingleStatementInfo) {
            if (element instanceof ReturnStatementInfo) {
                node = new CFGReturnStatementNode((ReturnStatementInfo) element);
            } else if (element instanceof ThrowStatementInfo) {
                node = new CFGThrowStatementNode((ThrowStatementInfo) element);
            } else if (element instanceof BreakStatementInfo) {
                node = new CFGBreakStatementNode((BreakStatementInfo) element);
            } else if (element instanceof ContinueStatementInfo) {
                node = new CFGContinueStatementNode((ContinueStatementInfo) element);
            } else {
                node = new CFGStatementNode((SingleStatementInfo) element);
            }
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
    @Override
    public CFGControlNode makeControlNode(final ConditionInfo condition) {

        CFGControlNode node = (CFGControlNode) this.getNode(condition);
        if (null != node) {
            return node;
        }

        if (condition instanceof ForeachConditionInfo) {
            node = new CFGForeachControlNode((ForeachConditionInfo) condition);
            this.elementToNodeMap.put(condition, node);
        } else {
            node = new CFGControlNode(condition);
            this.elementToNodeMap.put(condition, node);
        }

        return node;
    }

    @Override
    public CFGNode<? extends ExecutableElementInfo> getNode(final ExecutableElementInfo statement) {
        return this.elementToNodeMap.get(statement);
    }

    @Override
    public boolean removeNode(final ExecutableElementInfo element) {
        return null != this.elementToNodeMap.remove(element) ? true : false;
    }

    @Override
    public Collection<CFGNode<? extends ExecutableElementInfo>> getAllNodes() {
        return Collections.unmodifiableCollection(this.elementToNodeMap.values());
    }
}
