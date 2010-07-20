package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;


public class CFGExpressionNode extends CFGNormalNode<ExpressionInfo> {

    CFGExpressionNode(final ExpressionInfo expression) {
        super(expression);
    }

    @Override
    ExpressionInfo getDissolvingTarget() {
        return this.getCore();
    }

    @Override
    ExpressionInfo makeNewElement(final LocalSpaceInfo ownerSpace,
            final ExpressionInfo... requiredExpression) {

        if ((null == ownerSpace) || (1 != requiredExpression.length)) {
            throw new IllegalArgumentException();
        }

        return requiredExpression[0];
    }

    @Override
    void replace(List<CFGNode<? extends ExecutableElementInfo>> dissolvedNodeList) {
    }
}
