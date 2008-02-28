package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.DoBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.DoBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedDoBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;


public class DoBlockBuilder extends ConditionalBlockBuilder<DoBlockInfo, UnresolvedDoBlockInfo> {

    public DoBlockBuilder(final BuildDataManager targetDataManager) {
        super(targetDataManager, new DoBlockStateManager());
    }

    @Override
    protected UnresolvedDoBlockInfo createUnresolvedBlockInfo(final UnresolvedLocalSpaceInfo<?> outerSpace) {
        return new UnresolvedDoBlockInfo(outerSpace);
    }

}
