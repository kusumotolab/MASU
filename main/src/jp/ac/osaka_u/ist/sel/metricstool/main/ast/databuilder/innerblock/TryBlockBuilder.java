package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.TryBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTryBlockInfo;

public class TryBlockBuilder extends InnerBlockBuilder {

    public TryBlockBuilder(BuildDataManager targetDataManager) {
        super(targetDataManager);

        this.blockStateManager = new TryBlockStateManager();
    }

    @Override
    protected UnresolvedTryBlockInfo createUnresolvedBlockInfo() {
        return new UnresolvedTryBlockInfo();
    }

}
