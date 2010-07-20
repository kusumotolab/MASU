package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGUtility;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.SimpleCFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGControlEdge;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGNormalEdge;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayElementUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BinominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CastUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassConstructorCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalClauseInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EmptyExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LiteralUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MonominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NullUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParenthesesExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TernaryOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;


/**
 * 制御依存グラフのノードを表すクラス
 * 
 * @author t-miyake, higo
 * 
 * @param <T>
 *            ノードの核となる情報の型
 */
public abstract class CFGNode<T extends ExecutableElementInfo> implements
        Comparable<CFGNode<? extends ExecutableElementInfo>> {

    /**
     * 与えられた引数からたどることができる頂点のうち，最も先頭（メソッドの入口）にある頂点を返す
     * 
     * @param node
     * @return
     */
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

    /**
     * 頂点を分解するときに用いる変数の名前を生成するためのメソッド
     * @return
     */
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

    /**
     * この頂点のテキスト表現
     */
    private final String text;

    /**
     * このノードに対応する文
     */
    private final T core;

    /**
     * 核となるプログラム要素を与えてCFGを初期化
     * 
     * @param core
     */
    protected CFGNode(final T core) {

        if (null == core) {
            throw new IllegalArgumentException("core is null");
        }
        this.core = core;
        this.forwardEdges = new HashSet<CFGEdge>();
        this.backwardEdges = new HashSet<CFGEdge>();

        final StringBuilder text = new StringBuilder();
        text.append(core.getText());
        text.append(" <");
        text.append(core.getFromLine());
        text.append("> ");
        this.text = text.toString();
    }

    /**
     * 引数で与えられた辺をこの頂点のフォワードエッジとして追加する
     * 
     * @param forwardEdge
     */
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

    /**
     * 引数で与えられた辺をこの頂点のバックワードエッジとして追加する
     * 
     * @param backwardEdge
     */
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

    /**
     * 引数で与えられた辺をフォワードエッジから取り除く
     * 
     * @param forwardEdge
     */
    void removeForwardEdge(final CFGEdge forwardEdge) {

        if (null == forwardEdge) {
            throw new IllegalArgumentException();
        }

        this.forwardEdges.remove(forwardEdge);
    }

    /**
     * 引数で与えられた辺群をフォワードエッジから取り除く
     * 
     * @param forwardEdge
     */
    void removeForwardEdges(final Collection<CFGEdge> forwardEdges) {

        if (null == forwardEdges) {
            throw new IllegalArgumentException();
        }

        this.forwardEdges.removeAll(forwardEdges);
    }

    /**
     * 引数で与えられた辺をバックワードエッジから取り除く
     * 
     * @param backwardEdge
     */
    void removeBackwardEdge(final CFGEdge backwardEdge) {

        if (null == backwardEdge) {
            throw new IllegalArgumentException();
        }

        this.backwardEdges.remove(backwardEdge);
    }

    /**
     * 引数で与えられた辺群をバックワードエッジから取り除く
     * 
     * @param backwardEdges
     */
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
     * この頂点のテキスト表現を返す
     * 
     * @return
     */
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
     * 分解対象となりうるExpressionInfoを返す．
     * 
     * @return
     */
    abstract ExpressionInfo getDissolvingTarget();

    /**
     * 分解したノードを使って再構築を行う．
     * 
     * @param requiredExpressions
     * @return
     */
    abstract T makeNewElement(LocalSpaceInfo ownerSpace, ExpressionInfo... requiredExpressions);

    /**
     * このノードが表す要素をmasuが構築したデータから取り除き，分解したノードの核にある情報を追加する．
     */
    abstract void replace(List<CFGNode<? extends ExecutableElementInfo>> dissolvedNodeList);

    /**
     * このノードを分解するメソッド． 分解された場合は，分解後のノード群からなるCFGを返す． 分解が行われなかった場合はnullを返す．
     * 
     * @param nodeFactory
     * @return
     */
    public CFG dissolve(final ICFGNodeFactory nodeFactory) {

        final ExpressionInfo dissolvingTarget = this.getDissolvingTarget();

        // 分解対象がない場合は何もしないで抜ける
        if (null == dissolvingTarget) {
            return null;
        }

        if (dissolvingTarget instanceof ArrayElementUsageInfo) {

            return this.dissolveArrayElementUsage((ArrayElementUsageInfo) dissolvingTarget,
                    nodeFactory);

        } else if (dissolvingTarget instanceof ArrayInitializerInfo) {

            return this.dissolveArrayInitializer((ArrayInitializerInfo) dissolvingTarget,
                    nodeFactory);

        } else if (dissolvingTarget instanceof ArrayTypeReferenceInfo) {

            return null;

        } else if (dissolvingTarget instanceof BinominalOperationInfo) {

            return this.dissolveBinominalOperationInfo((BinominalOperationInfo) dissolvingTarget,
                    nodeFactory);

        } else if (dissolvingTarget instanceof CallInfo<?>) {

            return this.dissolveCall((CallInfo<?>) dissolvingTarget, nodeFactory);

        } else if (dissolvingTarget instanceof CastUsageInfo) {

            return this.dissolveCastUsage((CastUsageInfo) dissolvingTarget, nodeFactory);

        } else if (dissolvingTarget instanceof ClassReferenceInfo) {

            return null;

        } else if (dissolvingTarget instanceof EmptyExpressionInfo) {

            return null;

        } else if (dissolvingTarget instanceof ForeachConditionInfo) {

            return null;

        } else if (dissolvingTarget instanceof LiteralUsageInfo) {

            return null;

        } else if (dissolvingTarget instanceof MonominalOperationInfo) {

            // 単項演算子のオペランドは変数使用しかこないはずなので，処理する必要がないはず
            return null;

        } else if (dissolvingTarget instanceof NullUsageInfo) {

            return null;

        } else if (dissolvingTarget instanceof ParenthesesExpressionInfo) {

            return this.dissolveParenthesesExpression((ParenthesesExpressionInfo) dissolvingTarget,
                    nodeFactory);

        } else if (dissolvingTarget instanceof TernaryOperationInfo) {

            return this.dissolveTernaryOperation((TernaryOperationInfo) dissolvingTarget,
                    nodeFactory);

        } else if (dissolvingTarget instanceof UnknownEntityUsageInfo) {

            return null;

        } else if (dissolvingTarget instanceof VariableUsageInfo<?>) {

            return null;

        } else {
            throw new IllegalStateException("unknown expression type.");
        }
    }

    /**
     * 右辺がArrayElementUsageである代入文を分解するためのメソッド
     * 
     * @param arrayElementUsage
     * @param nodeFactory
     * @return
     */
    private CFG dissolveArrayElementUsage(final ArrayElementUsageInfo arrayElementUsage,
            final ICFGNodeFactory nodeFactory) {

        final T core = this.getCore();
        final ExpressionInfo indexExpression = arrayElementUsage.getIndexExpression();
        final ExpressionInfo qualifiedExpression = arrayElementUsage.getQualifierExpression();

        final boolean indexExpressionIsDissolved = CFGUtility.isDissolved(indexExpression);
        final boolean qualifiedExpressionIsDissolved = CFGUtility.isDissolved(qualifiedExpression);

        // indexExpressionとqualifiedExpressionが分解されないときは何もせずに抜ける
        if (!indexExpressionIsDissolved && !qualifiedExpressionIsDissolved) {
            return null;
        }

        // 分解前の文から必要な情報を取得
        final LocalSpaceInfo ownerSpace = arrayElementUsage.getOwnerSpace();
        final int fromLine = arrayElementUsage.getFromLine();
        final int toLine = arrayElementUsage.getToLine();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
        final LinkedList<LocalVariableUsageInfo> dissolvedVariableUsageList = new LinkedList<LocalVariableUsageInfo>();

        if (indexExpressionIsDissolved) {
            this.makeDissolvedNode(indexExpression, nodeFactory, dissolvedNodeList,
                    dissolvedVariableUsageList);
        }

        if (qualifiedExpressionIsDissolved) {
            this.makeDissolvedNode(qualifiedExpression, nodeFactory, dissolvedNodeList,
                    dissolvedVariableUsageList);
        }

        // 古いノードを削除
        nodeFactory.removeNode(core);
        this.remove();

        // ダミー変数を利用したArrayElementUsageInfo，およびそれを用いた新しいプログラム要素を作成
        int index = 0;
        final ArrayElementUsageInfo newArrayElementUsage = new ArrayElementUsageInfo(
                indexExpressionIsDissolved ? dissolvedVariableUsageList.get(index++)
                        : indexExpression,
                qualifiedExpressionIsDissolved ? dissolvedVariableUsageList.get(index++)
                        : qualifiedExpression, outerCallableUnit, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final ExecutableElementInfo newElement = this.makeNewElement(ownerSpace,
                newArrayElementUsage);
        final CFGNode<?> newNode = nodeFactory.makeNormalNode(newElement);
        dissolvedNodeList.add(newNode);

        // 分解したノードをエッジでつなぐ
        this.makeEdges(dissolvedNodeList);

        // ownerSpaceとの調整
        this.replace(dissolvedNodeList);

        // 分解したノード群からCFGを構築
        final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

        return newCFG;
    }

    /**
    * 右辺がArrayInitializerInfoである代入文を分解するためのメソッド
    * 
    * @param arrayInitialier
    * @param nodeFactory
    * @return
    */
    private CFG dissolveArrayInitializer(final ArrayInitializerInfo arrayInitializer,
            final ICFGNodeFactory nodeFactory) {

        final T core = this.getCore();
        final List<ExpressionInfo> initializers = arrayInitializer.getElementInitializers();

        final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
        final LinkedList<LocalVariableUsageInfo> dissolvedVariableUsageList = new LinkedList<LocalVariableUsageInfo>();

        //各イニシャライザを分解すべきかチェックし，分解し，分解した文から新規ノードを作成
        final List<ExpressionInfo> newInitializers = new LinkedList<ExpressionInfo>();
        for (final ExpressionInfo initializer : initializers) {

            if (CFGUtility.isDissolved(initializer)) {

                this.makeDissolvedNode(initializer, nodeFactory, dissolvedNodeList,
                        dissolvedVariableUsageList);

                newInitializers.add(dissolvedVariableUsageList.getLast());
            }

            else {
                newInitializers.add(initializer);
            }
        }

        // 分解されたイニシャライザがなければ何もせずに抜ける
        if (dissolvedNodeList.isEmpty()) {
            return null;
        }

        // 古いノードを削除
        nodeFactory.removeNode(core);
        this.remove();

        // 分解前の文から必要な情報を取得
        final LocalSpaceInfo ownerSpace = core.getOwnerSpace();
        final int fromLine = core.getFromLine();
        final int toLine = core.getToLine();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        final ArrayInitializerInfo newArrayInitializer = new ArrayInitializerInfo(newInitializers,
                outerCallableUnit, fromLine, CFGUtility.getRandomNaturalValue(), toLine, CFGUtility
                        .getRandomNaturalValue());
        final ExecutableElementInfo newElement = this.makeNewElement(ownerSpace,
                newArrayInitializer);
        final CFGNode<?> newNode = nodeFactory.makeNormalNode(newElement);
        dissolvedNodeList.add(newNode);

        // 分解したノードをエッジでつなぐ
        this.makeEdges(dissolvedNodeList);

        // ownerSpaceとの調整
        this.replace(dissolvedNodeList);

        // 分解したノード群からCFGを構築
        final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

        return newCFG;
    }

    /**
     * 右辺が BinominalOperationである代入文を分解するためのメソッド
     * 
     * @param binominalOperation
     * @param nodeFactory
     * @return
     */
    private CFG dissolveBinominalOperationInfo(final BinominalOperationInfo binominalOperation,
            final ICFGNodeFactory nodeFactory) {

        final T core = this.getCore();
        final ExpressionInfo firstOperand = binominalOperation.getFirstOperand();
        final ExpressionInfo secondOperand = binominalOperation.getFirstOperand();

        final boolean firstOperandIsDissolved = CFGUtility.isDissolved(firstOperand);
        final boolean secondOperandIsDissolved = CFGUtility.isDissolved(secondOperand);

        // 分解の必要のない場合は抜ける
        if (!firstOperandIsDissolved && !secondOperandIsDissolved) {
            return null;
        }

        // 分解前の文から必要な情報を取得
        final LocalSpaceInfo ownerSpace = core.getOwnerSpace();
        final int fromLine = core.getFromLine();
        final int toLine = core.getToLine();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
        final LinkedList<LocalVariableUsageInfo> dissolvedVariableUsageList = new LinkedList<LocalVariableUsageInfo>();

        // firstOperandが必要であれば分解
        if (firstOperandIsDissolved) {
            this.makeDissolvedNode(firstOperand, nodeFactory, dissolvedNodeList,
                    dissolvedVariableUsageList);
        }

        // secondOperandが必要であれば分解
        if (secondOperandIsDissolved) {
            this.makeDissolvedNode(secondOperand, nodeFactory, dissolvedNodeList,
                    dissolvedVariableUsageList);
        }

        // 古いノードを削除
        nodeFactory.removeNode(core);
        this.remove();

        // 新しい二項演算オブジェクトおよび新しいプログラム要素を生成
        int index = 0;
        final BinominalOperationInfo newBinominalOperation = new BinominalOperationInfo(
                binominalOperation.getOperator(),
                firstOperandIsDissolved ? dissolvedVariableUsageList.get(index++) : firstOperand,
                secondOperandIsDissolved ? dissolvedVariableUsageList.get(index++) : secondOperand,
                outerCallableUnit, fromLine, CFGUtility.getRandomNaturalValue(), toLine, CFGUtility
                        .getRandomNaturalValue());
        final ExecutableElementInfo newElement = this.makeNewElement(ownerSpace,
                newBinominalOperation);
        final CFGNode<?> newNode = nodeFactory.makeNormalNode(newElement);
        dissolvedNodeList.add(newNode);

        // 分解したノードをエッジでつなぐ
        this.makeEdges(dissolvedNodeList);

        // ownerSpaceとの調整
        this.replace(dissolvedNodeList);

        // 分解したノード群からCFGを構築
        final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

        return newCFG;
    }

    /**
     * 右辺が CallInfo<?>である代入文を分解するためのメソッド
     * 
     * @param call
     * @param nodeFactory
     * @return
     */
    private CFG dissolveCall(final CallInfo<? extends CallableUnitInfo> call,
            final ICFGNodeFactory nodeFactory) {

        final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
        final LinkedList<LocalVariableUsageInfo> dissolvedVariableUsageList = new LinkedList<LocalVariableUsageInfo>();

        //　引数を分解
        final List<ExpressionInfo> newArguments = new ArrayList<ExpressionInfo>();
        for (final ExpressionInfo argument : call.getArguments()) {
            if (CFGUtility.isDissolved(argument)) {
                this.makeDissolvedNode(argument, nodeFactory, dissolvedNodeList,
                        dissolvedVariableUsageList);
                newArguments.add(dissolvedVariableUsageList.getLast());
            } else {
                newArguments.add(argument);
            }
        }

        // メソッド呼び出しであれば，qualifiedExpressionを分解
        final ExpressionInfo newQualifiedExpression;
        if (call instanceof MethodCallInfo) {

            final MethodCallInfo methodCall = (MethodCallInfo) call;
            final ExpressionInfo qualifiedExpression = methodCall.getQualifierExpression();
            if (CFGUtility.isDissolved(qualifiedExpression)) {
                this.makeDissolvedNode(qualifiedExpression, nodeFactory, dissolvedNodeList,
                        dissolvedVariableUsageList);
                newQualifiedExpression = dissolvedVariableUsageList.getLast();
            } else {
                newQualifiedExpression = qualifiedExpression;
            }
        } else {
            newQualifiedExpression = null;
        }

        // 配列コンストラクタであれば，indexExpressionを分解
        final SortedMap<Integer, ExpressionInfo> newIndexExpressions = new TreeMap<Integer, ExpressionInfo>();
        if (call instanceof ArrayConstructorCallInfo) {

            final ArrayConstructorCallInfo arrayConstructorCall = (ArrayConstructorCallInfo) call;
            for (final Entry<Integer, ExpressionInfo> entry : arrayConstructorCall
                    .getIndexExpressions().entrySet()) {

                final Integer dimension = entry.getKey();
                final ExpressionInfo indexExpression = entry.getValue();

                if (CFGUtility.isDissolved(indexExpression)) {
                    this.makeDissolvedNode(indexExpression, nodeFactory, dissolvedNodeList,
                            dissolvedVariableUsageList);
                    newIndexExpressions.put(dimension, dissolvedVariableUsageList.getLast());
                } else {
                    newIndexExpressions.put(dimension, indexExpression);
                }
            }
        }

        // 分解が行われなかった場合は何もせずに抜ける
        if (dissolvedNodeList.isEmpty()) {
            return null;
        }

        // 分解前の文から必要な情報を取得
        final T core = this.getCore();
        final LocalSpaceInfo ownerSpace = core.getOwnerSpace();
        final int fromLine = core.getFromLine();
        final int toLine = core.getToLine();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        // 古いノードを削除
        nodeFactory.removeNode(core);
        this.remove();

        final CallInfo<? extends CallableUnitInfo> newCall;
        if (call instanceof MethodCallInfo) {
            final MethodCallInfo methodCall = (MethodCallInfo) call;
            newCall = new MethodCallInfo(newQualifiedExpression.getType(), newQualifiedExpression,
                    methodCall.getCallee(), outerCallableUnit, fromLine, CFGUtility
                            .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        } else if (call instanceof ClassConstructorCallInfo) {
            final ClassConstructorCallInfo classConstructorCall = (ClassConstructorCallInfo) call;
            newCall = new ClassConstructorCallInfo(classConstructorCall.getType(),
                    classConstructorCall.getCallee(), outerCallableUnit, fromLine, CFGUtility
                            .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        } else if (call instanceof ArrayConstructorCallInfo) {
            final ArrayConstructorCallInfo arrayConstructorCall = (ArrayConstructorCallInfo) call;
            newCall = new ArrayConstructorCallInfo(arrayConstructorCall.getType(),
                    outerCallableUnit, fromLine, CFGUtility.getRandomNaturalValue(), toLine,
                    CFGUtility.getRandomNaturalValue());

            for (final Entry<Integer, ExpressionInfo> entry : newIndexExpressions.entrySet()) {
                final Integer dimension = entry.getKey();
                final ExpressionInfo newIndexExpression = entry.getValue();
                ((ArrayConstructorCallInfo) newCall).addIndexExpression(dimension,
                        newIndexExpression);
            }

        } else {
            throw new IllegalStateException();
        }

        // 引数を追加
        for (final ExpressionInfo newArgument : newArguments) {
            newCall.addArgument(newArgument);
        }

        final ExecutableElementInfo newElement = this.makeNewElement(ownerSpace, newCall);
        final CFGNode<?> newNode = nodeFactory.makeNormalNode(newElement);
        dissolvedNodeList.add(newNode);

        // 分解したノードをエッジでつなぐ
        this.makeEdges(dissolvedNodeList);

        // ownerSpaceとの調整
        this.replace(dissolvedNodeList);

        // 分解したノード群からCFGを構築
        final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

        return newCFG;
    }

    /**
     * 右辺が CastUsageInfoである代入文を分解するためのメソッド
     * 
     * @param castUsage
     * @param nodeFactory
     * @return
     */
    private CFG dissolveCastUsage(final CastUsageInfo castUsage, final ICFGNodeFactory nodeFactory) {

        final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
        final LinkedList<LocalVariableUsageInfo> dissolvedVariableUsageList = new LinkedList<LocalVariableUsageInfo>();

        final ExpressionInfo castedUsage = castUsage.getCastedUsage();
        if (CFGUtility.isDissolved(castedUsage)) {
            this.makeDissolvedNode(castedUsage, nodeFactory, dissolvedNodeList,
                    dissolvedVariableUsageList);
        }

        if (dissolvedNodeList.isEmpty()) {
            return null;
        }

        // 分解前の文から必要な情報を取得
        final T core = this.getCore();
        final LocalSpaceInfo ownerSpace = core.getOwnerSpace();
        final int fromLine = core.getFromLine();
        final int toLine = core.getToLine();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        // 古いノードを削除
        nodeFactory.removeNode(core);
        this.remove();

        // 新しい二項演算オブジェクトおよびそれを右辺として持つ代入文を作成
        final CastUsageInfo newCastUsage = new CastUsageInfo(castUsage.getType(),
                dissolvedVariableUsageList.getFirst(), outerCallableUnit, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final ExecutableElementInfo newElement = this.makeNewElement(ownerSpace, newCastUsage);
        final CFGNode<?> newNode = nodeFactory.makeNormalNode(newElement);
        dissolvedNodeList.add(newNode);

        // 分解したノードをエッジでつなぐ
        this.makeEdges(dissolvedNodeList);

        // ownerSpaceとの調整
        this.replace(dissolvedNodeList);

        // 分解したノード群からCFGを構築
        final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

        return newCFG;
    }

    /**
     * 右辺がParenthesesExpressionInfoである代入文を分解するためのメソッド
     * 
     * @param parenthesExpression
     * @param nodeFactory
     * @return
     */
    private CFG dissolveParenthesesExpression(
            final ParenthesesExpressionInfo parenthesesExpression, final ICFGNodeFactory nodeFactory) {

        final LinkedList<CFGNode<?>> dissolvedNodeList = new LinkedList<CFGNode<?>>();
        final LinkedList<LocalVariableUsageInfo> dissolvedVariableUsageList = new LinkedList<LocalVariableUsageInfo>();

        final ExpressionInfo innerExpression = parenthesesExpression.getParnentheticExpression();
        if (CFGUtility.isDissolved(innerExpression)) {
            this.makeDissolvedNode(innerExpression, nodeFactory, dissolvedNodeList,
                    dissolvedVariableUsageList);
        }

        if (dissolvedNodeList.isEmpty()) {
            return null;
        }

        // 分解前の文から必要な情報を取得
        final T core = this.getCore();
        final LocalSpaceInfo ownerSpace = core.getOwnerSpace();
        final int fromLine = core.getFromLine();
        final int toLine = core.getToLine();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        // 古いノードを削除
        nodeFactory.removeNode(core);
        this.remove();

        // 新しい二項演算オブジェクトおよびそれを右辺として持つ代入文を作成
        final ParenthesesExpressionInfo newInnerExpression = new ParenthesesExpressionInfo(
                dissolvedVariableUsageList.getFirst(), outerCallableUnit, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final ExecutableElementInfo newElement = this
                .makeNewElement(ownerSpace, newInnerExpression);
        final CFGNode<?> newNode = nodeFactory.makeNormalNode(newElement);
        dissolvedNodeList.add(newNode);

        // 分解したノードをエッジでつなぐ
        this.makeEdges(dissolvedNodeList);

        // ownerSpaceとの調整
        this.replace(dissolvedNodeList);

        // 分解したノード群からCFGを構築
        final CFG newCFG = this.makeCFG(nodeFactory, dissolvedNodeList);

        return newCFG;
    }

    /**
     * 右辺がTernaryOperationInfoである代入文を分解するためのメソッド
     * 
     * @param ternaryOperation
     * @param nodeFactory
     * @return
     */
    private CFG dissolveTernaryOperation(final TernaryOperationInfo ternaryOperation,
            final ICFGNodeFactory nodeFactory) {

        final T core = this.getCore();
        final LocalSpaceInfo ownerSpace = core.getOwnerSpace();
        final int fromLine = core.getFromLine();
        final int toLine = core.getToLine();

        final ConditionInfo condition = ternaryOperation.getCondition();
        final ExpressionInfo trueExpression = ternaryOperation.getTrueExpression();
        final ExpressionInfo falseExpression = ternaryOperation.getFalseExpression();

        // conditionを再構築
        final IfBlockInfo newIfBlock = new IfBlockInfo(fromLine,
                CFGUtility.getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        newIfBlock.setOuterUnit(ownerSpace);
        final ConditionalClauseInfo newConditionalClause = new ConditionalClauseInfo(newIfBlock,
                condition, fromLine, CFGUtility.getRandomNaturalValue(), toLine, CFGUtility
                        .getRandomNaturalValue());
        newIfBlock.setConditionalClause(newConditionalClause);
        final ElseBlockInfo newElseBlock = new ElseBlockInfo(fromLine, CFGUtility
                .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue(), newIfBlock);
        newElseBlock.setOuterUnit(ownerSpace);
        newIfBlock.setSequentElseBlock(newElseBlock);

        // trueExpressionを再構築
        final ExecutableElementInfo trueElement = this.makeNewElement(newIfBlock, trueExpression);

        // falseExpressionを再構築
        final ExecutableElementInfo falseElement = this.makeNewElement(newElseBlock,
                falseExpression);

        newIfBlock.addStatement((StatementInfo) trueElement); // StatementInfo以外はこないはず
        newElseBlock.addStatement((StatementInfo) falseElement); // StatementInfo以外はこないはず

        // 古いノードを削除
        nodeFactory.removeNode(core);
        this.remove();

        // ノードを作成し，つなぐ
        final CFGControlNode conditionNode = nodeFactory.makeControlNode(condition);
        final CFGNode<?> trueNode = nodeFactory.makeNormalNode(trueElement);
        final CFGNode<?> falseNode = nodeFactory.makeNormalNode(falseElement);
        final CFGControlEdge trueEdge = new CFGControlEdge(conditionNode, trueNode, true);
        final CFGControlEdge falseEdge = new CFGControlEdge(conditionNode, falseNode, false);
        conditionNode.addForwardEdge(trueEdge);
        conditionNode.addForwardEdge(falseEdge);
        trueNode.addBackwardEdge(trueEdge);
        falseNode.addBackwardEdge(falseEdge);

        for (final CFGEdge backwardEdge : this.getBackwardEdges()) {
            final CFGNode<?> backwardNode = backwardEdge.getFromNode();
            final CFGEdge newBackwardEdge = backwardEdge.replaceToNode(conditionNode);
            backwardNode.addForwardEdge(newBackwardEdge);
            conditionNode.addBackwardEdge(newBackwardEdge);
        }
        for (final CFGEdge forwardEdge : this.getForwardEdges()) {
            final CFGNode<?> forwardNode = forwardEdge.getToNode();
            final CFGEdge newTrueForwardEdge = forwardEdge.replaceFromNode(trueNode);
            final CFGEdge newFalseForwardEdge = forwardEdge.replaceFromNode(falseNode);
            forwardNode.addBackwardEdge(newTrueForwardEdge);
            forwardNode.addBackwardEdge(newFalseForwardEdge);
            trueNode.addForwardEdge(newTrueForwardEdge);
            falseNode.addForwardEdge(newFalseForwardEdge);
        }

        // ownerSpaceとの調整
        ownerSpace.removeStatement((StatementInfo) core); // StatementInfo以外はこないはず
        ownerSpace.addStatement(newIfBlock);
        ownerSpace.addStatement(newElseBlock);

        // 分解したノード群からCFGを構築
        final SimpleCFG newCFG = new SimpleCFG(nodeFactory);
        newCFG.addNode(conditionNode);
        newCFG.setEnterNode(conditionNode);
        newCFG.addNode(trueNode);
        newCFG.addExitNode(trueNode);
        newCFG.addNode(falseNode);
        newCFG.addExitNode(falseNode);

        // 分解したノードについて再帰的に処理
        final CFG conditionCFG = conditionNode.dissolve(nodeFactory);
        final CFG trueCFG = trueNode.dissolve(nodeFactory);
        final CFG falseCFG = falseNode.dissolve(nodeFactory);

        if (null != conditionCFG) {
            newCFG.removeNode(conditionNode);
            newCFG.addNodes(conditionCFG.getAllNodes());
            newCFG.setEnterNode(conditionCFG.getEnterNode());
        }

        if (null != trueCFG) {
            newCFG.removeNode(trueNode);
            newCFG.addNodes(trueCFG.getAllNodes());
            newCFG.addExitNodes(trueCFG.getExitNodes());
        }

        if (null != falseCFG) {
            newCFG.removeNode(falseNode);
            newCFG.addNodes(falseCFG.getAllNodes());
            newCFG.addExitNodes(falseCFG.getExitNodes());
        }

        return newCFG;
    }

    /**
     * 引数で与えられたoriginalExpressionが右辺となる代入文を作成する．
     * 作成した代入文のCFGノードはdissolvedNodeListの最後に追加され，
     * 代入文の左辺の変数使用オブジェクトはdissolvedVariableUsageListの最後に追加される．
     * 
     * @param originalExpression
     * @param nodeFactory
     * @param dissolvedNodeList
     * @param dissolvedVariableUsageList
     */
    protected final void makeDissolvedNode(final ExpressionInfo originalExpression,
            final ICFGNodeFactory nodeFactory, final List<CFGNode<?>> dissolvedNodeList,
            final List<LocalVariableUsageInfo> dissolvedVariableUsageList) {

        final LocalSpaceInfo ownerSpace = originalExpression.getOwnerSpace();
        assert null != ownerSpace : "ownerSpace is null!";
        final CallableUnitInfo outerCallableUnit = originalExpression.getOwnerMethod();
        assert null != outerCallableUnit : "outerCallableUnit is null!";
        final int fromLine = originalExpression.getFromLine();
        final int toLine = originalExpression.getToLine();
        final TypeInfo type = originalExpression.getType();

        final LocalVariableInfo dummyVariable = new LocalVariableInfo(Collections
                .<ModifierInfo> emptySet(), getDummyVariableName(), type, ownerSpace, fromLine,
                CFGUtility.getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final VariableDeclarationStatementInfo dummyVariableDeclarationStatement = new VariableDeclarationStatementInfo(
                ownerSpace, LocalVariableUsageInfo.getInstance(dummyVariable, false, true,
                        outerCallableUnit, fromLine, CFGUtility.getRandomNaturalValue(), toLine,
                        CFGUtility.getRandomNaturalValue()), originalExpression, fromLine,
                CFGUtility.getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final LocalVariableUsageInfo dummyVariableUsage = LocalVariableUsageInfo.getInstance(
                dummyVariable, true, false, outerCallableUnit, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());

        final CFGNode<?> newNode = nodeFactory.makeNormalNode(dummyVariableDeclarationStatement);
        dissolvedNodeList.add(newNode);
        dissolvedVariableUsageList.add(dummyVariableUsage);
    }

    /**
     * 分解したノードつなぎ，元の場所に入れる
     * 
     * @param dissolvedNodeList
     */
    protected final void makeEdges(final LinkedList<CFGNode<?>> dissolvedNodeList) {

        // 分解したノードをつなぐ
        for (int i = 1; i < dissolvedNodeList.size(); i++) {
            final CFGNode<?> fromNode = dissolvedNodeList.get(i - 1);
            final CFGNode<?> toNode = dissolvedNodeList.get(i);
            final CFGEdge edge = new CFGNormalEdge(fromNode, toNode);
            fromNode.addForwardEdge(edge);
            toNode.addBackwardEdge(edge);
        }

        // 元の場所に入れる
        {
            final CFGNode<?> firstNode = dissolvedNodeList.getFirst();
            final CFGNode<?> lastNode = dissolvedNodeList.getLast();
            for (final CFGEdge backwardEdge : this.getBackwardEdges()) {
                final CFGNode<?> backwardNode = backwardEdge.getFromNode();
                final CFGEdge newBackwardEdge = backwardEdge.replaceToNode(firstNode);
                backwardNode.addForwardEdge(newBackwardEdge);
                firstNode.addBackwardEdge(newBackwardEdge);
            }
            for (final CFGEdge forwardEdge : this.getForwardEdges()) {
                final CFGNode<?> forwardNode = forwardEdge.getToNode();
                final CFGEdge newForwardEdge = forwardEdge.replaceFromNode(lastNode);
                forwardNode.addBackwardEdge(newForwardEdge);
                lastNode.addForwardEdge(newForwardEdge);
            }
        }
    }

    /**
     * 引数で与えられたノード群からCFGを構築して返す．
     * 
     * @param nodeFactory
     * @param dissolvedNodeList
     * @return
     */
    protected final CFG makeCFG(final ICFGNodeFactory nodeFactory,
            LinkedList<CFGNode<?>> dissolvedNodeList) {

        final SimpleCFG cfg = new SimpleCFG(nodeFactory);

        // enterNodeを設定
        {
            final CFGNode<?> firstNode = dissolvedNodeList.getFirst();
            final CFG dissolvedCFG = firstNode.dissolve(nodeFactory);
            if (null != dissolvedCFG) {
                cfg.setEnterNode(dissolvedCFG.getEnterNode());
                cfg.addNodes(dissolvedCFG.getAllNodes());
            } else {
                cfg.setEnterNode(firstNode);
                cfg.addNode(firstNode);
            }
        }

        // exitNodeを設定
        {
            final CFGNode<?> lastNode = dissolvedNodeList.getLast();
            final CFG dissolvedCFG = lastNode.dissolve(nodeFactory);
            if (null != dissolvedCFG) {
                cfg.addExitNodes(dissolvedCFG.getExitNodes());
                cfg.addNodes(dissolvedCFG.getAllNodes());
            } else {
                cfg.addExitNode(lastNode);
                cfg.addNode(lastNode);
            }
        }

        // nodesを設定
        for (int index = 1; index < dissolvedNodeList.size() - 1; index++) {
            final CFGNode<?> node = dissolvedNodeList.get(index);
            final CFG dissolvedCFG = node.dissolve(nodeFactory);
            if (null != dissolvedCFG) {
                cfg.addNodes(dissolvedCFG.getAllNodes());
            } else {
                cfg.addNode(node);
            }
        }

        return cfg;
    }
}
