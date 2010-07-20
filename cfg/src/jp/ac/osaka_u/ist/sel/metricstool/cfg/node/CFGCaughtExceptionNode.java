package jp.ac.osaka_u.ist.sel.metricstool.cfg.node;


import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.cfg.CaughtExceptionDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LocalSpaceInfo;


/**
 * catch節の例外部を表すCFGノード
 * @author higo
 *
 */
public class CFGCaughtExceptionNode extends CFGNormalNode<CaughtExceptionDeclarationStatementInfo> {

    CFGCaughtExceptionNode(final CaughtExceptionDeclarationStatementInfo caughtException) {
        super(caughtException);
    }

    @Override
    final ExpressionInfo getDissolvingTarget() {
        return null;
    }

    @Override
    CaughtExceptionDeclarationStatementInfo makeNewElement(final LocalSpaceInfo ownerSpace,
            final ExpressionInfo... requiredExpressions) {
        return null;
    }

    @Override
    void replace(List<CFGNode<? extends ExecutableElementInfo>> dissolvedNodeList) {
    }
}
