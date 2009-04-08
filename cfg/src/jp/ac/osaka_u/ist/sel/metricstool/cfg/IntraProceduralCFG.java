package jp.ac.osaka_u.ist.sel.metricstool.cfg;


import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BreakStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CatchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.DoBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalConstructorInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExternalMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FinallyBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LabelInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SimpleBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SwitchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SynchronizedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TryBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.WhileBlockInfo;


/**
 * 
 * @author t-miyake, higo
 *
 */
public class IntraProceduralCFG extends CFG {

    /**
     * CFG構築対象要素
     */
    private final Object element;

    /**
     * 呼び出し可能ユニットとノードファクトリを与えて，制御フローグラフを生成
     * 
     * @param unit 呼び出し可能ユニット
     * @param nodeFactory ノードファクトリ
     */
    public IntraProceduralCFG(final CallableUnitInfo unit, final ICFGNodeFactory nodeFactory) {

        super(nodeFactory);

        if (null == unit) {
            throw new IllegalArgumentException("unit is null");
        }

        this.element = unit;

        if (unit instanceof ExternalMethodInfo || unit instanceof ExternalConstructorInfo) {
            throw new IllegalArgumentException("unit is an external infromation.");
        }

        final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(unit
                .getStatements(), nodeFactory);
        this.enterNode = statementsCFG.getEnterNode();
        this.exitNodes.addAll(statementsCFG.getExitNodes());
    }

    /**
     * 呼び出し可能ユニットを与えて，制御フローグラフを生成
     * 
     * @param unit 呼び出し可能ユニット
     */
    public IntraProceduralCFG(final CallableUnitInfo unit) {
        this(unit, new DefaultCFGNodeFactory());
    }

