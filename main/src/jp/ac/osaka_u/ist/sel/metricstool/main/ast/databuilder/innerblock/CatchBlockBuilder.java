package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.CatchBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCatchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTryBlockInfo;

public class CatchBlockBuilder extends InnerBlockBuilder {

    public CatchBlockBuilder(BuildDataManager targetDataManager) {
        super(targetDataManager);
        this.blockStateManager = new CatchBlockStateManager();
    }

    @Override
    protected UnresolvedCatchBlockInfo createUnresolvedBlockInfo() {
        UnresolvedBlockInfo<?> preBlock = this.buildManager.getPreBlock();
        if(preBlock instanceof UnresolvedTryBlockInfo) {
            return new UnresolvedCatchBlockInfo((UnresolvedTryBlockInfo) preBlock);
        } else {
            return null;
        }
    }

}
