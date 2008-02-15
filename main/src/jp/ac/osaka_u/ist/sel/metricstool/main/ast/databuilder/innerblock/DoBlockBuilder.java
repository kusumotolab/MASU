package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.DoBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedDoBlockInfo;

public class DoBlockBuilder extends ConditionalBlockBuilder<UnresolvedDoBlockInfo> {

    public DoBlockBuilder(final BuildDataManager targetDataManager) {
        super(targetDataManager, new DoBlockStateManager());
    }

    @Override
    protected UnresolvedDoBlockInfo createUnresolvedBlockInfo() {
        return new UnresolvedDoBlockInfo();
    }

}
