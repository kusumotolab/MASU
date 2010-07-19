package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.IntraProceduralCFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.CFGNormalNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.DefaultCFGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.node.ICFGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BreakStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ContinueStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.DefaultPDGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.IPDGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGMethodEnterNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGNormalNode;
import jp.ac.osaka_u.ist.sel.metricstool.pdg.node.PDGParameterNode;


/**
 * 手続き内PDGを表すクラス
 * 
 * @author t-miyake, higo
 * 
 */
public class IntraProceduralPDG extends PDG {

    /**
     * PDGの入口ノード
     */
    // protected final SortedSet<PDGNode<?>> enterNodes;
    protected final PDGMethodEnterNode enterNode;

    /**
     * PDGの出口ノード
     */
    protected final SortedSet<PDGNode<?>> exitNodes;

    final CallableUnitInfo unit;

    final boolean buildDataDependence;

    final boolean buildControlDependence;

    final boolean buildExecutionDependence;

    final boolean countObjectStateChange;

    final boolean optimize;

    final boolean dissolve;

    final int dataDependencyDistance;

    final int controlDependencyDistance;

    final int executionDependencyDistance;

    /**
     * PDGを構築時に利用するCFG
     */
    private final IntraProceduralCFG cfg;

    /**
     * PDGを生成する
     * 
     * @param unit
     *            pdgを生成するユニット
     * @param pdgNodeFactory
     *            PDGのノード生成に用いるファクトリ
     * @param cfgNodeFactory
     *            CFGのノード生成に用いるファクトリ
     * @param buildDataDependency
     *            Data Dependencyを生成するか？
     * @param buildControlDependencey
     *            Control Dependencyを生成するか？
     * @param buildExecutionDependency
     *            Execution Dependencyを生成するか？
     * @param countObjectStateChange
     *            メソッド内部によるオブジェクトの変更を考慮するか
     * @param dataDependencyDistance
     *            データ依存辺を引く頂点間の距離の閾値（行の差）
     * @param controlDependencyDistance
     *            制御依存辺を引く頂点間の距離の閾値（行の差）
     * @param executionDependencyDistance
     *            実行依存辺を引く頂点間の距離の閾値（行の差）
     */
    public IntraProceduralPDG(final CallableUnitInfo unit, final IPDGNodeFactory pdgNodeFactory,
            final ICFGNodeFactory cfgNodeFactory, final boolean buildDataDependency,
            final boolean buildControlDependencey, final boolean buildExecutionDependency,
            final boolean countObjectStateChange, final boolean optimize, final boolean dissolve,
            final int dataDependencyDistance, final int controlDependencyDistance,
            final int executionDependencyDistance) {

        super(pdgNodeFactory, cfgNodeFactory);

        if (null == unit) {
            throw new IllegalArgumentException("method is null.");
        }

        this.enterNode = PDGMethodEnterNode.createNode(unit);
        this.exitNodes = new TreeSet<PDGNode<?>>();

        this.unit = unit;

        this.buildDataDependence = buildDataDependency;
        this.buildControlDependence = buildControlDependencey;
        this.buildExecutionDependence = buildExecutionDependency;
        this.countObjectStateChange = countObjectStateChange;
        this.optimize = optimize;
        this.dissolve = dissolve;
        this.dataDependencyDistance = dataDependencyDistance;
        this.controlDependencyDistance = controlDependencyDistance;
        this.executionDependencyDistance = executionDependencyDistance;

        this.cfg = new IntraProceduralCFG(unit, cfgNodeFactory, optimize, dissolve);

        this.buildPDG();
    }

