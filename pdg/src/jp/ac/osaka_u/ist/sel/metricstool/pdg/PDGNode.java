package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGNormalNode;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;


/**
 * PDGを構成するノードを表すクラス
 * 
 * @author t-miyake, higo
 *
 * @param <T> ノードの核となる情報の型
 */
public abstract class PDGNode<T extends ExecutableElementInfo> {

    /**
     * CFGノードからPDGノードを生成するメソッド
     * 
     * @param cfgNode
     * @return
     */
    public static PDGNode<?> generate(final CFGNode<?> cfgNode) {

        final ExecutableElementInfo element = cfgNode.getCore();
        if (cfgNode instanceof CFGControlNode) {
            return new PDGControlNode((ConditionInfo) element);

        } else if (cfgNode instanceof CFGNormalNode<?>) {

            if (element instanceof SingleStatementInfo) {
                return new PDGStatementNode((SingleStatementInfo) element);
            } else if (element instanceof ConditionInfo) {
                return new PDGExpressionNode((ConditionInfo) element);
            } else {
                throw new IllegalStateException();
            }

        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * フォワードエッジ（このノードからの依存辺）
     */
    private final Set<PDGEdge> forwardEdges;

    /**
     * バックワードエッジ（このノードへの依存辺）
     */
    private final Set<PDGEdge> backwardEdges;

    /**
     * ノードの核となる情報
     */
    protected T core;

    protected String text;

    /**
     * ノードの核となる情報を与えて初期化
     * @param core ノードの核となる情報
     */
    protected PDGNode() {
        this.forwardEdges = new HashSet<PDGEdge>();
        this.backwardEdges = new HashSet<PDGEdge>();
    }

    /**
     * このノードにて，変更または定義される変数のSet
     * 
     * @return
     */
    public abstract Set<VariableInfo<? extends UnitInfo>> getDefinedVariables();

    /**
     * このノードにて，参照されている変数のSet
     * 
     * @return
     */
    public abstract Set<VariableInfo<? extends UnitInfo>> getReferencedVariables();

    /**
     * 引数で与えられた変数がこのノードで定義されているかどうかを返す
     * 
     * @param variable
     * @return
     */
    public final boolean isDefine(final VariableInfo<? extends UnitInfo> variable) {
        return this.getDefinedVariables().contains(variable);
    }

    /**
     * 引数で与えられた変数がこのノードで参照されているかを返す
     * 
     * @param variable
     * @return
     */
    public final boolean isReferenace(final VariableInfo<? extends UnitInfo> variable) {
        return this.getReferencedVariables().contains(variable);
    }

    /**
     * このノードのフォワードエッジを追加
     * @param forwardEdge このノードのフォワードエッジ
     */
    protected final boolean addFowardEdge(final PDGEdge forwardEdge) {
        if (null == forwardEdge) {
            throw new IllegalArgumentException("forwardNode is null.");
        }

        if (!forwardEdge.getFromNode().equals(this)) {
            throw new IllegalArgumentException();
        }

        return this.forwardEdges.add(forwardEdge);
    }

    /**
     * このノードのバックワードエッジを追加
     * @param backwardEdge
     */
    protected final boolean addBackwardEdge(final PDGEdge backwardEdge) {
        if (null == backwardEdge) {
            throw new IllegalArgumentException("backwardEdge is null.");
        }

        if (!(backwardEdge.getToNode().equals(this))) {
            throw new IllegalArgumentException();
        }

        return this.backwardEdges.add(backwardEdge);
    }

    final void removeBackwardEdge(final PDGEdge backwardEdge) {
        this.backwardEdges.remove(backwardEdge);
    }

    final void removeForwardEdge(final PDGEdge forwardEdge) {
        this.forwardEdges.remove(forwardEdge);
    }

    /**
     * このノードからのデータ依存辺を追加
     * @param dependingNode
     */
    public boolean addDataDependingNode(final PDGNode<?> dependingNode, final VariableInfo<?> data) {
        if (null == dependingNode) {
            throw new IllegalArgumentException();
        }

        boolean added = false;
        final PDGDataDependenceEdge dataFlow = new PDGDataDependenceEdge(this, dependingNode, data);
        added = this.addFowardEdge(dataFlow);
        added &= dependingNode.addBackwardEdge(dataFlow);
        return added;
    }

    /**
     * このノードのバックワードエッジを取得
     * @return このノードのバックワードエッジ
     */
    public final Set<PDGEdge> getBackwardEdges() {
        return Collections.unmodifiableSet(this.backwardEdges);
    }

    /**
     * このノードのフォワードエッジを取得
     * @return このノードのフォワードエッジ
     */
    public final Set<PDGEdge> getForwardEdges() {
        return Collections.unmodifiableSet(this.forwardEdges);
    }

    /**
     * このノードの核となる情報取得
     * @return このノードの核となる情報
     */
    public final T getCore() {
        return this.core;
    }

    public final String getText() {
        return this.text;
    }
}
