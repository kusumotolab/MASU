package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGNormalNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.DefaultCFGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.ICFGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.IntraProceduralCFG;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;


/**
 * 手続き内PDGを表すクラス
 * 
 * @author t-miyake, higo
 *
 */
public class IntraProceduralPDG extends PDG {

    private final CallableUnitInfo unit;

    /**
     * PDGを構築時に利用するCFG
     */
    private final IntraProceduralCFG cfg;

    public IntraProceduralPDG(final CallableUnitInfo unit, final IPDGNodeFactory pdgNodeFactory,
            final ICFGNodeFactory cfgNodeFactory) {
        super(pdgNodeFactory);
        if (null == unit) {
            throw new IllegalArgumentException("method is null.");
        }

        this.unit = unit;

        this.cfg = new IntraProceduralCFG(unit, cfgNodeFactory);

        this.buildPDG();
    }

    public IntraProceduralPDG(final CallableUnitInfo unit) {
        this(unit, new DefaultPDGNodeFactory(), new DefaultCFGNodeFactory());
    }

    /**
     * コンストラクタで与えられたCallableUnitInfoのPDGを構築する
     */
    @Override
    protected void buildPDG() {

        final CFGNode<?> enterNode = this.cfg.getEnterNode();

        // unitの引数を処理
        for (final ParameterInfo parameter : this.unit.getParameters()) {

            final PDGNode<?> pdgNode = this.makeNormalNode(parameter);
            if (null != enterNode) {
                this.buildDataDependence(enterNode, pdgNode, parameter, new HashSet<CFGNode<?>>());
            }
        }

        // CFGの入口ノードから処理を行う
        if (null != enterNode) {
            this.buildDependence(enterNode, new HashSet<CFGNode<?>>());
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

        //与えられたCFGノードに対応するPDGノードを作成
        final PDGNode<?> pdgNode = this.makeNode(cfgNode);

        //与えられたCFGノードで定義された各変数に対して，
        //その変数を参照しているノードにDataDependenceを引く
        for (final VariableInfo<? extends UnitInfo> variable : cfgNode.getDefinedVariables()) {

            for (final CFGNode<?> forwardNode : cfgNode.getForwardNodes()) {
                final Set<CFGNode<?>> checkedNodesForDefinedVariables = new HashSet<CFGNode<?>>();
                checkedNodesForDefinedVariables.add(cfgNode);
                this.buildDataDependence(forwardNode, pdgNode, variable,
                        checkedNodesForDefinedVariables);
            }
        }

        //与えられたCFGノードからControlDependenceを引く
        if (pdgNode instanceof PDGControlNode) {
            final ConditionInfo condition = (ConditionInfo) cfgNode.getCore();
            this.buildControlDependence((PDGControlNode) pdgNode, Utility
                    .getOwnerConditionalBlock(condition));
        }

        for (final CFGNode<?> forwardNode : cfgNode.getForwardNodes()) {
            this.buildDependence(forwardNode, checkedNodes);
        }
    }

    /**
     * 第一引数で与えられたCFGのノードに対して，第二引数で与えられたPDGノードからのデータ依存があるかを調べ，
     * 或る場合は，データ依存辺を引く
     * 
     * @param cfgNode
     * @param fromPDGNode
     * @param variable
     */
    private void buildDataDependence(final CFGNode<?> cfgNode, final PDGNode<?> fromPDGNode,
            final VariableInfo<?> variable, final Set<CFGNode<?>> checkedCFGNodes) {

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
        if (cfgNode.getUsedVariables().contains(variable)) {

            final PDGNode<?> toPDGNode = this.makeNode(cfgNode);
            fromPDGNode.addDataDependingNode(toPDGNode);
        }

        // cfgNodeがvariableに代入している場合は，
        // これ以降のノードのデータ依存は調べない
        if (cfgNode.getDefinedVariables().contains(variable)) {
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

      * 第一引数で与えられたノードに対して，第二引数で与えられたblockに含まれる文に制御依存辺を引く
      * 
      * @param fromPDGNode
      * @param block
      */
    private void buildControlDependence(final PDGControlNode fromPDGNode, final BlockInfo block) {

        for (final StatementInfo innerStatement : block.getStatements()) {

            // 単文やケースエントリの場合は，fromPDGNodeからの制御依存辺を引く
            if (innerStatement instanceof SingleStatementInfo
                    || innerStatement instanceof CaseEntryInfo) {
                final PDGNode<?> toPDGNode = this.makeNormalNode(innerStatement);
                fromPDGNode.addControlDependingNode(toPDGNode);
            }

            // Block文の場合は，条件付き文であれば，単文の時と同じ処理
            //　そうでなければ，さらに内部を調べる
            else if (innerStatement instanceof BlockInfo) {

                if (innerStatement instanceof ConditionalBlockInfo) {

                    final ConditionInfo condition = ((ConditionalBlockInfo) innerStatement)
                            .getConditionalClause().getCondition();
                    final PDGNode<?> toPDGNode = this.makeControlNode(condition);
                    fromPDGNode.addControlDependingNode(toPDGNode);
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

    }

    /**
     * 定義ノードとデータ依存候補ノードがデータ依存関係にある場合，データ依存辺を構築
     * @param definitionNode 定義ノード
     * @param definedVariable 定義ノードで定義されている変数のうち，構築するデータ依存辺に関係する変数
     * @param dependCandidates データ依存候補ノード
     * @param passedNodeCache 調査済みのCFG制御ノードのキャッシュ
     */
    private void buildDataDependence(final PDGNode<?> definitionNode,
            final VariableInfo<? extends UnitInfo> definedVariable,
            final CFGNode<? extends ExecutableElementInfo> dependCandidates,
            final Set<CFGControlNode> passedNodeCache) {

        final PDGNode<?> firstCandidate = this.makeNode(dependCandidates);

        // 候補ノードが存在する場合，最初の候補ノードへのデータ依存を調査
        if (null != firstCandidate) {

            // 候補ノードが定義変数を参照している場合，データ依存変を構築
            if (firstCandidate.isReferenace(definedVariable)) {
                boolean aleadyAdded = !definitionNode.addDataDependingNode(firstCandidate);
                if (aleadyAdded) {
                    return;
                }
            }

            // 候補ノード上で定義変数が再定義されている場合，
            // 以降の経路で現在の定義ノードからのデータ依存は存在しないので終了
            if (firstCandidate.isDefine(definedVariable)) {
                return;
            }
        }

        // 最初の候補ノードから派生する全ノードに対してデータ依存を調査
        for (final CFGNode<? extends ExecutableElementInfo> nextCandidate : dependCandidates
                .getForwardNodes()) {

            if (nextCandidate instanceof CFGControlNode) {
                if (!passedNodeCache.add((CFGControlNode) nextCandidate)) {
                    continue;
                }
            }

            this.buildDataDependence(definitionNode, definedVariable, nextCandidate,
                    passedNodeCache);
        }
    }

    /**
     * 引数で与えられた制御文からの制御依存辺を構築
     * @param conditionalBlock
     */
    private void buildControlFlow(final ConditionalBlockInfo conditionalBlock) {

        final ConditionInfo condition = conditionalBlock.getConditionalClause().getCondition();
        final PDGControlNode controlNode = this.makeControlNode(condition);
        this.buildControlFlow(controlNode, conditionalBlock.getStatements());

        if (conditionalBlock instanceof IfBlockInfo) {
            IfBlockInfo ifBlock = (IfBlockInfo) conditionalBlock;

            if (ifBlock.hasElseBlock()) {
                this.buildControlFlow(controlNode, ifBlock.getSequentElseBlock().getStatements());
            }
        }
    }

    /**
     * 制御ノードから第2引数で与えられた文に対する制御依存辺を構築
     * @param controlNode 制御ノード
     * @param controlledStatements 第1引数の制御ノードに依存している文の集合
     */
    private void buildControlFlow(final PDGControlNode controlNode,
            final SortedSet<StatementInfo> controlledStatements) {

        for (final StatementInfo controlledStatement : controlledStatements) {

            if (controlledStatement instanceof SingleStatementInfo
                    || controlledStatement instanceof ConditionalBlockInfo) {
                // 単文や制御文の場合，それ自体を制御される文として追加

                final PDGNode<?> controlledNode = this.makeNormalNode(controlledStatement);

                assert null != controlledNode;

                controlNode.addControlDependingNode(controlledNode);

            } else if (controlledStatement instanceof BlockInfo) {
                // 制御文以外のブロック文の場合，内部の文を制御される文に追加
                this.buildControlFlow(controlNode, ((BlockInfo) controlledStatement)
                        .getStatements());
            }
        }
    }

    private PDGNode<?> makeNode(final CFGNode<?> cfgNode) {

        final ExecutableElementInfo element = cfgNode.getCore();
        if (cfgNode instanceof CFGControlNode) {
            return this.makeControlNode((ConditionInfo) element);
        } else if (cfgNode instanceof CFGNormalNode<?>) {
            return this.makeNormalNode(element);
        } else {
            throw new IllegalStateException();
        }
    }

    /**
     * 引数で与えられた条件を表すPDG制御ノードを作成する
     * 
     * @param element ノードを作成したい要素
     * @return
     */
    private PDGControlNode makeControlNode(final ConditionInfo condition) {

        final IPDGNodeFactory factory = this.getNodeFactory();
        final PDGControlNode node = factory.makeControlNode(condition);
        if (null == node) {
            return null;
        }

        this.nodes.add(node);
        return node;
    }

    /**
     * 引数で与えられた条件を表すPDG普通ノード(制御ノード以外)を作成する
     * 
     * @param element ノードを作成したい要素
     * @return
     */
    private PDGNormalNode<?> makeNormalNode(final Object element) {

        final IPDGNodeFactory factory = this.getNodeFactory();
        final PDGNormalNode<?> node = factory.makeNormalNode(element);
        if (null == node) {
            return null;
        }

        this.nodes.add(node);
        return node;
    }

    /**
     * PDGの構築に利用したCFGを取得
     * @return
     */
    public IntraProceduralCFG getCFG() {
        return this.cfg;
    }

    public CallableUnitInfo getMethodInfo() {
        return this.unit;
    }
}
