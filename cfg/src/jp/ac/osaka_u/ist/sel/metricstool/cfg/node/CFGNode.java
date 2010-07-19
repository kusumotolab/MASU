package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGUtility;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BreakStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


/**
 * 制御依存グラフのノードを表すクラス
 * 
 * @author t-miyake
 * 
 * @param <T>
 *            ノードの核となる情報の型
 */
public abstract class CFGNode<T extends ExecutableElementInfo> implements
        Comparable<CFGNode<? extends ExecutableElementInfo>> {

    public static CFGNode<? extends ExecutableElementInfo> getHeadmostNode(
            final CFGNode<? extends ExecutableElementInfo> node) {

        final Set<CFGEdge> edges = node.getBackwardEdges();
        // バックワードエッジがないのであれば，そのノードが最も上にある
        if (0 == edges.size()) {
            return node;
        }

        else {
            final List<CFGNode<? extends ExecutableElementInfo>> headmostNodes = new ArrayList<CFGNode<? extends ExecutableElementInfo>>();
            for (final CFGEdge edge : edges) {
                final CFGNode<? extends ExecutableElementInfo> fromNode = edge.getFromNode();
                final CFGNode<? extends ExecutableElementInfo> headmostNode = getHeadmostNode(fromNode);
                headmostNodes.add(headmostNode);
            }

            // 全てのheadmostNodeが等しいかをチェックする
            for (int i = 0; i < headmostNodes.size(); i++) {
                for (int j = i + 1; j < headmostNodes.size(); j++) {
                    if (headmostNodes.get(i) != headmostNodes.get(j)) {
                        throw new IllegalStateException();
                    }
                }
            }

            return headmostNodes.get(0);
        }
    }

    private static final AtomicLong DUMMY_VARIABLE_NUMBER = new AtomicLong();

    protected static final String getDummyVariableName() {
        final StringBuilder text = new StringBuilder();
        text.append("$");
        text.append(Long.toString(DUMMY_VARIABLE_NUMBER.getAndIncrement()));
        return text.toString();

    }

    /**
     * このノードのフォワードノードのセット
     */
    protected final Set<CFGEdge> forwardEdges;

    /**
     * このノードのバックワードノードのセット
     */
    protected final Set<CFGEdge> backwardEdges;

    private final String text;

    /**
     * このノードに対応する文
     */
    private final T core;

    protected CFGNode(final T core) {

        if (null == core) {
            throw new IllegalArgumentException("core is null");
        }
        this.core = core;
        this.forwardEdges = new HashSet<CFGEdge>();
        this.backwardEdges = new HashSet<CFGEdge>();
        this.text = core.getText() + " <" + core.getFromLine() + ">";
    }

    public void addForwardEdge(final CFGEdge forwardEdge) {

        if (null == forwardEdge) {
            throw new IllegalArgumentException();
        }

        if (!this.equals(forwardEdge.getFromNode())) {
            throw new IllegalArgumentException();
        }

        if (this.forwardEdges.add(forwardEdge)) {
            forwardEdge.getToNode().backwardEdges.add(forwardEdge);
        }
    }

    public void addBackwardEdge(final CFGEdge backwardEdge) {

        if (null == backwardEdge) {
            throw new IllegalArgumentException();
        }

        if (!this.equals(backwardEdge.getToNode())) {
            throw new IllegalArgumentException();
        }

        if (this.backwardEdges.add(backwardEdge)) {
            backwardEdge.getFromNode().forwardEdges.add(backwardEdge);
        }
    }

    void removeForwardEdge(final CFGEdge forwardEdge) {

        if (null == forwardEdge) {
            throw new IllegalArgumentException();
        }

        this.forwardEdges.remove(forwardEdge);
    }

    void removeForwardEdges(final Collection<CFGEdge> forwardEdges) {

        if (null == forwardEdges) {
            throw new IllegalArgumentException();
        }

        this.forwardEdges.removeAll(forwardEdges);
    }

    void removeBackwardEdge(final CFGEdge backwardEdge) {

        if (null == backwardEdge) {
            throw new IllegalArgumentException();
        }

        this.backwardEdges.remove(backwardEdge);
    }

    void removeBackwardEdges(final Collection<CFGEdge> backwardEdges) {

        if (null == backwardEdges) {
            throw new IllegalArgumentException();
        }

        this.backwardEdges.removeAll(backwardEdges);
    }

    /**
     * このノードを前ノードと後ろノードから辿れないようにする
     */
    void remove() {
        for (final CFGEdge edge : this.getBackwardEdges()) {
            final CFGNode<?> backwardNode = edge.getFromNode();
            backwardNode.removeForwardEdge(edge);
        }
        for (final CFGEdge edge : this.getForwardEdges()) {
            final CFGNode<?> forwardNode = edge.getToNode();
            forwardNode.removeBackwardEdge(edge);
        }
    }

    /**
     * このノードが存在する位置に，引数で与えられたノードを追加し，このノードを削除
     * 
     * @param node
     */
    void replace(final CFGNode<?> node) {
        if (null == node) {
            throw new IllegalArgumentException();
        }
        this.remove();
        for (final CFGEdge edge : this.getBackwardEdges()) {
            final CFGNode<?> backwardNode = edge.getFromNode();
            final CFGEdge newEdge = edge.replaceToNode(node);
            backwardNode.addForwardEdge(newEdge);
        }
        for (final CFGEdge edge : this.getForwardEdges()) {
            final CFGNode<?> forwardNode = edge.getToNode();
            final CFGEdge newEdge = edge.replaceFromNode(node);
            forwardNode.addBackwardEdge(newEdge);
        }
    }

    /**
     * このノードに対応する文の情報を取得
     * 
     * @return このノードに対応する文
     */
    public T getCore() {
        return this.core;
    }

    /**
     * このノードのフォワードノードのセットを取得
     * 
     * @return このノードのフォワードノードのセット
     */
    public Set<CFGNode<? extends ExecutableElementInfo>> getForwardNodes() {
        final Set<CFGNode<? extends ExecutableElementInfo>> forwardNodes = new HashSet<CFGNode<? extends ExecutableElementInfo>>();
        for (final CFGEdge forwardEdge : this.getForwardEdges()) {
            forwardNodes.add(forwardEdge.getToNode());
        }
        return Collections.unmodifiableSet(forwardNodes);
    }

    /**
     * このノードのフォワードエッジのセットを取得
     * 
     * @return このノードのフォワードエッジのセット
     */
    public Set<CFGEdge> getForwardEdges() {
        return Collections.unmodifiableSet(this.forwardEdges);
    }

    /**
     * このノードのバックワードノードのセットを取得
     * 
     * @return このノードのバックワードノードのセット
     */
    public Set<CFGNode<? extends ExecutableElementInfo>> getBackwardNodes() {
        final Set<CFGNode<? extends ExecutableElementInfo>> backwardNodes = new HashSet<CFGNode<? extends ExecutableElementInfo>>();
        for (final CFGEdge backwardEdge : this.getBackwardEdges()) {
            backwardNodes.add(backwardEdge.getFromNode());
        }
        return Collections.unmodifiableSet(backwardNodes);
    }

    /**
     * このノードのバックワードエッジのセットを取得
     * 
     * @return このノードのバックワードエッジのセット
     */
    public Set<CFGEdge> getBackwardEdges() {
        return Collections.unmodifiableSet(this.backwardEdges);
    }

    @Override
    public int compareTo(final CFGNode<? extends ExecutableElementInfo> node) {

        if (null == node) {
            throw new IllegalArgumentException();
        }

        final int methodOrder = this.getCore().getOwnerMethod().compareTo(
                node.getCore().getOwnerMethod());
        if (0 != methodOrder) {
            return methodOrder;
        }

        return this.getCore().compareTo(node.getCore());
    }

    /**
     * 必要のないノードの場合は，このメソッドをオーバーライドすることによって，削除される
     * 最適化によりノードが削除されるときはtrue,そうでないときはfalseを返す.
     */
    public boolean optimize() {
        return false;
    }

    /**
     * このノードが引数で与えられたローカル空間の出口のノードであるか否か返す．
     * 
     * @param localSpace
     *            ローカル空間
     * @return 引数のローカル空間の出口の場合，true
     */
    public boolean isExitNode(final LocalSpaceInfo localSpace) {
        if (this.core instanceof ReturnStatementInfo) {
            return true;
        } else if (this.core instanceof BreakStatementInfo) {
            final BreakStatementInfo breakStatement = (BreakStatementInfo) this.core;
            if (localSpace instanceof BlockInfo && ((BlockInfo) localSpace).isLoopStatement()) {
                if (null == breakStatement.getDestinationLabel()) {
                    return true;
                } else {

                }
            }
        }
        return false;
    }

    public final String getText() {
        return this.text;
    }

    /**
     * このノードで定義・変更されている変数のSetを返す
     * 
     * @param countObjectStateChange
     *            呼び出されたメソッドないでのオブジェクトの状態変更
     *            （フィールドへの代入など）を参照されている変数の変更とする場合はtrue．
     * 
     * @return
     */
    public final Set<VariableInfo<? extends UnitInfo>> getDefinedVariables(
            final boolean countObjectStateChange) {
        final Set<VariableInfo<? extends UnitInfo>> assignments = new HashSet<VariableInfo<? extends UnitInfo>>();
        assignments.addAll(VariableUsageInfo.getUsedVariables(VariableUsageInfo.getAssignments(this
                .getCore().getVariableUsages())));

        // オブジェクトの状態変更が，変数の変更とされる場合
        if (countObjectStateChange) {
            for (final CallInfo<? extends CallableUnitInfo> call : this.getCore().getCalls()) {
                if (call instanceof MethodCallInfo) {
                    final MethodCallInfo methodCall = (MethodCallInfo) call;
                    final MethodInfo callee = methodCall.getCallee();

                    // methodCallのquantifierを調査
                    if (CFGUtility.stateChange(callee)) {
                        final ExpressionInfo qualifier = methodCall.getQualifierExpression();
                        if (qualifier instanceof VariableUsageInfo<?>) {
                            assignments.add(((VariableUsageInfo<?>) qualifier).getUsedVariable());
                        }
                    }

                    for (final MethodInfo overrider : callee.getOverriders()) {
                        if (CFGUtility.stateChange(overrider)) {
                            final ExpressionInfo qualifier = methodCall.getQualifierExpression();
                            if (qualifier instanceof VariableUsageInfo<?>) {
                                assignments.add(((VariableUsageInfo<?>) qualifier)
                                        .getUsedVariable());
                            }
                        }
                    }
                }

                if (call instanceof MethodCallInfo || call instanceof ClassConstructorCallInfo) {
                    // methodCallのparameterを調査
                    final List<ExpressionInfo> arguments = call.getArguments();
                    for (int index = 0; index < arguments.size(); index++) {

                        final CallableUnitInfo callee = call.getCallee();
                        if (CFGUtility.stateChange(callee, index)) {
                            final ExpressionInfo argument = call.getArguments().get(index);
                            if (argument instanceof VariableUsageInfo<?>) {
                                assignments
                                        .add(((VariableUsageInfo<?>) argument).getUsedVariable());
                            }
                        }

                        if (callee instanceof MethodInfo) {
                            for (final MethodInfo overrider : ((MethodInfo) callee).getOverriders()) {
                                if (CFGUtility.stateChange(overrider, index)) {
                                    final ExpressionInfo argument = call.getArguments().get(index);
                                    if (argument instanceof VariableUsageInfo<?>) {
                                        assignments.add(((VariableUsageInfo<?>) argument)
                                                .getUsedVariable());
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }

        return assignments;

    }

    /**
     * このノードで利用（参照）されている変数のSetを返す
     * 
     * @return
     */
    public final Set<VariableInfo<? extends UnitInfo>> getReferencedVariables() {
        return VariableUsageInfo.getUsedVariables(VariableUsageInfo.getReferencees(this.getCore()
                .getVariableUsages()));
    }

    /**
     * このノードを分解するメソッド． 分解された場合は，分解後のノード群からなるCFGを返す． 分解が行われなかった場合はnullを返す．
     * 
     * @param nodeFactory
     * @return
     */
    public CFG dissolve(final ICFGNodeFactory nodeFactory) {
        return null;
    }
}