    /**
     * 文の制御フローグラフを生成する
     * 
     * @param statement
     * @param nodeFactory
     */
    IntraProceduralCFG(final StatementInfo statement, final ICFGNodeFactory nodeFactory) {

        super(nodeFactory);

        if (null == statement) {
            throw new IllegalArgumentException();
        }

        this.element = statement;

        //単文の場合
        if (statement instanceof SingleStatementInfo) {
            final CFGNormalNode<?> node = nodeFactory.makeNormalNode(statement);
            assert null != node : "node is null!";
            this.enterNode = node;
            this.exitNodes.add(node);
        }

        // caseエントリの場合
        else if (statement instanceof CaseEntryInfo) {

            final CaseEntryInfo caseEntry = (CaseEntryInfo) statement;
            final CFGNormalNode<?> node = nodeFactory.makeNormalNode(caseEntry);
            this.enterNode = node;
            this.exitNodes.add(node);
        }

        // Labelの場合
        else if (statement instanceof LabelInfo) {
            // 何もしなくていいはず
        }

        // if文の場合
        else if (statement instanceof IfBlockInfo) {

            //if文の条件式からコントロールノードを生成
            final IfBlockInfo ifBlock = (IfBlockInfo) statement;
            final ConditionInfo condition = ifBlock.getConditionalClause().getCondition();
            final CFGControlNode controlNode = nodeFactory.makeControlNode(condition);
            assert null != controlNode : "controlNode is null!";
            this.enterNode = controlNode;

            // if文の内側を処理
            {
                final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(ifBlock
                        .getStatements(), nodeFactory);

                // if文の内部が空の場合は，if文の条件式がexitノードになる
                if (statementsCFG.isEmpty()) {
                    this.exitNodes.add(controlNode);
                }

                // if文の内部が空でない場合は，内部の最後の文がexitノードになる
                else {
                    controlNode.addForwardNode(statementsCFG.getEnterNode());
                    this.exitNodes.addAll(statementsCFG.getExitNodes());
                }
            }

            //対応するelse文がある場合の処理
            if (ifBlock.hasElseBlock()) {
                final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(ifBlock
                        .getSequentElseBlock().getStatements(), nodeFactory);

                // else文の内部が空の場合は，if文の条件式がexitノードになる
                if (statementsCFG.isEmpty()) {
                    this.exitNodes.add(controlNode);
                }

                // else文の内部が～でない場合は，内部の文の最後の文がexitノードになる
                else {
                    controlNode.addForwardNode(statementsCFG.getEnterNode());
                    this.exitNodes.addAll(statementsCFG.getExitNodes());
                }
            }

            //対応するelse文がない場合は，if文の条件式がexitノードになる
            else {
                this.exitNodes.add(controlNode);
            }
        }

        // while文の場合
        else if (statement instanceof WhileBlockInfo) {

            // while文の条件式からコントロールノードを生成
            final WhileBlockInfo whileBlock = (WhileBlockInfo) statement;
            final ConditionInfo condition = whileBlock.getConditionalClause().getCondition();
            final CFGControlNode controlNode = nodeFactory.makeControlNode(condition);
            assert null != controlNode : "controlNode is null!";
            this.enterNode = controlNode;
            this.exitNodes.add(controlNode);

            // while文内部の処理
            final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(whileBlock
                    .getStatements(), nodeFactory);

            // 内部が空でない場合は処理を行う
            if (!statementsCFG.isEmpty()) {
                controlNode.addForwardNode(statementsCFG.getEnterNode());
                for (final CFGNode<?> exitNode : statementsCFG.getExitNodes()) {
                    exitNode.addForwardNode(controlNode);
                }
            }
        }

        // else 文の場合
        else if (statement instanceof ElseBlockInfo) {
            //else文は対応するif文で処理しているため，ここではなにもしない
        }

        // do文の場合
        else if (statement instanceof DoBlockInfo) {

            // do文の条件式からコントロールノードを生成
            final DoBlockInfo doBlock = (DoBlockInfo) statement;
            final ConditionInfo condition = doBlock.getConditionalClause().getCondition();
            final CFGControlNode controlNode = nodeFactory.makeControlNode(condition);
            assert null != controlNode : "controlNode is null!";
            this.exitNodes.add(controlNode);

            // do文内部の処理
            final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(doBlock
                    .getStatements(), nodeFactory);

            // 内部が空の時は，do文の条件式がenterノードになる
            if (statementsCFG.isEmpty()) {
                this.enterNode = controlNode;
            }

            // 空でない場合は，内部CFGのenterノードが，このCFGのenterノードになる
            else {
                this.enterNode = statementsCFG.getEnterNode();
                for (final CFGNode<?> exitNode : statementsCFG.getExitNodes()) {
                    exitNode.addForwardNode(controlNode);
                }
            }
        }

        // for文の場合
        else if (statement instanceof ForBlockInfo) {

            // for文の条件式からコントロールノードを生成
            final ForBlockInfo forBlock = (ForBlockInfo) statement;
            final ConditionInfo condition = forBlock.getConditionalClause().getCondition();
            final CFGControlNode controlNode = nodeFactory.makeControlNode(condition);
            assert null != controlNode : "controlNode is null";
            this.exitNodes.add(controlNode);

            //初期化式からCFGを生成
            final SortedSet<ConditionInfo> initializers = forBlock.getInitializerExpressions();
            final SequentialExpressionsCFG initializersCFG = new SequentialExpressionsCFG(
                    initializers, nodeFactory);

            //初期化式をfor文のCFGに追加
            if (initializersCFG.isEmpty()) {
                this.enterNode = controlNode;
            } else {
                this.enterNode = initializersCFG.getEnterNode();
                for (final CFGNode<?> exitNode : initializersCFG.getExitNodes()) {
                    exitNode.addForwardNode(controlNode);
                }
            }

            //繰り返し式からCFGを生成
            final SortedSet<ExpressionInfo> iterators = forBlock.getIteratorExpressions();
            final SequentialExpressionsCFG iteratorsCFG = new SequentialExpressionsCFG(iterators,
                    nodeFactory);

            // for文の内部の処理
            final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(forBlock
                    .getStatements(), nodeFactory);
            // for文の内部が空の場合
            if (statementsCFG.isEmpty()) {

                //繰り返し式が空の場合
                if (iteratorsCFG.isEmpty()) {
                    controlNode.addForwardNode(controlNode);
                }

                //繰り返し式が空でない場合
                else {
                    controlNode.addForwardNode(iteratorsCFG.getEnterNode());
                    for (final CFGNode<?> exitNode : iteratorsCFG.getExitNodes()) {
                        exitNode.addForwardNode(controlNode);
                    }
                }
            }

            // for文の内部が空でない場合
            else {

                controlNode.addForwardNode(statementsCFG.getEnterNode());

                //繰り返し式が空の場合
                if (iteratorsCFG.isEmpty()) {

                    for (final CFGNode<?> exitNode : statementsCFG.getExitNodes()) {
                        exitNode.addForwardNode(controlNode);
                    }
                }

                //繰り返し式が空でない場合
                else {

                    for (final CFGNode<?> exitNode : statementsCFG.getExitNodes()) {
                        exitNode.addForwardNode(iteratorsCFG.getEnterNode());
                    }

                    for (final CFGNode<?> exitNode : iteratorsCFG.getExitNodes()) {
                        exitNode.addForwardNode(controlNode);
                    }
                }
            }
        }

        // switch文の場合
        else if (statement instanceof SwitchBlockInfo) {

            // switch文の条件式からコントロールノードを生成
            final SwitchBlockInfo switchBlock = (SwitchBlockInfo) statement;
            final ConditionInfo condition = switchBlock.getConditionalClause().getCondition();
            final CFGControlNode controlNode = nodeFactory.makeControlNode(condition);
            assert null != controlNode : "controlNode is null!";
            this.enterNode = controlNode;

            // 空のCFGを取り除く処理
            final List<IntraProceduralCFG> statementCFGs = new ArrayList<IntraProceduralCFG>();
            for (final StatementInfo innerStatement : switchBlock.getStatements()) {
                final IntraProceduralCFG innerStatementCFG = new IntraProceduralCFG(innerStatement,
                        nodeFactory);
                if (!innerStatementCFG.isEmpty()) {
                    statementCFGs.add(innerStatementCFG);
                }
            }

            for (int i = 0; i < statementCFGs.size() - 1; i++) {

                final IntraProceduralCFG fromCFG = statementCFGs.get(i);
                final IntraProceduralCFG toCFG = statementCFGs.get(i + 1);

                // fromCFGがbreak文の場合
                if (fromCFG.getElement() instanceof BreakStatementInfo) {
                    this.exitNodes.addAll(fromCFG.getExitNodes());
                }

                // break文でない時は，fromCFGとtoCFGをつなぐ
                else {

                    for (final CFGNode<?> exitNode : fromCFG.getExitNodes()) {
                        exitNode.addForwardNode(toCFG.getEnterNode());
                    }

                    //fromCFGがcase文である場合は，switch文の条件式から依存辺を引く
                    if (fromCFG.getElement() instanceof CaseEntryInfo) {
                        controlNode.addForwardNode(fromCFG.getEnterNode());
                    }
                }
            }

            final IntraProceduralCFG lastCFG = statementCFGs.get(statementCFGs.size() - 1);
            this.exitNodes.addAll(lastCFG.getExitNodes());
        }

        // try文の場合
        else if (statement instanceof TryBlockInfo) {

            final TryBlockInfo tryBlock = (TryBlockInfo) statement;
            final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(tryBlock
                    .getStatements(), nodeFactory);
            this.enterNode = statementsCFG.getEnterNode();
            this.exitNodes.addAll(statementsCFG.getExitNodes());
        }

        // catch文の場合
        else if (statement instanceof CatchBlockInfo) {

            final CatchBlockInfo catchBlock = (CatchBlockInfo) statement;
            final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(catchBlock
                    .getStatements(), nodeFactory);
            this.enterNode = statementsCFG.getEnterNode();
            this.exitNodes.addAll(statementsCFG.getExitNodes());
        }

        // finally文の場合
        else if (statement instanceof FinallyBlockInfo) {

            final FinallyBlockInfo finallyBlock = (FinallyBlockInfo) statement;
            final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(finallyBlock
                    .getStatements(), nodeFactory);
            this.enterNode = statementsCFG.getEnterNode();
            this.exitNodes.addAll(statementsCFG.getExitNodes());
        }

        // simple文の場合
        else if (statement instanceof SimpleBlockInfo) {

            final SimpleBlockInfo simpleBlock = (SimpleBlockInfo) statement;
            final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(simpleBlock
                    .getStatements(), nodeFactory);
            this.enterNode = statementsCFG.getEnterNode();
            this.exitNodes.addAll(statementsCFG.getExitNodes());
        }

        // synchorized文の場合
        else if (statement instanceof SynchronizedBlockInfo) {

            final SynchronizedBlockInfo synchronizedBlock = (SynchronizedBlockInfo) statement;
            final SequentialStatementsCFG statementsCFG = new SequentialStatementsCFG(
                    synchronizedBlock.getStatements(), nodeFactory);
            this.enterNode = statementsCFG.getEnterNode();
            this.exitNodes.addAll(statementsCFG.getExitNodes());
        }

        else {
            assert false : "Here shouldn't be reached!";
        }
    }

