package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.FinallyBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFinallyBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTryBlockInfo;

public class FinallyBlockBuilder extends InnerBlockBuilder {

    public FinallyBlockBuilder(BuildDataManager targetDataManager) {
        super(targetDataManager);
        
        this.blockStateManager = new FinallyBlockStateManager();
    }

    @Override
    protected UnresolvedFinallyBlockInfo createUnresolvedBlockInfo() {
        UnresolvedBlockInfo<?> preBlock = this.buildManager.getPreBlock();
        if(preBlock instanceof UnresolvedTryBlockInfo) {
            return new UnresolvedFinallyBlockInfo((UnresolvedTryBlockInfo) preBlock);
        } else {
            return null;
        }
    }

}
