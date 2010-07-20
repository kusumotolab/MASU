package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CFGUtility;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalVariableUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;


/**
 * CFGの制御ノードを表すクラス
 * 
 * @author t-miyake, higo
 * 
 */
public class CFGControlNode extends CFGNode<ConditionInfo> {

    CFGControlNode(final ConditionInfo condition) {
        super(condition);
    }

    @Override
    final ExpressionInfo getDissolvingTarget() {
        final ConditionInfo condition = this.getCore();
        if (condition instanceof VariableDeclarationStatementInfo) {
            final VariableDeclarationStatementInfo statement = (VariableDeclarationStatementInfo) condition;
            if (statement.isInitialized()) {
                return statement.getInitializationExpression();
            }
        } else if (condition instanceof ExpressionInfo) {
            return (ExpressionInfo) condition;
        } else {
            throw new IllegalStateException();
        }
        return null;
    }

    /**
     * 与えられた引数の情報を用いて，ノードの核となるプログラム要素を生成する
     */
    @Override
    ConditionInfo makeNewElement(final LocalSpaceInfo ownerSpace,
            final ExpressionInfo... requiredExpression) {

        if ((null == ownerSpace) || (1 != requiredExpression.length)) {
            throw new IllegalArgumentException();
        }

        final ConditionInfo condition = this.getCore();
        final int fromLine = condition.getFromLine();
        final int toLine = condition.getToLine();

        if (condition instanceof VariableDeclarationStatementInfo) {

            final VariableDeclarationStatementInfo statement = (VariableDeclarationStatementInfo) condition;
            final LocalVariableUsageInfo variableDeclaration = statement.getDeclaration();
            final VariableDeclarationStatementInfo newStatement = new VariableDeclarationStatementInfo(
                    ownerSpace, variableDeclaration, requiredExpression[0], fromLine, CFGUtility
                            .getRandomNaturalValue(), toLine, CFGUtility.getRandomNaturalValue());
            return newStatement;
        }

        else if (condition instanceof ExpressionInfo) {
            return requiredExpression[0];
        }

        else {
            throw new IllegalStateException();
        }
    }

    @Override
    void replace(List<CFGNode<? extends ExecutableElementInfo>> dissolvedNodeList) {
    }
}
