package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.IfBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedIfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedLocalSpaceInfo;


public class IfBlockBuilder extends ConditionalBlockBuilder<IfBlockInfo, UnresolvedIfBlockInfo> {

    public IfBlockBuilder(final BuildDataManager buildManager) {
        super(buildManager, new IfBlockStateManager());
    }

    @Override
    protected UnresolvedIfBlockInfo createUnresolvedBlockInfo(final UnresolvedLocalSpaceInfo<?> ownerSpace) {
        return new UnresolvedIfBlockInfo(ownerSpace);
    }

}
