package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.DoBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SwitchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.WhileBlockInfo;


/**
 * 手続き間を横断しない制御フローグラフ(CFG)を表すクラス
 * 
 * @author t-miyake
 * 
 */
public class IntraProceduralCFG extends CFG {

    /**
     * 制御フローグラフに対応するローカル空間
     */
    private final LocalSpaceInfo localSpace;

    /**
     * 生成する制御フローグラフに対応するローカル空間とCFGノードのファクトリを与えて初期化
     * 
     * @param unit
     *            生成する制御フローグラフに対応するローカル空間
     * @param nodeFactory
     *            CFGノードのファクトリ
     */
    public IntraProceduralCFG(final CallableUnitInfo unit, final ICFGNodeFactory nodeFactory) {

        super(nodeFactory);

        if (null == unit) {
            throw new IllegalArgumentException("unit is null");
        }

        if (unit instanceof ExternalMethodInfo || unit instanceof ExternalConstructorInfo) {
            throw new IllegalArgumentException("unit is an external infromation.");
        }

        this.localSpace = unit;

        this.buildCFG(unit);
    }

    /**
     * 生成する制御フローグラフに対応するローカル空間を与えて初期化
     * 
     * @param unit
     *            生成する制御フローグラフに対応するローカル空間
     */
    public IntraProceduralCFG(final CallableUnitInfo unit) {
        this(unit, new DefaultCFGNodeFactory());
    }

    private IntraProceduralCFG(final ICFGNodeFactory nodeFactory) {
        super(nodeFactory);

        this.localSpace = null;
    }

    private IntraProceduralCFG(final StatementInfo statement, final ICFGNodeFactory nodeFactory) {
        super(nodeFactory);
        if (statement instanceof BlockInfo) {
            this.localSpace = (BlockInfo) statement;

            this.buildCFG(this.localSpace);
        } else {
            this.localSpace = null;
            this.enterNode = this.nodeFactory.makeNode(statement);
            this.exitNodes.add(this.enterNode);
        }
    }

