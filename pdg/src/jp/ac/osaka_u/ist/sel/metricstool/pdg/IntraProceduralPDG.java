package jp.ac.osaka_u.ist.sel.metricstool.pdg;


import java.util.HashSet;
import java.util.Set;

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
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
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

                    {
                        final ConditionInfo condition = ((ConditionalBlockInfo) innerStatement)
                                .getConditionalClause().getCondition();
                        final PDGNode<?> toPDGNode = this.makeControlNode(condition);
                        fromPDGNode.addControlDependingNode(toPDGNode);
                    }

                    if (innerStatement instanceof ForBlockInfo) {
                        final ForBlockInfo forBlock = (ForBlockInfo) innerStatement;
                        for (final ConditionInfo expression : forBlock.getInitializerExpressions()) {
                            final PDGNode<?> toPDGNode = this.makeNormalNode(expression);
                            fromPDGNode.addControlDependingNode(toPDGNode);
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
            final ConditionInfo condition = forBlock.getConditionalClause().getCondition();
            final PDGControlNode extraFromPDGNode = this.makeControlNode(condition);

            for (final ExpressionInfo expression : forBlock.getIteratorExpressions()) {
                final PDGNode<?> extraToPDGNode = this.makeNormalNode(expression);
                extraFromPDGNode.addControlDependingNode(extraToPDGNode);
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