    /**
     * CFG構築対象要素を返す
     * 
     * @return CFG構築対象要素
     */
    public Object getElement() {
        return this.element;
    }

    /**
     * StatementInfoの列からCFGを作成するクラス
     * 
     * @author higo
     *
     */
    private class SequentialStatementsCFG extends CFG {

        SequentialStatementsCFG(final SortedSet<StatementInfo> statements,
                final ICFGNodeFactory nodeFactory) {

            super(nodeFactory);

            // 空のCFGを除去する処理
            final List<IntraProceduralCFG> statementCFGs = new ArrayList<IntraProceduralCFG>();
            for (final StatementInfo statement : statements) {
                final IntraProceduralCFG statementCFG = new IntraProceduralCFG(statement,
                        nodeFactory);
                if (!statementCFG.isEmpty()) {
                    statementCFGs.add(statementCFG);
                }
            }

            if (0 == statementCFGs.size()) {
                return;
            }

            //最初の文からenterノードを生成
            {
                this.enterNode = statementCFGs.get(0).getEnterNode();
            }

            //最後の文からexitノードを生成
            {
                this.exitNodes.addAll(statementCFGs.get(statementCFGs.size() - 1).getExitNodes());
            }

            //statementsから生成したCFGを順番につないでいく
            {
                for (int i = 0; i < statementCFGs.size() - 1; i++) {
                    final IntraProceduralCFG fromCFG = statementCFGs.get(i);
                    final IntraProceduralCFG toCFG = statementCFGs.get(i + 1);

                    for (final CFGNode<?> exitNode : fromCFG.getExitNodes()) {
                        exitNode.addForwardNode(toCFG.getEnterNode());
                    }
                }
            }
        }
    }

