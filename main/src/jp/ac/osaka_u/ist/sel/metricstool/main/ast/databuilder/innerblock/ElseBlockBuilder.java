package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.ElseBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedIfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;

public class ElseBlockBuilder extends InnerBlockBuilder<ElseBlockInfo, UnresolvedElseBlockInfo> {

    public ElseBlockBuilder(final BuildDataManager targetDataManager) {
        super(targetDataManager, new ElseBlockStateManager());
    }

    @Override
    protected UnresolvedElseBlockInfo createUnresolvedBlockInfo(final UnresolvedLocalSpaceInfo<?> ownerSpace) {
        if(ownerSpace instanceof UnresolvedIfBlockInfo) {
            final UnresolvedIfBlockInfo ownerIf = (UnresolvedIfBlockInfo) ownerSpace;
            final UnresolvedElseBlockInfo elseBlock = new UnresolvedElseBlockInfo(ownerIf, ownerIf.getOwnerSpace());
            
            ownerIf.setSequentElseBlock(elseBlock);
            
            return elseBlock;
        } else {
            assert false : "Illegal state : incorrect block structure";
            return null;
        }
    }

}
