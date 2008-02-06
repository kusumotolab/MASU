package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.SynchronizedBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedSynchronizedBlockInfo;

public class SynchronizedBlockBuilder extends InnerBlockBuilder {

    public SynchronizedBlockBuilder(BuildDataManager targetDataManager) {
        super(targetDataManager);

        this.blockStateManager = new SynchronizedBlockStateManager();
    }

    @Override
    protected UnresolvedSynchronizedBlockInfo createUnresolvedBlockInfo() {
        return new UnresolvedSynchronizedBlockInfo();
    }

}
