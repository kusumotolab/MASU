package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.innerblock;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock.SwitchBlockStateManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedSwitchBlockInfo;

public class SwitchBlockBuilder extends ConditionalBlockBuilder {

    public SwitchBlockBuilder(final BuildDataManager targetDataManager) {
        super(targetDataManager, new SwitchBlockStateManager());
    }

    @Override
    protected UnresolvedSwitchBlockInfo createUnresolvedBlockInfo() {
        return new UnresolvedSwitchBlockInfo();
    }

}
