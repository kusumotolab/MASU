package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EmptyExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;


/**
 * CFGの制御ノードを表すクラス
 * @author t-miyake, higo
 *
 */
public class CFGControlNode extends CFGNode<ConditionInfo> {

    /**
     * このノードのTrueフォワードノードのセット
     */
    private final Set<CFGNode<? extends ExecutableElementInfo>> trueForwardNodes;

    /**
     * このノードのFalseフォワードノードのセット
     */
    private final Set<CFGNode<? extends ExecutableElementInfo>> falseForwardNodes;

    /**
     * 生成するノードに対応する制御文を与えて初期化
     * @param controlStatement 生成するノードに対応する制御文
     */
    CFGControlNode(final ConditionInfo condition) {
        super(condition);
        this.trueForwardNodes = new HashSet<CFGNode<? extends ExecutableElementInfo>>();
        this.falseForwardNodes = new HashSet<CFGNode<? extends ExecutableElementInfo>>();
    }

    /**
     * 条件式がTrueの場合のフォワードノードを追加
     * 
     * @param forwardNode 条件式がTrueの場合のフォワードノード
     */
    void addTrueForwardNode(final CFGNode<? extends ExecutableElementInfo> forwardNode) {

        if (null == forwardNode) {
            throw new IllegalArgumentException();
        }

        this.trueForwardNodes.add(forwardNode);
        forwardNode.backwardNodes.add(this);
    }

    void addTrueForwardNodes(final Set<CFGNode<? extends ExecutableElementInfo>> forwardNodes) {

        if (null == forwardNodes) {
            throw new IllegalArgumentException();
        }

        this.trueForwardNodes.addAll(forwardNodes);
        for (final CFGNode<?> forwardNode : forwardNodes) {
            forwardNode.backwardNodes.add(this);
        }
    }

    /**
     * 条件式がFalseの場合のフォワードノードを追加
     * 
     * @param forwardNode 条件式がFalseの場合のフォワードノード
     */
    void addFalseForwardNode(final CFGNode<? extends ExecutableElementInfo> forwardNode) {

        if (null == forwardNode) {
            throw new IllegalArgumentException();
        }

        this.falseForwardNodes.add(forwardNode);
        forwardNode.backwardNodes.add(this);
    }

    void addFalseForwardNodes(final Set<CFGNode<? extends ExecutableElementInfo>> forwardNodes) {

        if (null == forwardNodes) {
            throw new IllegalArgumentException();
        }

        this.falseForwardNodes.addAll(forwardNodes);
        for (final CFGNode<?> forwardNode : forwardNodes) {
            forwardNode.backwardNodes.add(this);
        }
    }

    /**
     * 通常のフォワードノードとして登録する場合はFalseフォワード
     * 
     */
    @Override
    void addForwardNode(final CFGNode<? extends ExecutableElementInfo> forwardNode) {
        this.addFalseForwardNode(forwardNode);
    }

    @Override
    void removeForwardNode(final CFGNode<? extends ExecutableElementInfo> forwardNode) {

        if (null == forwardNode) {
            throw new IllegalArgumentException();
        }

        if (this.trueForwardNodes.remove(forwardNode)) {
            this.addTrueForwardNodes(forwardNode.getForwardNodes());
        }

        if (this.falseForwardNodes.remove(forwardNode)) {
            this.addFalseForwardNodes(forwardNode.getForwardNodes());
        }
    }

    /**
     * このノードのフォワードノードのセットを取得
     * @return このノードのフォワードノードのセット
     */
    @Override
    public Set<CFGNode<? extends ExecutableElementInfo>> getForwardNodes() {
        final Set<CFGNode<? extends ExecutableElementInfo>> forwardNodes = new HashSet<CFGNode<? extends ExecutableElementInfo>>();
        forwardNodes.addAll(this.trueForwardNodes);
        forwardNodes.addAll(this.falseForwardNodes);
        return Collections.unmodifiableSet(forwardNodes);
    }

    /**
     * このノードのTrueフォワードノードのセットを取得
     * @return このノードのTrueフォワードノードのセット
     */
    public Set<CFGNode<? extends ExecutableElementInfo>> getTrueForwardNodes() {
        return Collections.unmodifiableSet(this.trueForwardNodes);
    }

    /**
     * このノードのFalseフォワードノードのセットを取得
     * @return このノードのFalseフォワードノードのセット
     */
    public Set<CFGNode<? extends ExecutableElementInfo>> getFalseForwardNodes() {
        return Collections.unmodifiableSet(this.falseForwardNodes);
    }

    @Override
    protected void optimize() {

        if (this.getCore() instanceof EmptyExpressionInfo) {

            final Set<CFGNode<?>> forwardNodes = (HashSet<CFGNode<?>>) ((HashSet<CFGNode<?>>) this.forwardNodes)
                    .clone();
            final Set<CFGNode<?>> backwardNodes = (HashSet<CFGNode<?>>) ((HashSet<CFGNode<?>>) this.backwardNodes)
                    .clone();

            for (final CFGNode<?> forwardNode : forwardNodes) {
                forwardNode.removeBackwardNode(this);
            }

            for (final CFGNode<?> backwardNode : backwardNodes) {
                backwardNode.removeForwardNode(this);
            }
        }
    }
}
