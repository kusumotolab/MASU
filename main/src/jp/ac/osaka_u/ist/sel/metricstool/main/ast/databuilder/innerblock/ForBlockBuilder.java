package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.ForBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;


public class ForBlockBuilder extends ConditionalBlockBuilder<ForBlockInfo, UnresolvedForBlockInfo> {

    public ForBlockBuilder(final BuildDataManager targetDataManager) {
        super(targetDataManager, new ForBlockStateManager());
    }

    @Override
    protected UnresolvedForBlockInfo createUnresolvedBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        return new UnresolvedForBlockInfo(outerSpace);
    }

}
