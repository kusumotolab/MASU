package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.FinallyBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedFinallyBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTryBlockInfo;

public class FinallyBlockBuilder extends InnerBlockBuilder<UnresolvedFinallyBlockInfo> {

    public FinallyBlockBuilder(final BuildDataManager targetDataManager) {
        super(targetDataManager, new FinallyBlockStateManager());
    }

    @Override
    protected UnresolvedFinallyBlockInfo createUnresolvedBlockInfo() {
        final UnresolvedBlockInfo<?> preBlock = this.buildManager.getPreBlock();
        if(preBlock instanceof UnresolvedTryBlockInfo) {
            final UnresolvedTryBlockInfo ownerTry = (UnresolvedTryBlockInfo) preBlock;
            final UnresolvedFinallyBlockInfo finallyBlock = new UnresolvedFinallyBlockInfo(ownerTry);
            
            ownerTry.setSequentFinallyBlock(finallyBlock);
            
            return finallyBlock;
        } else {
            assert false : "Illegal state : incorrect block structure";
            return null;
        }
    }

}