    /**
     * CFGを構築
     * 
     * @param local
     *            構築されるCFGに対応するローカル空間
     */
    private void buildCFG(final LocalSpaceInfo local) {

        this.enterNode = null;
        this.exitNodes.clear();

        CFG innerCFG = this.buildInnerCFG(local);

        // 対応するローカル空間が制御文でない場合
        if (!(local instanceof ConditionalBlockInfo)) {
            // 内部CFGがそのままCFGとなる
            this.enterNode = innerCFG.getEnterNode();
            this.exitNodes.addAll(innerCFG.getExitNodes());

            return;
        }

        // 対応するローカル空間がif文の場合
        if (local instanceof IfBlockInfo) {
            final IfBlockInfo ifBlock = (IfBlockInfo) local;
            this.enterNode = this.nodeFactory.makeNode(ifBlock);

            if (!innerCFG.isEmpty()) {
                this.enterNode.addForwardNode(innerCFG.getEnterNode());
            } else {
                this.exitNodes.add(this.enterNode);
            }

            this.exitNodes.addAll(innerCFG.getExitNodes());

            if (ifBlock.hasElseBlock()) {
                final CFG elseCFG = new IntraProceduralCFG(ifBlock.getSequentElseBlock(),
                        this.nodeFactory);
                if (!elseCFG.isEmpty()) {
                    this.enterNode.addForwardNode(elseCFG.getEnterNode());
                    this.exitNodes.addAll(elseCFG.getExitNodes());
                } else {
                    this.exitNodes.add(this.enterNode);
                }
            } else {
                this.exitNodes.add(this.enterNode);
            }

        }
        // 対応するローカル空間がwhile文の場合
        else if (local instanceof WhileBlockInfo) {
            // 制御文自体が入口ノードかつ出口ノード
            this.enterNode = this.nodeFactory.makeNode((ConditionalBlockInfo) local);
            this.exitNodes.add(this.enterNode);

            // 内部のCFGと連結．ループ文なので内部のCFGが空の場合は入口ノードのフォワードノードは入口ノード
            this.enterNode.addForwardNode(!innerCFG.isEmpty() ? innerCFG.getEnterNode()
                    : this.enterNode);

            for (final CFGNode<? extends ExecutableElementInfo> innerExitNode : innerCFG
                    .getExitNodes()) {
                if (innerExitNode.isExitNode(this.localSpace)) {
                    this.exitNodes.add(innerExitNode);
                } else {
                    innerExitNode.addForwardNode(this.enterNode);
                }
            }
        }
        // 対応するローカル空間がfor文の場合
        else if (local instanceof ForBlockInfo) {

            //入口ノードは初期化式，もし初期化式がない場合は条件式
            //出口は条件式
            final List<CFGNode<? extends ExecutableElementInfo>> initializerNodes = new LinkedList<CFGNode<? extends ExecutableElementInfo>>();
            for (final ConditionInfo initializer : ((ForBlockInfo) local)
                    .getInitializerExpressions()) {

                final CFGNode<? extends ExecutableElementInfo> initializerNode = this.nodeFactory
                        .makeNode(initializer);
                initializerNodes.add(initializerNode);
            }

            final CFGNode<? extends ExecutableElementInfo>[] initializerNodeArray = initializerNodes
                    .toArray(new CFGNode<?>[0]);
            for (int i = 0; i < initializerNodeArray.length - 1; i++) {
                initializerNodeArray[i].addForwardNode(initializerNodeArray[i + 1]);
            }

            final CFGNode<? extends ExecutableElementInfo> conditionNode = this.nodeFactory
                    .makeNode((ForBlockInfo) local);
            this.exitNodes.add(conditionNode);

            if (0 < initializerNodes.size()) {
                this.enterNode = initializerNodeArray[0];
                initializerNodeArray[initializerNodeArray.length - 1].addForwardNode(conditionNode);
            } else {
                this.enterNode = conditionNode;
            }

            // 内部のCFGと連結．ループ文なので内部のCFGが空の場合は入口ノードのフォワードノードは入口ノード
            conditionNode.addForwardNode(!innerCFG.isEmpty() ? innerCFG.getEnterNode()
                    : conditionNode);

            // 繰り返し式を追加
            final List<CFGNode<? extends ExecutableElementInfo>> iteratorNodes = new LinkedList<CFGNode<? extends ExecutableElementInfo>>();
            for (final ExpressionInfo iterator : ((ForBlockInfo) local).getIteratorExpressions()) {

                final CFGNode<? extends ExecutableElementInfo> iteratorNode = this.nodeFactory
                        .makeNode(iterator);
                iteratorNodes.add(iteratorNode);
            }

            final CFGNode<? extends ExecutableElementInfo>[] iteratorNodeArray = iteratorNodes
                    .toArray(new CFGNode<?>[0]);
            for (int i = 0; i < iteratorNodeArray.length - 1; i++) {
                iteratorNodeArray[i].addForwardNode(iteratorNodeArray[i + 1]);
            }

            if (0 < iteratorNodes.size()) {
                for (final CFGNode<? extends ExecutableElementInfo> innerExitNode : innerCFG
                        .getExitNodes()) {
                    innerExitNode.addForwardNode(iteratorNodeArray[0]);
                }
                iteratorNodeArray[iteratorNodeArray.length - 1].addForwardNode(conditionNode);
            } else {
                for (final CFGNode<? extends ExecutableElementInfo> innerExitNode : innerCFG
                        .getExitNodes()) {
                    innerExitNode.addForwardNode(conditionNode);
                }
            }

        } else if (local instanceof SwitchBlockInfo) {
            // TODO 面倒なので未実装
            this.enterNode = this.nodeFactory.makeNode((ConditionalBlockInfo) local);
            this.enterNode.addForwardNode(innerCFG.enterNode);
            this.exitNodes.addAll(innerCFG.exitNodes);
        } else if (local instanceof DoBlockInfo) {

            final CFGNode<? extends ExecutableElementInfo> controlNode = this.nodeFactory
                    .makeNode((ConditionalBlockInfo) local);

            this.enterNode = !innerCFG.isEmpty() ? innerCFG.getEnterNode() : controlNode;
            this.exitNodes.add(controlNode);
            controlNode.addForwardNode(this.enterNode);
        }
    }

    private CFG buildInnerCFG(final LocalSpaceInfo local) {
        final CFG innerCFG = new IntraProceduralCFG(this.nodeFactory);

        final Iterator<StatementInfo> innerStatements = local.getStatements().iterator();

        if (!innerStatements.hasNext()) {
            return innerCFG;
        }

        CFG preSubCFG = new IntraProceduralCFG(innerStatements.next(), this.nodeFactory);
        innerCFG.enterNode = preSubCFG.getEnterNode();

        while (innerStatements.hasNext()) {
            final StatementInfo nextStatement = innerStatements.next();

            if (nextStatement instanceof ElseBlockInfo) {
                // else文はif文のCFGと同時に構築されるので個別には構築しない
                continue;
            }

            final CFG nextSubCFG = new IntraProceduralCFG(nextStatement, this.nodeFactory);

            if (null != nextSubCFG.getEnterNode()) {
                for (final CFGNode<? extends ExecutableElementInfo> preExitNode : preSubCFG
                        .getExitNodes()) {
                    if (preExitNode.isExitNode(local)) {
                        innerCFG.exitNodes.add(preExitNode);
                    } else {
                        preExitNode.addForwardNode(nextSubCFG.getEnterNode());
                    }
                }

                preSubCFG = nextSubCFG;
            }
        }

        innerCFG.exitNodes.addAll(preSubCFG.getExitNodes());

        return innerCFG;
    }

}