    /**
     * ExpressionInfoの列，またはCOnditionInfoの列からCFGを作成するクラス
     * 
     * @author higo
     *
     */
    private class SequentialExpressionsCFG extends CFG {

        SequentialExpressionsCFG(final SortedSet<? extends ConditionInfo> expressions,
                final ICFGNodeFactory nodeFactory) {

            super(nodeFactory);

            if (0 == expressions.size()) {
                return;
            }

            // 最初の式からenterノードを生成
            {
                final ConditionInfo firstExpression = expressions.first();
                final CFGNormalNode<?> firstExpressionNode = nodeFactory
                        .makeNormalNode(firstExpression);
                this.enterNode = firstExpressionNode;
            }

            // 最後の式からexitノードを生成
            {
                final ConditionInfo lastExpression = expressions.last();
                final CFGNormalNode<?> lastExpressionNode = nodeFactory
                        .makeNormalNode(lastExpression);
                this.exitNodes.add(lastExpressionNode);
            }

            // expressions から生成したノードを順番につないでいく
            final ConditionInfo[] expressionArray = expressions.toArray(new ConditionInfo[0]);
            for (int i = 0; i < expressionArray.length - 1; i++) {
                final CFGNormalNode<?> fromNode = nodeFactory.makeNormalNode(expressionArray[i]);
                final CFGNormalNode<?> toNode = nodeFactory.makeNormalNode(expressionArray[i + 1]);
                fromNode.addForwardNode(toNode);
            }
        }
    }
}
