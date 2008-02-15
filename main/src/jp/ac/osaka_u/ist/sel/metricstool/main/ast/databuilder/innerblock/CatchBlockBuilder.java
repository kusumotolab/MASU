package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.CatchBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCatchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTryBlockInfo;

public class CatchBlockBuilder extends InnerBlockBuilder<UnresolvedCatchBlockInfo> {

    public CatchBlockBuilder(final BuildDataManager targetDataManager) {
        super(targetDataManager, new CatchBlockStateManager());
    }

    @Override
    protected UnresolvedCatchBlockInfo createUnresolvedBlockInfo() {
        final UnresolvedBlockInfo<?> preBlock = this.buildManager.getPreBlock();
        if(preBlock instanceof UnresolvedTryBlockInfo) {
            final UnresolvedTryBlockInfo ownerTry = (UnresolvedTryBlockInfo) preBlock;
            final UnresolvedCatchBlockInfo catchBlock = new UnresolvedCatchBlockInfo(ownerTry);
            
            ownerTry.addSequentCatchBlock(catchBlock);
            
            return catchBlock;
        } else {
            assert false : "Illegal state : incorrect block structure";
            return null;
        }
    }

}
