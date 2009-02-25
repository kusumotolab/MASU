package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;


/**
 * PDGを構成するノードを表すクラス
 * 
 * @author t-miyake
 *
 * @param <T> ノードの核となる情報の型
 */
public abstract class PDGNode<T> {

    /**
     * フォワードエッジ（このノードからの依存辺）
     */
    private final Set<PDGEdge> forwardEdges;

    /**
     * バックワードエッジ（このノードへの依存辺）
     */
    private final Set<PDGEdge> backwardEdges;

    /**
     * ノード上で値が定義・変更される変数
     */
    private final Set<VariableInfo<? extends UnitInfo>> definedVariables;

    /**
     * ノードの核となる情報
     */
    private final T core;

    protected String text;
    
    /**
     * ノードの核となる情報を与えて初期化
     * @param core ノードの核となる情報
     */
    protected PDGNode(final T core) {
        if (null == core) {
            throw new IllegalArgumentException("statement is null.");
        }

        this.core = core;
        this.forwardEdges = new HashSet<PDGEdge>();
        this.backwardEdges = new HashSet<PDGEdge>();

        this.definedVariables = this.extractDefinedVariables(core);
    }

    /**
     * ノード上で定義・変更される変数を抽出
     * @param core ノードの核となる情報
     * @return ノード上で定義・変更される変数
     */
    protected abstract Set<VariableInfo<? extends UnitInfo>> extractDefinedVariables(final T core);

    public abstract boolean isDefine(final VariableInfo<? extends UnitInfo> variable);

    public  abstract boolean isReferenace(final VariableInfo<? extends UnitInfo> variable);
    
    /**
     * このノードのフォワードエッジを追加
     * @param forwardEdge このノードのフォワードエッジ
     */
    protected boolean addFowardEdge(PDGEdge forwardEdge) {
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
    protected boolean addBackwardEdge(PDGEdge backwardEdge) {
        return this.backwardEdges.add(backwardEdge);
    }
    
    void removeBackwardEdge(final PDGEdge backwardEdge){
        this.backwardEdges.remove(backwardEdge);
    }
    
    void removeForwardEdge(final PDGEdge forwardEdge) {
        this.forwardEdges.remove(forwardEdge);
    }
    
    

    /**
     * このノードからのデータ依存変を追加
     * @param dependingNode
     */
    public boolean addDataDependingNode(final PDGNode<?> dependingNode) {
        if (null == dependingNode) {
            throw new IllegalArgumentException();
        }

        boolean added = false;
        final DataDependenceEdge dataFlow = new DataDependenceEdge(this, dependingNode);
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
     * このノードで定義・変更されている変数を取得
     * @return このノードで定義・変更されている変数
     */
    public final Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        return Collections.unmodifiableSet(this.definedVariables);
    }

    /**
     * このノードの核となる情報取得
     * @return このノードの核となる情報
     */
    public final T getCore() {
        return this.core;
    }
    
    public String getText() {
        return this.text;
    }
}
