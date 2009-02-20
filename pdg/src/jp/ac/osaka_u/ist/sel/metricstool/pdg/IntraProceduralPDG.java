package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGControlNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGNode;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.DefaultCFGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.ICFGNodeFactory;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.IntraProceduralCFG;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParameterInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableInfo;


/**
 * 手続き内PDGを表すクラス
 * 
 * @author t-miyake
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

    @Override
    protected void buildPDG() {
        if (null == this.getCFG().getEnterNode()) {
            return;
        }

        for (final ParameterInfo parameter : this.unit.getParameters()) {
            final ParameterNode parameterNode = new ParameterNode(parameter);
            this.enterNodes.add(parameterNode);
            this.buildDataDependence(parameterNode, parameter, this.cfg.getEnterNode(),
                    new HashSet<CFGControlNode>());
        }

        for (final StatementInfo statement : LocalSpaceInfo.getAllStatements(this.unit)) {

            // statementと他の文とのデータ依存を構築
            {
                final PDGNode<?> pdgNode = this.getStatementNode(statement);
                if (null == pdgNode) {
                    continue;
                }

                final CFGNode<? extends StatementInfo> cfgNode = this.cfg.getCFGNode(statement);

                // このpdgNodeに対応する文で定義されている全変数に関してデータ依存辺を構築
                for (final VariableInfo<? extends UnitInfo> definedVariable : pdgNode
                        .getDefinedVariables()) {

                    // cfgNodeから派生するすべての経路のデータ依存辺を構築
                    for (final CFGNode<? extends StatementInfo> forwardCFGNode : cfgNode
                            .getForwardNodes()) {
                        this.buildDataDependence(pdgNode, definedVariable, forwardCFGNode,
                                new HashSet<CFGControlNode>());
                    }
                }
            }

            if (statement instanceof ConditionalBlockInfo) {
                // statementが制御文の場合は制御フローを構築
                this.buildControlFlow((ConditionalBlockInfo) statement);
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
            final CFGNode<? extends StatementInfo> dependCandidates,
            final Set<CFGControlNode> passedNodeCache) {

        final PDGNode<?> firstCandidate = this.getStatementNode(dependCandidates.getStatement());

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
        for (final CFGNode<? extends StatementInfo> nextCandidate : dependCandidates
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
     * @param controlStatement
     */
    private void buildControlFlow(final ConditionalBlockInfo controlStatement) {
        final PDGControlNode controlNode = (PDGControlNode) this.getStatementNode(controlStatement);
        this.buildControlFlow(controlNode, controlStatement.getStatements());

        if (controlStatement instanceof IfBlockInfo) {
            IfBlockInfo ifBlock = (IfBlockInfo) controlStatement;

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

                final PDGNode<?> controlledNode = this.getStatementNode(controlledStatement);

                assert null != controlledNode;

                controlNode.addControlDependingNode(controlledNode);

            } else if (controlledStatement instanceof BlockInfo) {
                // 制御文以外のブロック文の場合，内部の文を制御される文に追加
                this.buildControlFlow(controlNode, ((BlockInfo) controlledStatement)
                        .getStatements());
            }
        }
    }

    protected PDGNode<?> getStatementNode(final StatementInfo statement) {
        final PDGNode<?> node = this.nodeFactory.makeNode(statement);
        if (statement instanceof ReturnStatementInfo) {
            this.exitNodes.add(node);
        }
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

    public final ParameterNode getParamerNode(final ParameterInfo parameter) {
        for (final PDGNode<?> startNode : this.enterNodes) {
            if (startNode instanceof ParameterNode) {
                ParameterNode parameterNode = (ParameterNode) startNode;
                if (parameterNode.getCore().equals(parameter)) {
                    return parameterNode;
                }
            }
        }

        return null;
    }

}