    /**
     * PDGを生成する
     * 
     * @param unit
     *            pdgを生成するユニット
     * @param pdgNodeFactory
     *            PDGのノード生成に用いるファクトリ
     * @param cfgNodeFactory
     *            CFGのノード生成に用いるファクトリ
     * @param buildDataDependency
     *            Data Dependencyを生成するか？
     * @param buildControlDependencey
     *            Control Dependencyを生成するか？
     * @param buildExecutionDependency
     *            Execution Dependencyを生成するか？
     * @param countObjectStateChange
     *            メソッド内部によるオブジェクトの変更を考慮するか
     */
    public IntraProceduralPDG(final CallableUnitInfo unit, final IPDGNodeFactory pdgNodeFactory,
            final ICFGNodeFactory cfgNodeFactory, final boolean buildDataDependency,
            final boolean buildControlDependency, final boolean buildExecutionDependency,
            final boolean countObjectStateChange, final boolean optimize, final boolean dissolve) {

        this(unit, pdgNodeFactory, cfgNodeFactory, buildDataDependency, buildControlDependency,
                buildExecutionDependency, countObjectStateChange, optimize, dissolve,
                Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**
     * PDGを生成する
     * 
     * @param unit
     *            pdgを生成するユニット
     * @param pdgNodeFactory
     *            PDGのノード生成に用いるファクトリ
     * @param cfgNodeFactory
     *            CFGのノード生成に用いるファクトリ
     * @param buildDataDependency
     *            Data Dependencyを生成するか？
     * @param buildControlDependencey
     *            Control Dependencyを生成するか？
     * @param buildExecutionDependency
     *            Execution Dependencyを生成するか？
     */
    public IntraProceduralPDG(final CallableUnitInfo unit, final IPDGNodeFactory pdgNodeFactory,
            final ICFGNodeFactory cfgNodeFactory, final boolean buildDataDependency,
            final boolean buildControlDependency, final boolean buildExecutionDependency) {

        this(unit, pdgNodeFactory, cfgNodeFactory, buildDataDependency, buildControlDependency,
                buildExecutionDependency, false, true, false, Integer.MAX_VALUE, Integer.MAX_VALUE,
                Integer.MAX_VALUE);
    }

    public IntraProceduralPDG(final CallableUnitInfo unit, final IPDGNodeFactory pdgNodeFactory,
            final ICFGNodeFactory cfgNodeFactory) {
        this(unit, pdgNodeFactory, cfgNodeFactory, true, true, true);
    }

    public IntraProceduralPDG(final CallableUnitInfo unit) {
        this(unit, new DefaultPDGNodeFactory(), new DefaultCFGNodeFactory());
    }

    public IntraProceduralPDG(final CallableUnitInfo unit, final boolean countObjectStateChange) {
        this(unit, new DefaultPDGNodeFactory(), new DefaultCFGNodeFactory(), true, true, true,
                countObjectStateChange, true, false);
    }

    public IntraProceduralPDG(final CallableUnitInfo unit, final boolean buildDataDependency,
            final boolean buildControlDependencey, final boolean buildExecutionDependency) {
        this(unit, new DefaultPDGNodeFactory(), new DefaultCFGNodeFactory(), buildDataDependency,
                buildControlDependencey, buildExecutionDependency);
    }

    /**
     * 入口ノードを取得
     * 
     * @return 入口ノード
     */
    public final PDGMethodEnterNode getMethodEnterNode() {
        return this.enterNode;
    }

    /**
     * 出口ノードを取得
     * 
     * @return 出口ノード
     */
    public final SortedSet<PDGNode<?>> getExitNodes() {
        return Collections.unmodifiableSortedSet(this.exitNodes);
    }

    public boolean isBuiltDataDependency() {
        return this.buildDataDependence;
    }

    public boolean isBuiltControlDependency() {
        return this.buildControlDependence;
    }

    public boolean isBuiltExecutionDependency() {
        return this.buildExecutionDependence;
    }

    /**
     * コンストラクタで与えられたCallableUnitInfoのPDGを構築する
     */
    @Override
    protected void buildPDG() {

        final CFGNode<?> cfgEnterNode = this.cfg.getEnterNode();

        {
            // メソッドのエンターノードから直接の内部文に対して制御依存辺を引く
            if (this.isBuiltControlDependency()) {
                final PDGMethodEnterNode enterNode = this.getMethodEnterNode();
                this.nodes.add(enterNode);
                final CallableUnitInfo unit = this.getMethodInfo();
                this.buildControlDependence(enterNode, unit);
            }

            // メソッドのエンターノードからメソッド内で最初に実行される文に実行依存辺を引く
            if (this.isBuiltExecutionDependency()) {
                if (null != cfgEnterNode) {
                    final PDGNode<?> toPDGNode = this.makeNode(cfgEnterNode);
                    this.enterNode.addExecutionDependingNode(toPDGNode);
                }
            }
        }

        // unitの引数を処理
        for (final ParameterInfo parameter : this.unit.getParameters()) {

            final PDGParameterNode pdgParameterNode = PDGParameterNode.getInstance(parameter);
            this.pdgNodeFactory.addNode(pdgParameterNode);
            this.nodes.add(pdgParameterNode);

            if (null != cfgEnterNode) {
                this.buildDataDependence(cfgEnterNode, pdgParameterNode, parameter,
                        new HashSet<CFGNode<?>>());
            }
        }

        // CFGの入口ノードから処理を行う
        final Set<CFGNode<?>> checkedNodes = new HashSet<CFGNode<?>>();
        if (null != cfgEnterNode) {

            // 引数がない場合は，CFGの入口ノードがPDGの入口ノードになる
            /*
             * if (0 == this.unit.getParameters().size()) { final PDGNode<?>
             * pdgEnterNode = this.makeNode(cfgEnterNode);
             * this.enterNodes.add(pdgEnterNode); }
             */

            this.buildDependence(cfgEnterNode, checkedNodes);
        }

        // CFGの出口ノードはPDGの出口ノードになる
        for (final CFGNode<?> cfgExitNode : this.cfg.getExitNodes()) {
            final PDGNode<?> pdgExitNode = this.makeNode(cfgExitNode);
            this.exitNodes.add(pdgExitNode);
        }

        // Unreablebleなノードに対しても処理を行う

        if (!this.cfg.isEmpty()) {
            final Set<CFGNode<?>> unreachableNodes = new HashSet<CFGNode<?>>();
            unreachableNodes.addAll(this.cfg.getAllNodes());
            unreachableNodes.removeAll(this.cfg.getReachableNodes(cfgEnterNode));
            for (final CFGNode<?> unreachableNode : unreachableNodes) {
                this.buildDependence(unreachableNode, checkedNodes);
            }
        }
    }

    private void buildDependence(final CFGNode<?> cfgNode, final Set<CFGNode<?>> checkedNodes) {

        if (null == cfgNode || null == checkedNodes) {
            throw new IllegalArgumentException();
        }

        // 既に調査されているノードである場合は何もしない
        if (checkedNodes.contains(cfgNode)) {
            return;
        }

        // 現在のノードを調査済みに追加
        else {
            checkedNodes.add(cfgNode);
        }

        // 与えられたCFGノードに対応するPDGノードを作成
        final PDGNode<?> pdgNode = this.makeNode(cfgNode);

        // 与えられたCFGノードで定義された各変数に対して，
        // その変数を参照しているノードにDataDependenceを引く
        if (this.isBuiltDataDependency()) {
            for (final VariableInfo<? extends UnitInfo> variable : cfgNode
                    .getDefinedVariables(this.countObjectStateChange)) {

                for (final CFGNode<?> forwardNode : cfgNode.getForwardNodes()) {
                    final Set<CFGNode<?>> checkedNodesForDefinedVariables = new HashSet<CFGNode<?>>();
                    // checkedNodesForDefinedVariables.add(cfgNode);
                    this.buildDataDependence(forwardNode, pdgNode, variable,
                            checkedNodesForDefinedVariables);
                }
            }
        }

        // 与えられたCFGノードからControlDependenceを引く
        if (this.isBuiltControlDependency()) {
            if (pdgNode instanceof PDGControlNode) {
                final ConditionInfo condition = (ConditionInfo) cfgNode.getCore();
                this.buildControlDependence((PDGControlNode) pdgNode, PDGUtility
                        .getOwnerConditionalBlock(condition));
            }
        }

        // 与えられたCFGノードからExecutionDependenceを引く
        if (this.isBuiltExecutionDependency()) {
            final PDGNode<?> fromPDGNode = this.makeNode(cfgNode);
            for (final CFGNode<?> toNode : cfgNode.getForwardNodes()) {
                final PDGNode<?> toPDGNode = this.makeNode(toNode);
                final int distance = Math.abs(toPDGNode.getCore().getFromLine()
                        - fromPDGNode.getCore().getToLine()) + 1;
                if (distance <= this.dataDependencyDistance) {
                    fromPDGNode.addExecutionDependingNode(toPDGNode);
                }

            }
        }

        for (final CFGNode<?> forwardNode : cfgNode.getForwardNodes()) {
            this.buildDependence(forwardNode, checkedNodes);
        }
    }

    /**
     * 第一引数で与えられたCFGのノードに対して，第二引数で与えられたPDGノードからのデータ依存があるかを調べ， 或る場合は，データ依存辺を引く
     * 
     * @param cfgNode
     * @param fromPDGNode
     * @param variable
     */
    private void buildDataDependence(final CFGNode<?> cfgNode,
            final PDGNode<? extends CFGNode<?>> fromPDGNode, final VariableInfo<?> variable,
            final Set<CFGNode<?>> checkedCFGNodes) {

        if (null == cfgNode || null == fromPDGNode || null == variable || null == checkedCFGNodes) {
            throw new IllegalArgumentException();
        }

        // 既に調べているノード場合は何もしないでメソッドを抜ける
        if (checkedCFGNodes.contains(cfgNode)) {
            return;
        }

        // たった今調べたノードをチェックしたノードに追加
        else {
            checkedCFGNodes.add(cfgNode);
        }

        // cfgNodeがvariableを参照している場合は，
        // cfgNodeからPDGNodeを作成し，fromPDGNodeからデータ依存辺を引く
        if (cfgNode.getReferencedVariables().contains(variable)) {

            final PDGNode<? extends CFGNode<?>> toPDGNode = this.makeNode(cfgNode);

            // fromノードとtoノードの距離が閾値以内であればエッジを引く
            final int distance = Math.abs(toPDGNode.getCore().getFromLine()
                    - fromPDGNode.getCore().getToLine()) + 1;
            if (distance <= this.dataDependencyDistance) {
                fromPDGNode.addDataDependingNode(toPDGNode, variable);
            }
        }

        // cfgNodeがvariableに代入している場合は，
        // これ以降のノードのデータ依存は調べない
        if (cfgNode.getDefinedVariables(this.countObjectStateChange).contains(variable)) {
            return;
        }

        // cfgNodeのフォワードノードに対してもデータ依存を調べる
        else {
            for (final CFGNode<?> forwardNode : cfgNode.getForwardNodes()) {
                this.buildDataDependence(forwardNode, fromPDGNode, variable, checkedCFGNodes);
            }
        }
    }

    /**
     * 
     * 第一引数で与えられたノードに対して，第二引数で与えられたblockに含まれる文に制御依存辺を引く
     * 
     * @param fromPDGNode
     * @param block
     */
    private void buildControlDependence(final PDGControlNode fromPDGNode, final LocalSpaceInfo block) {

        for (final StatementInfo innerStatement : block.getStatements()) {

            // 単文の場合は，fromPDGNodeからの制御依存辺を引く
            // CaseEntryの場合は，制御依存辺はいらない
            // Break文の場合，Continue文の場合も制御依存辺はいらない
            if (innerStatement instanceof SingleStatementInfo
                    && !(innerStatement instanceof BreakStatementInfo)
                    && !(innerStatement instanceof ContinueStatementInfo)) {

                final Set<PDGNode<?>> toPDGNodes = new HashSet<PDGNode<?>>();
                final Set<CFGNode<?>> dissolvedCFGNodes = this.cfgNodeFactory
                        .getDissolvedNodes(innerStatement);

                // innerStatementが複数のCFGノードに分割されていた場合
                if (null != dissolvedCFGNodes) {
                    for (final CFGNode<?> dissolvedCFGNode : dissolvedCFGNodes) {
                        final PDGNode<?> toPDGNode = this.makeNode(dissolvedCFGNode);
                        toPDGNodes.add(toPDGNode);
                    }
                }

                // 分割されていない場合
                else {
                    final CFGNode<?> cfgNode = this.cfgNodeFactory.getNode(innerStatement);
                    final PDGNode<?> toPDGNode = this.makeNode(cfgNode);
                    toPDGNodes.add(toPDGNode);
                }

                // fromノードとtoノードの距離が閾値以内であればエッジを引く
                for (final PDGNode<?> toPDGNode : toPDGNodes) {
                    final int distance = Math.abs(toPDGNode.getCore().getFromLine()
                            - fromPDGNode.getCore().getToLine()) + 1;
                    if (distance <= this.controlDependencyDistance) {
                        fromPDGNode.addControlDependingNode(toPDGNode,
                                !(block instanceof ElseBlockInfo));
                    }
                }
            }

            // Block文の場合は，条件付き文であれば，単文の時と同じ処理
            // そうでなければ，さらに内部を調べる
            else if (innerStatement instanceof BlockInfo) {

                if (innerStatement instanceof ConditionalBlockInfo) {

                    {
                        final ConditionInfo condition = ((ConditionalBlockInfo) innerStatement)
                                .getConditionalClause().getCondition();

                        final Set<PDGNode<?>> toPDGNodes = new HashSet<PDGNode<?>>();
                        final Set<CFGNode<?>> dissolvedCFGNodes = this.cfgNodeFactory
                                .getDissolvedNodes(condition);

                        // innerStatementが複数のCFGノードに分割されていた場合
                        if (null != dissolvedCFGNodes) {
                            for (final CFGNode<?> dissolvedCFGNode : dissolvedCFGNodes) {
                                final PDGNode<?> toPDGNode = this.makeNode(dissolvedCFGNode);
                                toPDGNodes.add(toPDGNode);
                            }
                        }

                        // 分割されていない場合
                        else {
                            final CFGNode<?> cfgNode = this.cfgNodeFactory.getNode(condition);
                            final PDGNode<?> toPDGNode = this.makeNode(cfgNode);
                            toPDGNodes.add(toPDGNode);
                        }

                        // fromノードとtoノードの距離が閾値以内であればエッジを引く
                        for (final PDGNode<?> toPDGNode : toPDGNodes) {
                            final int distance = Math.abs(toPDGNode.getCore().getFromLine()
                                    - fromPDGNode.getCore().getToLine()) + 1;
                            if (distance <= this.controlDependencyDistance) {
                                fromPDGNode.addControlDependingNode(toPDGNode,
                                        !(block instanceof ElseBlockInfo));
                            }
                        }
                    }

                    if (innerStatement instanceof ForBlockInfo) {
                        final ForBlockInfo forBlock = (ForBlockInfo) innerStatement;
                        for (final ConditionInfo expression : forBlock.getInitializerExpressions()) {

                            final Set<PDGNode<?>> toPDGNodes = new HashSet<PDGNode<?>>();
                            final Set<CFGNode<?>> dissolvedCFGNodes = this.cfgNodeFactory
                                    .getDissolvedNodes(expression);

                            // innerStatementが複数のCFGノードに分割されていた場合
                            if (null != dissolvedCFGNodes) {
                                for (final CFGNode<?> dissolvedCFGNode : dissolvedCFGNodes) {
                                    final PDGNode<?> toPDGNode = this.makeNode(dissolvedCFGNode);
                                    toPDGNodes.add(toPDGNode);
                                }
                            }

                            // 分割されていない場合
                            else {
                                final CFGNode<?> cfgNode = this.cfgNodeFactory.getNode(expression);
                                final PDGNode<?> toPDGNode = this.makeNode(cfgNode);
                                toPDGNodes.add(toPDGNode);
                            }

                            // fromノードとtoノードの距離が閾値以内であればエッジを引く
                            for (final PDGNode<?> toPDGNode : toPDGNodes) {
                                final int distance = Math.abs(toPDGNode.getCore().getFromLine()
                                        - fromPDGNode.getCore().getToLine()) + 1;
                                if (distance <= this.controlDependencyDistance) {
                                    fromPDGNode.addControlDependingNode(toPDGNode,
                                            !(block instanceof ElseBlockInfo));
                                }
                            }
                        }
                    }
                }

                // elseブロックの場合はここでは，依存辺は引かない
                else if (innerStatement instanceof ElseBlockInfo) {

                }

                else {
                    this.buildControlDependence(fromPDGNode, (BlockInfo) innerStatement);
                }
            }
        }

        // if文の場合は，elseへの対応もしなければならない
        if (block instanceof IfBlockInfo) {
            final ElseBlockInfo elseBlock = ((IfBlockInfo) block).getSequentElseBlock();
            if (null != elseBlock) {
                this.buildControlDependence(fromPDGNode, elseBlock);
            }
        }

        // for文の繰り返し文への対応もしなければならない
        if (block instanceof ForBlockInfo) {

            final ForBlockInfo forBlock = (ForBlockInfo) block;
            for (final ExpressionInfo expression : forBlock.getIteratorExpressions()) {

                final Set<PDGNode<?>> toPDGNodes = new HashSet<PDGNode<?>>();
                final Set<CFGNode<?>> dissolvedCFGNodes = this.cfgNodeFactory
                        .getDissolvedNodes(expression);

                // innerStatementが複数のCFGノードに分割されていた場合
                if (null != dissolvedCFGNodes) {
                    for (final CFGNode<?> dissolvedCFGNode : dissolvedCFGNodes) {
                        final PDGNode<?> toPDGNode = this.makeNode(dissolvedCFGNode);
                        toPDGNodes.add(toPDGNode);
                    }
                }

                // 分割されていない場合
                else {
                    final CFGNode<?> cfgNode = this.cfgNodeFactory.getNode(expression);
                    final PDGNode<?> toPDGNode = this.makeNode(cfgNode);
                    toPDGNodes.add(toPDGNode);
                }

                // fromノードとtoノードの距離が閾値以内であればエッジを引く
                for (final PDGNode<?> toPDGNode : toPDGNodes) {
                    final int distance = Math.abs(toPDGNode.getCore().getFromLine()
                            - fromPDGNode.getCore().getToLine()) + 1;
                    if (distance <= this.controlDependencyDistance) {
                        fromPDGNode.addControlDependingNode(toPDGNode,
                                !(block instanceof ElseBlockInfo));
                    }
                }
            }
        }
    }

    private PDGNode<?> makeNode(final CFGNode<?> cfgNode) {

        if (cfgNode instanceof CFGControlNode) {
            return this.makeControlNode((CFGControlNode) cfgNode);
        } else if (cfgNode instanceof CFGNormalNode<?>) {
            return this.makeNormalNode((CFGNormalNode<?>) cfgNode);
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * 引数で与えられたCFGノードからPDGノードを作成する
     * 
     * @param cfgNode 
     * @return
     */
    private PDGControlNode makeControlNode(final CFGControlNode cfgNode) {

        final IPDGNodeFactory factory = this.getNodeFactory();
        final PDGControlNode node = factory.makeControlNode(cfgNode);
        if (null == node) {
            return null;
        }

        this.nodes.add(node);
        return node;
    }

    /**
     * 引数で与えられたCFGノードからPDGノードを作成する
     * 
     * @param cfgNode
     * @return
     */
    private PDGNormalNode<?> makeNormalNode(final CFGNormalNode<?> cfgNode) {

        final IPDGNodeFactory factory = this.getNodeFactory();
        final PDGNormalNode<?> node = factory.makeNormalNode(cfgNode);
        if (null == node) {
            return null;
        }

        this.nodes.add(node);
        return node;
    }

    /**
     * PDGの構築に利用したCFGを取得
     * 
     * @return
     */
    public IntraProceduralCFG getCFG() {
        return this.cfg;
    }

    public CallableUnitInfo getMethodInfo() {
        return this.unit;
    }
}
