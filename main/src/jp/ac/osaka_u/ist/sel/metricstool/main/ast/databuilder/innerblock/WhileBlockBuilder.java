package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.WhileBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.WhileBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedWhileBlockInfo;


public class WhileBlockBuilder extends
        ConditionalBlockBuilder<WhileBlockInfo, UnresolvedWhileBlockInfo> {

    public WhileBlockBuilder(final BuildDataManager targetDataManager,
            final ExpressionElementManager expressionManager) {
        super(targetDataManager, new WhileBlockStateManager(), expressionManager);
    }

    @Override
    protected UnresolvedWhileBlockInfo createUnresolvedBlockInfo(
            final UnresolvedLocalSpaceInfo<?> outerSpace) {
        return new UnresolvedWhileBlockInfo(outerSpace);
    }

}
