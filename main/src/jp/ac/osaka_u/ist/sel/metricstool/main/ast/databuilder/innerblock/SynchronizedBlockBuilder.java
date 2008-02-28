package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.SynchronizedBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SynchronizedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedSynchronizedBlockInfo;


public class SynchronizedBlockBuilder extends InnerBlockBuilder<SynchronizedBlockInfo, UnresolvedSynchronizedBlockInfo> {

    public SynchronizedBlockBuilder(final BuildDataManager targetDataManager) {
        super(targetDataManager, new SynchronizedBlockStateManager());
    }

    @Override
    protected UnresolvedSynchronizedBlockInfo createUnresolvedBlockInfo(
            final UnresolvedLocalSpaceInfo<?> outerSpace) {
        return new UnresolvedSynchronizedBlockInfo(outerSpace);
    }

}
