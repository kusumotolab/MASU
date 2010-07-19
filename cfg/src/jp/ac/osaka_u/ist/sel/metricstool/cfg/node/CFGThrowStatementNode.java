package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import java.util.Collections;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGUtility;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.SimpleCFG;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGEdge;
import jp.ac.osaka_u.ist.sel.metricstool.cfg.edge.CFGNormalEdge;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallableUnitInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ThrowStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;


/**
 * throw文を表すノード
 * 
 * @author higo
 * 
 */
public class CFGThrowStatementNode extends CFGStatementNode<ThrowStatementInfo> {

    /**
     * ノードを生成するthrow文を与えて初期化
     * 
     * @param throwStatement
     */
    CFGThrowStatementNode(final ThrowStatementInfo throwStatement) {
        super(throwStatement);
    }

    @Override
    public CFG dissolve(final ICFGNodeFactory nodeFactory) {

        final ThrowStatementInfo statement = this.getCore();
        final ExpressionInfo expression = statement.getThrownExpression();

        // assertの判定部分が変数使用の式でない場合は分解を行う
        if (!CFGUtility.isDissolved(expression)) {
            return null;
        }

        // 分解前の文から必要な情報を取得
        final LocalSpaceInfo ownerSpace = statement.getOwnerSpace();
        final int fromLine = statement.getFromLine();
        final int toLine = statement.getToLine();
        final CallableUnitInfo outerCallableUnit = ownerSpace instanceof CallableUnitInfo ? (CallableUnitInfo) ownerSpace
                : ownerSpace.getOuterCallableUnit();

        // ダミー変数の宣言を生成
        final LocalVariableInfo dummyVariable = new LocalVariableInfo(Collections
                .<ModifierInfo> emptySet(), getDummyVariableName(), expression.getType(),
                ownerSpace, fromLine, CFGUtility.getRandomNaturalValue(), toLine, CFGUtility
                        .getRandomNaturalValue());
        final LocalVariableUsageInfo dummyVariableInitialization = LocalVariableUsageInfo
                .getInstance(dummyVariable, false, true, outerCallableUnit, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final VariableDeclarationStatementInfo dummyVariableDeclaration = new VariableDeclarationStatementInfo(
                dummyVariableInitialization, expression, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());

        // ダミー変数を利用したassert文を生成
        final LocalVariableUsageInfo dummyVariableUsage = LocalVariableUsageInfo.getInstance(
                dummyVariable, true, false, outerCallableUnit, fromLine, CFGUtility
                        .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
        final ThrowStatementInfo newThrowStatement = new ThrowStatementInfo(ownerSpace,
                dummyVariableUsage, fromLine, CFGUtility.getRandomNaturalValue(), toLine,
                CFGUtility.getRandomNaturalValue());

        // 古いノードを削除
        nodeFactory.removeNode(statement);
        this.remove();

        // 新しい文のCFGノードを作成し，フローを生成
        final CFGNode<?> definitionNode = nodeFactory.makeNormalNode(dummyVariableDeclaration);
        final CFGNode<?> referenceNode = nodeFactory.makeNormalNode(newThrowStatement);
        final CFGEdge newEdge = new CFGNormalEdge(definitionNode, referenceNode);
        definitionNode.addForwardEdge(newEdge);
        referenceNode.addBackwardEdge(newEdge);
        for (final CFGEdge backwardEdge : this.getBackwardEdges()) {
            final CFGNode<?> backwardNode = backwardEdge.getFromNode();
            final CFGEdge newBackwardEdge = backwardEdge.replaceToNode(definitionNode);
            backwardNode.addForwardEdge(newBackwardEdge);
        }
        for (final CFGEdge forwardEdge : this.getForwardEdges()) {
            final CFGNode<?> forwardNode = forwardEdge.getToNode();
            final CFGEdge newForwardEdge = forwardEdge.replaceFromNode(referenceNode);
            forwardNode.addBackwardEdge(newForwardEdge);
        }

        final SimpleCFG newCFG = new SimpleCFG(nodeFactory);
        newCFG.addNode(definitionNode);
        newCFG.addNode(referenceNode);
        newCFG.setEnterNode(definitionNode);
        newCFG.addExitNode(referenceNode);

        // 抽出したExpressionに対しては再帰的にdissolveを実行
        final CFG definitionCFG = definitionNode.dissolve(nodeFactory);
        if (null != definitionCFG) {
            newCFG.removeNode(definitionNode);
            newCFG.addNodes(definitionCFG.getAllNodes());
            newCFG.setEnterNode(definitionCFG.getEnterNode());
        }

        return newCFG;
    }
}
