package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.CatchBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CatchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedCatchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTryBlockInfo;

public class CatchBlockBuilder extends InnerBlockBuilder<CatchBlockInfo, UnresolvedCatchBlockInfo> {

    public CatchBlockBuilder(final BuildDataManager targetDataManager) {
        super(targetDataManager, new CatchBlockStateManager());
    }

    @Override
    protected UnresolvedCatchBlockInfo createUnresolvedBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        if(outerSpace instanceof UnresolvedTryBlockInfo) {
            final UnresolvedTryBlockInfo ownerTry = (UnresolvedTryBlockInfo) outerSpace;
            
            final UnresolvedCatchBlockInfo catchBlock = new UnresolvedCatchBlockInfo(ownerTry, ownerTry.getOuterSpace());
            
            ownerTry.addSequentCatchBlock(catchBlock);
            
            return catchBlock;
        } else {
            assert false : "Illegal state : incorrect block structure";
            return null;
        }
    }

}
